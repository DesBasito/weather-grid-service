package kg.manurov.weathergridservice.mapper;

import kg.manurov.weathergridservice.dto.WeatherLocationDto;
import kg.manurov.weathergridservice.entities.WeatherLocation;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {FieldMapper.class})
public interface WeatherLocationMapper {
    WeatherLocation toEntity(WeatherLocationDto weatherLocationDto);

    @AfterMapping
    default void linkFields(@MappingTarget WeatherLocation weatherLocation) {
        weatherLocation.getFields().forEach(field -> field.setWeatherLocation(weatherLocation));
    }

    WeatherLocationDto toDto(WeatherLocation weatherLocation);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    WeatherLocation partialUpdate(WeatherLocationDto weatherLocationDto, @MappingTarget WeatherLocation weatherLocation);
}