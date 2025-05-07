package kg.manurov.weathergridservice.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "weather_locations")
public class WeatherLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "latitude")
    Double latitude;

    @Column(name = "longitude")
    Double longitude;

    @ColumnDefault("now()")
    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "name")
    String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    String description;

    @OneToMany(mappedBy = "weatherLocation")
    Set<Field> fields = new LinkedHashSet<>();

}