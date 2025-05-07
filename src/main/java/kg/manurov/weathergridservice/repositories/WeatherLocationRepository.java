package kg.manurov.weathergridservice.repositories;

import kg.manurov.weathergridservice.entities.WeatherLocation;
import kg.manurov.weathergridservice.repositories.projections.LocationIdCoord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface WeatherLocationRepository extends JpaRepository<WeatherLocation, Long> {
    Optional<WeatherLocation> findByLatitudeAndLongitude(double lat, double lon);

    @Query("""
        SELECT wl.id  AS id,
               wl.latitude AS latitude,
               wl.longitude AS longitude
        FROM WeatherLocation wl
        JOIN wl.fields f
        GROUP BY wl.id, wl.latitude, wl.longitude
        """)
    List<LocationIdCoord> findDistinctLocationsWithFields();
}