package kg.manurov.weathergridservice.services.impl;

import kg.manurov.weathergridservice.dto.ForecastDto;
import kg.manurov.weathergridservice.entities.WeatherLocation;
import kg.manurov.weathergridservice.services.components.OpenMeteoCounter;
import kg.manurov.weathergridservice.services.interfaces.WeatherForecastService;
import kg.manurov.weathergridservice.services.interfaces.WeatherLocationService;
import kg.manurov.weathergridservice.util.GeometryHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
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
        Long weatherLocationId = locationService.getOrCreateLocation(cellLat, cellLon).getId();

        checkConsume();

        return openMeteoWebClient.get()
                .uri(uriBuilder -> buildWeatherForecastUri(cellLat, cellLon, uriBuilder))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ForecastDto.class)
                .map(dto -> {
                    dto.setLocationId(weatherLocationId);
                    return dto;
                })
                .flatMap(dto -> redisTemplate.opsForValue()
                        .set(cacheKey, dto, Duration.ofHours(2))
                        .thenReturn(dto));
    }

    private URI buildWeatherForecastUri(double cellLat, double cellLon, UriBuilder uriBuilder) {
        return uriBuilder
                .path("/v1/forecast")
                .queryParam("latitude", cellLat)
                .queryParam("longitude", cellLon)
                .queryParam("current_weather", "true")
                .queryParam("hourly", "temperature_2m")
                .queryParam("daily", "temperature_2m_max,temperature_2m_min,precipitation_sum")
                .queryParam("forecast_days", "7")
                .queryParam("timezone", "auto")
                .build();
    }

    private void checkConsume() {
        if (!counter.tryConsume()) {
            log.info("Превышен лимит 10 000 запросов к Open-Meteo за сутки");
            throw new IllegalStateException("Превышен лимит 10 000 запросов к Open-Meteo за сутки");
        } else {
            log.info("Remaining Open-Meteo quota: {}/{}", counter.getRemaining(), 10_000);
        }
    }


}
