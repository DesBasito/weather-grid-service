package kg.manurov.weathergridservice.services.impl;

import kg.manurov.weathergridservice.dto.ForecastDto;
import kg.manurov.weathergridservice.entities.WeatherLocation;
import kg.manurov.weathergridservice.services.interfaces.WeatherForecastService;
import kg.manurov.weathergridservice.services.interfaces.WeatherLocationService;
import kg.manurov.weathergridservice.util.GeometryHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
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
    private final OpenMeteoCounter counter;
    private final ReactiveRedisTemplate<String, ForecastDto> redisTemplate;
    private final CacheManager cacheManager;

    @Override
    public Mono<ForecastDto> getForecast(Double fieldLat, Double fieldLon) {
        double cellLat = GeometryHelper.roundToCenter(fieldLat);
        double cellLon = GeometryHelper.roundToCenter(fieldLon);
        String cacheKey = "forecast:lat_" + cellLat + ":lon_" + cellLon;

        log.info("Fetching forecast for location at lat={}, lon={} with cache key={}", fieldLat, fieldLon, cacheKey);
        return redisTemplate.opsForValue()
                .get(cacheKey)
                .switchIfEmpty(fetchAndCacheForecast(cellLat, cellLon, cacheKey));
    }

    private Mono<ForecastDto> fetchAndCacheForecast(double cellLat, double cellLon, String cacheKey) {
        log.info("Fetching and caching new forecast data for cell coordinates lat={}, lon={}, cache key={}", cellLat, cellLon, cacheKey);
        WeatherLocation weatherLocation = locationService.getOrCreateLocation(cellLat, cellLon);
        if (!counter.tryConsume()) {
            log.info("Превышен лимит 10 000 запросов к Open-Meteo за сутки");
            throw new IllegalStateException("Превышен лимит 10 000 запросов к Open-Meteo за сутки");
        }else{
            log.info("Remaining Open-Meteo quota: {}/{}", counter.getRemaining(), 10_000);
        }
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
                    dto.setLocationId(weatherLocation.getId());
                    return dto;
                })
                .flatMap(dto -> redisTemplate.opsForValue()
                        .set(cacheKey, dto, Duration.ofHours(2))
                        .thenReturn(dto));
    }


}
