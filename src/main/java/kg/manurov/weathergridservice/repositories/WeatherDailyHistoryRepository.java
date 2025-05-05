package kg.manurov.weathergridservice.repositories;

import kg.manurov.weathergridservice.entities.WeatherDailyHistory;
import kg.manurov.weathergridservice.entities.WeatherDailyHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherDailyHistoryRepository extends JpaRepository<WeatherDailyHistory, WeatherDailyHistoryId> {
}