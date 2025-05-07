package kg.manurov.weathergridservice.services.impl;

import kg.manurov.weathergridservice.dto.WeatherHistoryDto;
import kg.manurov.weathergridservice.mapper.WeatherDailyHistoryMapper;
import kg.manurov.weathergridservice.repositories.WeatherDailyHistoryRepository;
import kg.manurov.weathergridservice.services.interfaces.WeatherHistoryService;
import kg.manurov.weathergridservice.services.interfaces.WeatherLocationService;
import kg.manurov.weathergridservice.util.GeometryHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherHistoryServiceImpl implements WeatherHistoryService {
    private final WeatherDailyHistoryRepository dailyHistoryRepo;
    private final WeatherLocationService weatherLocationService;
    private final WeatherDailyHistoryMapper dailyHistoryMapper;

    @Override
    public List<WeatherHistoryDto> getHistory(Double latitude, Double longitude, LocalDate startDate, LocalDate endDate, String aggregate) {
        Double lat = GeometryHelper.roundToCenter(latitude);
        Double lon = GeometryHelper.roundToCenter(longitude);
        Long locationId = weatherLocationService.getOrCreateLocationId(lat, lon);

        String dateExpr = switch (aggregate != null ? aggregate : "day") {
            case "month" -> "date_trunc('month', date)";
            case "year" -> "date_trunc('year', date)";
            default -> "date";
        };

        return dailyHistoryRepo.findAggregatedHistory(locationId, startDate, endDate, dateExpr)
                .stream()
                .map(p -> new WeatherHistoryDto()
                        .setDate(p.getDate())
                        .setTempMax(p.getTempMax())
                        .setTempMin(p.getTempMin())
                        .setPrecipitationSum(p.getPrecipitationSum())
                        .setHumidityAvg(p.getHumidityAvg())
                        .setWindSpeedAvg(p.getWindSpeedAvg())
                        .setWindDirection(p.getWindDirection())
                        .setSoilMoisture(p.getSoilMoisture())
                        .setPressure(p.getPressure())
                        .setCloudCover(p.getCloudCover())
                        .setSnowDepth(p.getSnowDepth()))
                .toList();
    }
}
