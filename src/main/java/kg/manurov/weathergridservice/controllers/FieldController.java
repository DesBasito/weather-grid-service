package kg.manurov.weathergridservice.controllers;

import kg.manurov.weathergridservice.dto.FieldDto;
import kg.manurov.weathergridservice.services.interfaces.FieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class FieldController {
    private final FieldService fieldService;

    @PostMapping("/field")
    public ResponseEntity<FieldDto> create(@RequestBody @Validated FieldDto fieldDto) {
        return ResponseEntity.ok(fieldService.create(fieldDto));
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/fields")
    public ResponseEntity<List<FieldDto>> getAllFields() {
        return ResponseEntity.ok(fieldService.getAllFields());
    }
}
