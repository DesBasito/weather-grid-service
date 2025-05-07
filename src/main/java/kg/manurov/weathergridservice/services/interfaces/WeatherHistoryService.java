package kg.manurov.weathergridservice.services.interfaces;

import kg.manurov.weathergridservice.dto.WeatherHistoryDto;

import java.time.LocalDate;
import java.util.List;

public interface WeatherHistoryService {
    List<WeatherHistoryDto> getHistory(Double latitude, Double longitude, LocalDate startDate, LocalDate endDate, String aggregate);

}
