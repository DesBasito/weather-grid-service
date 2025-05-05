package kg.manurov.weathergridservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "weather_daily_history")
public class WeatherDailyHistory {
    @EmbeddedId
    private WeatherDailyHistoryId id;

    @MapsId("locationId")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "location_id")
    private WeatherLocation location;

    @Column(name = "temp_max")
    private Double tempMax;

    @Column(name = "temp_min")
    private Double tempMin;

    @Column(name = "temp_avg")
    private Double tempAvg;

    @Column(name = "precipitation_sum")
    private Double precipitationSum;

    @Column(name = "humidity_avg")
    private Double humidityAvg;

    @Column(name = "wind_speed_avg")
    private Double windSpeedAvg;

    @Column(name = "wind_direction", length = 3)
    private String windDirection;

    @Column(name = "soil_moisture")
    private Double soilMoisture;

    @Column(name = "pressure")
    private Double pressure;

    @Column(name = "cloud_cover")
    private Double cloudCover;

    @Column(name = "snow_depth")
    private Double snowDepth;

}