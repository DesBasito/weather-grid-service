package kg.manurov.weathergridservice.services.impl;

import kg.manurov.weathergridservice.dto.FieldDto;
import kg.manurov.weathergridservice.entities.Field;
import kg.manurov.weathergridservice.entities.User;
import kg.manurov.weathergridservice.entities.WeatherLocation;
import kg.manurov.weathergridservice.mapper.FieldMapper;
import kg.manurov.weathergridservice.repositories.FieldRepository;
import kg.manurov.weathergridservice.repositories.UserRepository;
import kg.manurov.weathergridservice.services.interfaces.FieldService;
import kg.manurov.weathergridservice.services.interfaces.WeatherLocationService;
import kg.manurov.weathergridservice.util.GeometryHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class FieldServiceImpl implements FieldService {
    private final FieldRepository fieldRepository;
    private final WeatherLocationService weatherLocationService;
    private final UserRepository userRepository;
    private final FieldMapper fieldMapper;

    @Override
    public FieldDto create(FieldDto fieldDto) {
        boolean b = isFieldExist(fieldDto);
        if (b) {
            throw new IllegalArgumentException("Field with coordinates lat=" + fieldDto.getLatitude() + ", lon=" + fieldDto.getLongitude() + " already exists");
        }
        Field field = getMappedField(fieldDto);
        fieldRepository.save(field);
        log.info("Creating field {}", fieldDto);
        return fieldMapper.toDto(field);
    }

    private Field getMappedField(FieldDto fieldDto) {
        WeatherLocation weatherLocation = weatherLocationService
                .getOrCreateLocation(fieldDto.getLatitude(), fieldDto.getLongitude());
        User user = userRepository.findById(fieldDto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User with id=" + fieldDto.getUserId() + " not found"));

        Field field = fieldMapper.toEntity(fieldDto);
        field.setWeatherLocation(weatherLocation);
        field.setUser(user);
        field.setCreatedAt(LocalDateTime.now());
        field.setUpdatedAt(LocalDateTime.now());
        return field;
    }

    private boolean isFieldExist(FieldDto fieldDto) {
        double lat = GeometryHelper.roundToCenter(fieldDto.getLatitude());
        double lon = GeometryHelper.roundToCenter(fieldDto.getLongitude());
        return fieldRepository.existsByGeometry(lon, lat);
    }
}
