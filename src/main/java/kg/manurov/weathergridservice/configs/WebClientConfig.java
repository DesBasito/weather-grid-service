package kg.manurov.weathergridservice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient openMeteoWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://api.open-meteo.com")
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .filter(WebClientErrorHandler.errorHandler())
                .build();
    }
}

