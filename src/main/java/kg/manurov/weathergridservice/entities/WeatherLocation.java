package kg.manurov.weathergridservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "weather_locations")
public class WeatherLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @ColumnDefault("now()")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "name")
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @OneToMany(mappedBy = "weatherLocation")
    private Set<Field> fields = new LinkedHashSet<>();

    @OneToMany(mappedBy = "location")
    private Set<WeatherDailyHistory> weatherDailyHistories = new LinkedHashSet<>();

}