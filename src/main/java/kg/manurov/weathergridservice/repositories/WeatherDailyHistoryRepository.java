package kg.manurov.weathergridservice.repositories;

import kg.manurov.weathergridservice.entities.WeatherDailyHistory;
import kg.manurov.weathergridservice.entities.WeatherDailyHistoryId;
import kg.manurov.weathergridservice.repositories.projections.WeatherAggregateProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WeatherDailyHistoryRepository extends JpaRepository<WeatherDailyHistory, WeatherDailyHistoryId> {

    @Query(value = """
            SELECT date_trunc(:dateExpr, date) AS date,
            AVG(temp_max) AS tempMax,
            AVG(temp_min) AS tempMin,
            SUM(precipitation_sum) AS precipitationSum,
            AVG(humidity_avg) AS humidityAvg,
            AVG(wind_speed_avg) AS windSpeedAvg,
            MIN(wind_direction) AS windDirection,
            AVG(soil_moisture) AS soilMoisture,
            AVG(pressure) AS pressure,
            AVG(cloud_cover) AS cloudCover,
            AVG(snow_depth) AS snowDepth
            FROM weather_daily_history
            WHERE location_id = :locationId AND date BETWEEN :startDate AND :endDate
            GROUP BY date
            ORDER BY date
            """, nativeQuery = true)
    List<WeatherAggregateProjection> findAggregatedHistory(Long locationId, LocalDate startDate, LocalDate endDate, String dateExpr);
}