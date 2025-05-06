package kg.manurov.weathergridservice.repositories;

import kg.manurov.weathergridservice.entities.WeatherDailyHistory;
import kg.manurov.weathergridservice.entities.WeatherDailyHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherDailyHistoryRepository extends JpaRepository<WeatherDailyHistory, WeatherDailyHistoryId> {
}