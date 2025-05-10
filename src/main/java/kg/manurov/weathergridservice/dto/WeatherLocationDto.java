package kg.manurov.weathergridservice.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for {@link kg.manurov.weathergridservice.entities.WeatherLocation}
 */
public record WeatherLocationDto(Double latitude,
                                 Double longitude,
                                 String createdAt,
                                 String name,
                                 String description,
                                 Set<FieldDto> fields) implements Serializable {
}