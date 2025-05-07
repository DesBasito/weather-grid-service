package kg.manurov.weathergridservice.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenMeteoCombinedResponse {
    private OpenMeteoDailyResponse.Daily daily;
    private OpenMeteoHourlyResponse.Hourly hourly;
}