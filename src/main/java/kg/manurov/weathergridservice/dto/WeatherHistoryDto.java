package kg.manurov.weathergridservice.dto;

import jakarta.persistence.Access;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WeatherHistoryDto {
    LocalDate date;
    Double tempMax;
    Double tempMin;
    Double precipitationSum;
    Double humidityAvg;
    Double windSpeedAvg;
    String windDirection;
    Double soilMoisture;
    Double pressure;
    Double cloudCover;
    Double snowDepth;
}
