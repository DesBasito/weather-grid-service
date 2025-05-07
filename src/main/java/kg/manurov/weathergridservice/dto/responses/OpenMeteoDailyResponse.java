package kg.manurov.weathergridservice.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class OpenMeteoDailyResponse {
    private Daily daily;

    @Getter
    @Setter
    public static class Daily {
        @JsonProperty("time")
        private List<LocalDate> time;

        @JsonProperty("temperature_2m_max")
        private List<Double> temperature2mMax;

        @JsonProperty("temperature_2m_min")
        private List<Double> temperature2mMin;

        @JsonProperty("precipitation_sum")
        private List<Double> precipitationSum;
    }
}
