package kg.manurov.weathergridservice.services.impl;

import kg.manurov.weathergridservice.dto.WeatherLocationDto;
import kg.manurov.weathergridservice.entities.WeatherLocation;
import kg.manurov.weathergridservice.mapper.WeatherLocationMapper;
import kg.manurov.weathergridservice.repositories.WeatherLocationRepository;
import kg.manurov.weathergridservice.services.interfaces.WeatherLocationService;
import kg.manurov.weathergridservice.util.GeometryHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherLocationServiceImpl implements WeatherLocationService {
    private final WeatherLocationRepository locationRepo;
    private final WeatherLocationMapper mapper;

    @Transactional
    @Override
    public WeatherLocation getOrCreateLocation(Double fieldLat, Double fieldLon) {
        log.info("Getting or creating location for coordinates lat={}, lon={}", fieldLat, fieldLon);
        return locationRepo.findByLatitudeAndLongitude(fieldLat, fieldLon)
                .orElseGet(() -> {
                    try {
                        WeatherLocation loc = new WeatherLocation();
                        loc.setLatitude(fieldLat);
                        loc.setLongitude(fieldLon);
                        loc.setCreatedAt(LocalDateTime.now());
                        return locationRepo.save(loc);
                    } catch (Exception e) {
                        log.error("Failed to create location for coordinates lat={}, lon={}: {}", fieldLat, fieldLon, e.getMessage());
                        throw e;
                    }
                });
    }

    @Override
    @Cacheable(value = "forecast",
            key = "'Location_Id_' + #locationId",
            unless = "#result == null")
    public WeatherLocationDto getByLocationId(Long locationId) {
        log.info("Getting location by id={}", locationId);
        return locationRepo.findById(locationId)
                .map(mapper::toDto)
                .orElseThrow(NoSuchElementException::new);
    }
}
