package kg.manurov.weathergridservice.configs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

@Slf4j
public class WebClientErrorHandler {
    public static ExchangeFilterFunction errorHandler() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            if (response.statusCode().is4xxClientError() || response.statusCode().is5xxServerError()) {
                return response.bodyToMono(String.class)
                        .flatMap(body -> {
                            log.error("[WebClient] Ошибка {}: {}%n",
                                    response.statusCode(), body);
                            return Mono.error(
                                    new RuntimeException("Ошибка Open-Meteo: " + body)
                            );
                        });
            }
            return Mono.just(response);
        });
    }
}
