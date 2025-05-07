package kg.manurov.weathergridservice.repositories.projections;

import java.time.LocalDate;

public interface WeatherAggregateProjection {
    LocalDate getDate();
    Double getTempMax();
    Double getTempMin();
    Double getPrecipitationSum();
    Double getHumidityAvg();
    Double getWindSpeedAvg();
    String getWindDirection();
    Double getSoilMoisture();
    Double getPressure();
    Double getCloudCover();
    Double getSnowDepth();
}
