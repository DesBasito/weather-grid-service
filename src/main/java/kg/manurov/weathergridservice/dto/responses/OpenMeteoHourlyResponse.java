package kg.manurov.weathergridservice.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OpenMeteoHourlyResponse {
    private Hourly hourly;

    @Getter @Setter
    public static class Hourly {
        @JsonProperty("time")
        private List<LocalDateTime> time;

        @JsonProperty("temperature_2m")
        private List<Double> temperature2m;

        @JsonProperty("precipitation")
        private List<Double> precipitation;

        @JsonProperty("relativehumidity_2m")
        private List<Double> relativeHumidity2m;

        @JsonProperty("windspeed_10m")
        private List<Double> windspeed10m;

        @JsonProperty("winddirection_10m")
        private List<Double> winddirection10m;

        @JsonProperty("cloudcover")
        private List<Double> cloudcover;

        @JsonProperty("soil_moisture_0_to_7cm")
        private List<Double> soilMoisture;

        @JsonProperty("pressure_msl")
        private List<Double> pressureMsl;

        @JsonProperty("snow_depth")
        private List<Double> snowDepth;
    }
}
