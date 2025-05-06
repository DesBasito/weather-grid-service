package kg.manurov.weathergridservice.dto;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ForecastDto {
    Long locationId;
    Map<String, Object> current_weather;
    Map<String, Object> hourly;
    Map<String, Object> daily;
}

