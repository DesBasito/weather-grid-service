package kg.manurov.weathergridservice.repositories;

import kg.manurov.weathergridservice.entities.WeatherLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface WeatherLocationRepository extends JpaRepository<WeatherLocation, Long> {
    Optional<WeatherLocation> findByLatitudeAndLongitude(double lat, double lon);
}