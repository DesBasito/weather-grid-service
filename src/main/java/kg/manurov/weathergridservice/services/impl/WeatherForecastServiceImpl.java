package kg.manurov.weathergridservice.services.impl;

import kg.manurov.weathergridservice.dto.ForecastDto;
import kg.manurov.weathergridservice.services.interfaces.WeatherForecastService;
import kg.manurov.weathergridservice.services.interfaces.WeatherLocationService;
import kg.manurov.weathergridservice.util.GeometryHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherForecastServiceImpl implements WeatherForecastService {
    private final WebClient openMeteoWebClient;
    private final WeatherLocationService locationService;
    private final ReactiveRedisTemplate<String, ForecastDto> redisTemplate;

    @Override
    public Mono<ForecastDto> getForecast(Double fieldLat, Double fieldLon) {
        double cellLat = GeometryHelper.roundToCenter(fieldLat);
        double cellLon = GeometryHelper.roundToCenter(fieldLon);
        String cacheKey = "forecast:lat_" + cellLat + ":lon_" + cellLon;

        return redisTemplate.opsForValue()
                .get(cacheKey)
                .switchIfEmpty(fetchAndCacheForecast(fieldLat, fieldLon, cellLat, cellLon, cacheKey));
    }

    private Mono<ForecastDto> fetchAndCacheForecast(Double fieldLat, Double fieldLon,
                                                    double cellLat, double cellLon,
                                                    String cacheKey) {
        Long locationId = locationService.getOrCreateLocationId(fieldLat, fieldLon);

        return openMeteoWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/forecast")
                        .queryParam("latitude", cellLat)
                        .queryParam("longitude", cellLon)
                        .queryParam("current_weather", "true")
                        .queryParam("hourly", "temperature_2m")
                        .queryParam("daily", "temperature_2m_max,temperature_2m_min,precipitation_sum")
                        .queryParam("forecast_days", "7")
                        .queryParam("timezone", "auto")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ForecastDto.class)
                .map(dto -> {
                    dto.setLocationId(locationId);
                    return dto;
                })
                .flatMap(dto -> redisTemplate.opsForValue()
                        .set(cacheKey, dto, Duration.ofHours(2))
                        .thenReturn(dto));
    }


}
