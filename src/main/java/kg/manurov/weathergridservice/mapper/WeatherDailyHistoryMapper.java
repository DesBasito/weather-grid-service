package kg.manurov.weathergridservice.mapper;

import kg.manurov.weathergridservice.dto.WeatherHistoryDto;
import kg.manurov.weathergridservice.entities.WeatherDailyHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WeatherDailyHistoryMapper {
    WeatherHistoryDto toDto(WeatherDailyHistory weatherDailyHistory);
    WeatherDailyHistory toEntity(WeatherHistoryDto weatherHistoryDto);
}
