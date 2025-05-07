package kg.manurov.weathergridservice.controllers;

import kg.manurov.weathergridservice.dto.WeatherHistoryDto;
import kg.manurov.weathergridservice.services.interfaces.WeatherHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/weather")
@RequiredArgsConstructor
public class WeatherHistoryController {
    private final WeatherHistoryService weatherHistoryService;

    @GetMapping("/history")
    public ResponseEntity<List<WeatherHistoryDto>> getHistory(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start_date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end_date,
            @RequestParam(required = false) String aggregate
    ) {
        List<WeatherHistoryDto> result = weatherHistoryService.getHistory(latitude, longitude, start_date, end_date, aggregate);
        return ResponseEntity.ok(result);
    }
}
