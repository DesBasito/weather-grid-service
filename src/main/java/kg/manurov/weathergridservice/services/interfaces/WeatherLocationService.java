package kg.manurov.weathergridservice.services.interfaces;

import kg.manurov.weathergridservice.dto.WeatherLocationDto;
import kg.manurov.weathergridservice.entities.WeatherLocation;
import org.springframework.transaction.annotation.Transactional;

public interface WeatherLocationService {
    WeatherLocation getOrCreateLocation(Double fieldLat, Double fieldLon);

    WeatherLocationDto getByLocationId(Long locationId);
}
