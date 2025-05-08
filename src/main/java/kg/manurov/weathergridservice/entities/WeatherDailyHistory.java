package kg.manurov.weathergridservice.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
@Table(name = "weather_daily_history")
public class WeatherDailyHistory {
    @EmbeddedId
    WeatherDailyHistoryId id;


    @Column(name = "location_id", insertable = false, updatable = false)
    Long locationId;

    @Column(name = "temp_max")
    Double tempMax;

    @Column(name = "temp_min")
    Double tempMin;

    @Column(name = "temp_avg")
    Double tempAvg;

    @Column(name = "precipitation_sum")
    Double precipitationSum;

    @Column(name = "humidity_avg")
    Double humidityAvg;

    @Column(name = "wind_speed_avg")
    Double windSpeedAvg;

    @Column(name = "wind_direction", length = 3)
    String windDirection;

    @Column(name = "soil_moisture")
    Double soilMoisture;

    @Column(name = "pressure")
    Double pressure;

    @Column(name = "cloud_cover")
    Double cloudCover;

    @Column(name = "snow_depth")
    Double snowDepth;

}