package kg.manurov.weathergridservice.services.interfaces;

import kg.manurov.weathergridservice.dto.FieldDto;

import java.util.List;

public interface FieldService {
    FieldDto create(FieldDto fieldDto);

    List<FieldDto> getAllFields();
}
