package kg.manurov.weathergridservice.services.impl;

import kg.manurov.weathergridservice.dto.responses.OpenMeteoCombinedResponse;
import kg.manurov.weathergridservice.dto.responses.OpenMeteoDailyResponse;
import kg.manurov.weathergridservice.dto.responses.OpenMeteoHourlyResponse;
import kg.manurov.weathergridservice.entities.WeatherDailyHistory;
import kg.manurov.weathergridservice.entities.WeatherDailyHistoryId;
import kg.manurov.weathergridservice.repositories.WeatherDailyHistoryRepository;
import kg.manurov.weathergridservice.repositories.WeatherLocationRepository;
import kg.manurov.weathergridservice.repositories.projections.LocationIdCoord;
import kg.manurov.weathergridservice.services.interfaces.DailyHistoryUpdateService;
import kg.manurov.weathergridservice.util.GeometryHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DailyHistoryUpdateServiceImpl implements DailyHistoryUpdateService {
    private final WeatherLocationRepository locationRepo;
    private final WeatherDailyHistoryRepository historyRepo;
    private final WebClient openMeteoWebClient;
    private static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Bishkek");
    private static final int FORECAST_DAYS_LIMIT = 16;
    private static final long PAUSE_MILLIS = 500L;

    @Scheduled(cron = "0 0 2 * * *", zone = "Asia/Bishkek")
    public void updateDailyHistory() {
        LocalDate yesterday = LocalDate.now(DEFAULT_ZONE).minusDays(1);
        log.info("Запуск ежедневного обновления истории погоды за {}", yesterday);

        List<LocationIdCoord> locations = locationRepo.findDistinctLocationsWithFields();
        log.info("Найдено {} активных узлов для обновления", locations.size());

        for (LocationIdCoord loc : locations) {
            saveLoc(loc, yesterday);
        }

        log.info("Завершено ежедневное обновление истории погоды");
    }

    @Override
    public void updateRange(LocalDate start, LocalDate end) {
        log.info("Начинаем backfill c {} по {}", start, end);
        List<LocationIdCoord> locations = locationRepo.findDistinctLocationsWithFields();
        for (LocationIdCoord loc : locations) {
            LocalDate date = start;
            while (!date.isAfter(end)) {
                saveLoc(loc, date);
                date = date.plusDays(1);
            }
        }
        log.info("Backfill завершён");
    }

    private void saveLoc(LocationIdCoord loc, LocalDate date) {
        try {
            boolean archive = !isWithinForecastRange(date);
            String uri = getUri(loc, date, archive);

            OpenMeteoCombinedResponse resp = openMeteoWebClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(OpenMeteoCombinedResponse.class)
                    .block();

            if (resp != null && resp.getDaily() != null) {
                saveCombined(loc, resp, date);
            } else {
                log.warn("Нет данных daily для location_id={}", loc.getId());
            }

            Thread.sleep(PAUSE_MILLIS);

        } catch (Exception ex) {
            log.error("Ошибка обновления history для location_id={}", loc.getId(), ex);
            throw new RuntimeException("Ошибка обновления history для location_id=" + loc.getId(), ex.getCause() != null ? ex.getCause() : ex);
        }
    }

    private void saveCombined(LocationIdCoord loc, OpenMeteoCombinedResponse resp, LocalDate yesterday) {
        OpenMeteoDailyResponse.Daily d = resp.getDaily();
        double max = safeFirst(d.getTemperature2mMax());
        double min = safeFirst(d.getTemperature2mMin());
        double sum = safeFirst(d.getPrecipitationSum());

        OpenMeteoHourlyResponse.Hourly h = resp.getHourly();
        DoubleSummaryStatistics stTemp  = summaryStats(h.getTemperature2m());
        DoubleSummaryStatistics stHum   = summaryStats(h.getRelativeHumidity2m());
        DoubleSummaryStatistics stWind  = summaryStats(h.getWindspeed10m());
        DoubleSummaryStatistics stCloud = summaryStats(h.getCloudcover());
        DoubleSummaryStatistics stSoil  = summaryStats(h.getSoilMoisture());
        DoubleSummaryStatistics stPres  = summaryStats(h.getPressureMsl());
        DoubleSummaryStatistics stSnow  = summaryStats(h.getSnowDepth());

        String modeDir = windDirection(h);

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
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(v -> v, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(h.getWinddirection10m().stream()
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElse(0.0));

        return GeometryHelper.toCardinal(modeAngle);

    }

    private double safeFirst(List<Double> list) {
        if (list == null || list.isEmpty() || list.getFirst() == null) {
            return Double.NaN;
        }
        return list.getFirst();
    }

    private DoubleSummaryStatistics summaryStats(List<Double> data) {
        return Optional.ofNullable(data)
                .orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .map(d -> BigDecimal.valueOf(d)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue())
                .mapToDouble(Double::doubleValue)
                .summaryStatistics();
    }

    private String getUri(LocationIdCoord loc, LocalDate yesterday, boolean archive) {
        UriComponentsBuilder b = archive
                ? UriComponentsBuilder.fromUriString("https://archive-api.open-meteo.com/v1/archive")
                : UriComponentsBuilder.fromPath("/v1/forecast");
        return b
                .queryParam("latitude", loc.getLatitude())
                .queryParam("longitude", loc.getLongitude())
                .queryParam("daily", "temperature_2m_max,temperature_2m_min,precipitation_sum")
                .queryParam("hourly", "temperature_2m,precipitation,relativehumidity_2m," +
                                      "windspeed_10m,winddirection_10m,cloudcover," +
                                      "soil_moisture_0_to_7cm,pressure_msl,snow_depth")
                .queryParam("start_date", yesterday)
                .queryParam("end_date", yesterday)
                .queryParam("timezone", "auto")
                .build().toUriString();
    }

    private boolean isWithinForecastRange(LocalDate date) {
        LocalDate today = LocalDate.now(DEFAULT_ZONE);
        long daysAgo = ChronoUnit.DAYS.between(date, today);
        return daysAgo >= 0 && daysAgo <= FORECAST_DAYS_LIMIT;
    }
}
