package kg.manurov.weathergridservice.services.components;

import kg.manurov.weathergridservice.dto.responses.OpenMeteoCombinedResponse;
import kg.manurov.weathergridservice.dto.responses.OpenMeteoDailyResponse;
import kg.manurov.weathergridservice.dto.responses.OpenMeteoHourlyResponse;
import kg.manurov.weathergridservice.entities.WeatherDailyHistory;
import kg.manurov.weathergridservice.entities.WeatherDailyHistoryId;
import kg.manurov.weathergridservice.repositories.WeatherDailyHistoryRepository;
import kg.manurov.weathergridservice.repositories.WeatherLocationRepository;
import kg.manurov.weathergridservice.repositories.projections.LocationIdCoord;
import kg.manurov.weathergridservice.util.GeometryHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class DailyHistoryUpdateTask {
    private final WeatherLocationRepository locationRepo;
    private final WeatherDailyHistoryRepository historyRepo;
    private final WebClient openMeteoWebClient;
    private static final long PAUSE_MILLIS = 500L;

    @Scheduled(cron = "0 0 1 * * *", zone = "Asia/Bishkek")
    public void updateDailyHistory() {
        LocalDate yesterday = LocalDate.now(ZoneId.of("Asia/Bishkek")).minusDays(1);
        log.info("Запуск ежедневного обновления истории погоды за {}", yesterday);

        List<LocationIdCoord> locations = locationRepo.findDistinctLocationsWithFields();
        log.info("Найдено {} активных узлов для обновления", locations.size());

        for (LocationIdCoord loc : locations) {
            saveLoc(loc, yesterday);
        }

        log.info("Завершено ежедневное обновление истории погоды");
    }

    private void saveLoc(LocationIdCoord loc, LocalDate yesterday) {
        try {
            // 1) Собираем URI с параметрами
            String uri = getUri(loc, yesterday);

            // 2) Запрашиваем и блокируем поток до ответа
            OpenMeteoCombinedResponse resp = openMeteoWebClient.get()
                    .uri(uri)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .flatMap(body -> {
                                        log.error("Ошибка при запросе к Open-Meteo: статус={}, тело={}", response.statusCode(), body);
                                        return Mono.error(new RuntimeException("Ошибка Open-Meteo: " + body));
                                    })
                    )
                    .bodyToMono(OpenMeteoCombinedResponse.class)
                    .block();

            if (resp != null && resp.getDaily() != null) {
                saveCombined(loc, resp, yesterday);
            } else {
                log.warn("Нет данных daily для location_id={}", loc.getId());
            }

            Thread.sleep(PAUSE_MILLIS);

        } catch (Exception ex) {
            log.error("Ошибка обновления history для location_id={}", loc.getId(), ex);
        }
    }

    private void saveCombined(LocationIdCoord loc, OpenMeteoCombinedResponse resp, LocalDate yesterday) {
        // 1) daily
        var d = resp.getDaily();
        double max = d.getTemperature2mMax().getFirst();
        double min = d.getTemperature2mMin().getFirst();
        double sum = d.getPrecipitationSum().getFirst();

        // 2) hourly aggregation
        var h = resp.getHourly();
        DoubleSummaryStatistics stTemp = h.getTemperature2m().stream()
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .summaryStatistics();
        DoubleSummaryStatistics stHum  = h.getRelativeHumidity2m().stream()
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .summaryStatistics();
        DoubleSummaryStatistics stWind = h.getWindspeed10m().stream()
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .summaryStatistics();
        DoubleSummaryStatistics stCloud= h.getCloudcover().stream()
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .summaryStatistics();
        DoubleSummaryStatistics stSoil = h.getSoilMoisture().stream()
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .summaryStatistics();
        DoubleSummaryStatistics stPres = h.getPressureMsl().stream()
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .summaryStatistics();
        DoubleSummaryStatistics stSnow = h.getSnowDepth().stream()
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .summaryStatistics();

        // mode wind direction
        String modeDir = windDirection(h);

        // 3) сохраняем
        WeatherDailyHistory hist = new WeatherDailyHistory();
        hist.setId(new WeatherDailyHistoryId(loc.getId(), yesterday))
                .setLocationId(loc.getId())
                .setTempMax(max)
                .setTempMin(min)
                .setTempAvg(stTemp.getAverage())
                .setPrecipitationSum(sum)
                .setHumidityAvg(stHum.getAverage())
                .setWindSpeedAvg(stWind.getAverage())
                .setWindDirection(modeDir)
                .setCloudCover(stCloud.getAverage())
                .setSoilMoisture(stSoil.getAverage())
                .setPressure(stPres.getAverage())
                .setSnowDepth(stSnow.getMax());

        historyRepo.save(hist);
        log.info("Обновлена история для location_id={}", loc.getId());
    }

    private String windDirection(OpenMeteoHourlyResponse.Hourly h) {
        double modeAngle = h.getWinddirection10m().stream()
                .collect(Collectors.groupingBy(v -> v, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(h.getWinddirection10m().getFirst());

        return GeometryHelper.toCardinal(modeAngle);
    }

    private String getUri(LocationIdCoord loc, LocalDate yesterday) {
        return UriComponentsBuilder.fromPath("/v1/forecast")
                .queryParam("latitude", loc.getLatitude())
                .queryParam("longitude", loc.getLongitude())
                .queryParam("daily", "temperature_2m_max,temperature_2m_min,precipitation_sum")
                .queryParam("hourly", "temperature_2m,precipitation,relativehumidity_2m,windspeed_10m,winddirection_10m,cloudcover,soil_moisture_0_to_7cm,pressure_msl,snow_depth")
                .queryParam("start_date", yesterday)
                .queryParam("end_date", yesterday)
                .queryParam("timezone", "auto")
                .build().toUriString();
    }
}
