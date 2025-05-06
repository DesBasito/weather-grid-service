package kg.manurov.weathergridservice.services.impl;

import kg.manurov.weathergridservice.entities.WeatherLocation;
import kg.manurov.weathergridservice.repositories.WeatherLocationRepository;
import kg.manurov.weathergridservice.services.interfaces.WeatherLocationService;
import kg.manurov.weathergridservice.util.GeometryHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class WeatherLocationServiceImpl implements WeatherLocationService {
    private final WeatherLocationRepository locationRepo;

    @Transactional
    @Override
    public Long getOrCreateLocationId(Double fieldLat, Double fieldLon) {
        double cellLat = GeometryHelper.roundToCenter(fieldLat);
        double cellLon = GeometryHelper.roundToCenter(fieldLon);

        return locationRepo.findByLatitudeAndLongitude(cellLat, cellLon)
                .map(WeatherLocation::getId)
                .orElseGet(() -> {
                    WeatherLocation loc = new WeatherLocation();
                    loc.setLatitude(cellLat);
                    loc.setLongitude(cellLon);
                    loc.setCreatedAt(LocalDateTime.now());
                    return locationRepo.save(loc).getId();
                });
    }

    @Override
    @Cacheable(value = "forecast",
            key = "'Location_Id_' + #locationId",
            unless = "#result == null")
    public Long getByLocationId(Long locationId) {
        return locationRepo.findById(locationId)
                .map(WeatherLocation::getId)
                .orElseThrow(NoSuchElementException::new);
    }
}
