package kg.manurov.weathergridservice.services.impl;

import kg.manurov.weathergridservice.dto.WeatherHistoryDto;
import kg.manurov.weathergridservice.repositories.WeatherDailyHistoryRepository;
import kg.manurov.weathergridservice.repositories.projections.WeatherAggregateProjection;
import kg.manurov.weathergridservice.services.interfaces.WeatherHistoryService;
import kg.manurov.weathergridservice.services.interfaces.WeatherLocationService;
import kg.manurov.weathergridservice.util.GeometryHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static java.lang.Double.NaN;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherHistoryServiceImpl implements WeatherHistoryService {
    private final WeatherDailyHistoryRepository dailyHistoryRepo;
    private final WeatherLocationService weatherLocationService;


    @Override
    public List<WeatherHistoryDto> getHistory(Double latitude, Double longitude, LocalDate startDate, LocalDate endDate, String aggregate) {
        Double lat = GeometryHelper.roundToCenter(latitude);
        Double lon = GeometryHelper.roundToCenter(longitude);
        Long locationId = weatherLocationService.getOrCreateLocation(lat, lon).getId();
        List<WeatherAggregateProjection> list = dailyHistoryRepo.findAggregatedHistory(locationId, startDate, endDate, aggregate);

        return list.stream()
                .map(p -> new WeatherHistoryDto()
                        .setDate(p.getDate())
                        .setTempMax(roundedNumber(p.getTempMax()))
                        .setTempMin(roundedNumber(p.getTempMin()))
                        .setPrecipitationSum(roundedNumber(p.getPrecipitationSum()))
                        .setHumidityAvg(roundedNumber(p.getHumidityAvg()))
                        .setWindSpeedAvg(roundedNumber(p.getWindSpeedAvg()))
                        .setWindDirection(p.getWindDirection())
                        .setSoilMoisture(roundedNumber(p.getSoilMoisture()))
                        .setPressure(roundedNumber(p.getPressure()))
                        .setCloudCover(roundedNumber(p.getCloudCover()))
                        .setSnowDepth(roundedNumber(p.getSnowDepth())))
                .toList();
    }

    private double roundedNumber(double number) {
        if (Double.isNaN(number) || Double.isInfinite(number)) return number;
        return BigDecimal.valueOf(number)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
