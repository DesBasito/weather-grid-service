package kg.manurov.weathergridservice.controllers;

import kg.manurov.weathergridservice.services.interfaces.DailyHistoryUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("api/backfill")
@RequiredArgsConstructor
public class WeatherLocationController {
    private final DailyHistoryUpdateService historyUpdateService;

    @PostMapping
    public ResponseEntity<String> backfill(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        historyUpdateService.updateRange(start, end);
        return ResponseEntity.ok("Backfill запущен");
    }
}
