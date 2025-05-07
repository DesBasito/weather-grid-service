package kg.manurov.weathergridservice.controllers;

import kg.manurov.weathergridservice.dto.FieldDto;
import kg.manurov.weathergridservice.dto.ForecastDto;
import kg.manurov.weathergridservice.services.interfaces.FieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/field")
@RequiredArgsConstructor
public class FieldController {
    private final FieldService fieldService;

    @PostMapping
    public ResponseEntity<FieldDto> create(@RequestBody @Validated FieldDto fieldDto) {
        return ResponseEntity.ok(fieldService.create(fieldDto));
    }
}
