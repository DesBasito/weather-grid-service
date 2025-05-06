package kg.manurov.weathergridservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class WeatherGridServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherGridServiceApplication.class, args);
	}

}
