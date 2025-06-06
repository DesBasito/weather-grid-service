package kg.manurov.weathergridservice.controllers;

import kg.manurov.weathergridservice.dto.ForecastDto;
import kg.manurov.weathergridservice.dto.WeatherLocationDto;
import kg.manurov.weathergridservice.services.impl.WeatherForecastServiceImpl;
import kg.manurov.weathergridservice.services.interfaces.WeatherForecastService;
import kg.manurov.weathergridservice.services.interfaces.WeatherLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/weather/forecast")
@RequiredArgsConstructor
public class WeatherController {
    private final WeatherForecastService weatherForecastService;
    private final WeatherLocationService weatherLocationService;

    @GetMapping
    public Mono<ForecastDto> forecast(
            @RequestParam double latitude,
            @RequestParam double longitude
    ) {
        return weatherForecastService.getForecast(latitude, longitude);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeatherLocationDto> getForecastByLocationId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(weatherLocationService.getByLocationId(id)) ;
    }
}
