package kg.manurov.weathergridservice.services.interfaces;

import org.springframework.transaction.annotation.Transactional;

public interface WeatherLocationService {
    Long getOrCreateLocationId(Double fieldLat, Double fieldLon);

    Long getByLocationId(Long locationId);
}
