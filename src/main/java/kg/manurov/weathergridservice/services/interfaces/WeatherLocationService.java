package kg.manurov.weathergridservice.services.interfaces;

import kg.manurov.weathergridservice.entities.WeatherLocation;
import org.springframework.transaction.annotation.Transactional;

public interface WeatherLocationService {
    WeatherLocation getOrCreateLocation(Double fieldLat, Double fieldLon);

    Long getByLocationId(Long locationId);
}
