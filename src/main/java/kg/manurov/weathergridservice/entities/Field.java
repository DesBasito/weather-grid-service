package kg.manurov.weathergridservice.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;
import org.locationtech.jts.geom.Point;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "fields")
public class Field {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @Column(name = "name")
    String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weather_location_id")
    WeatherLocation weatherLocation;

    @Column(name = "area_ha", precision = 10, scale = 2)
    BigDecimal areaHa;

    @Column(name = "crop_type", length = 100)
    String cropType;

    @Column(name = "irrigation_type", length = 50)
    String irrigationType;

    @ColumnDefault("now()")
    @Column(name = "created_at")
    LocalDateTime createdAt;

    @ColumnDefault("now()")
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "geometry", columnDefinition = "geometry")
    Point geometry;
}