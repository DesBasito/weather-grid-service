package kg.manurov.weathergridservice.services.interfaces;

import kg.manurov.weathergridservice.dto.ForecastDto;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

public interface WeatherForecastService {
    Mono<ForecastDto> getForecast(Double fieldLat, Double fieldLon);
}
