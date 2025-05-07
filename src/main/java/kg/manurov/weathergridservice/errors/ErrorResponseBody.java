package kg.manurov.weathergridservice.errors;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter@Setter@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseBody {
    private String title;
    private Map<String, List<String>> response;
}
