package kg.manurov.weathergridservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.locationtech.jts.geom.Point;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "fields")
public class Field {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weather_location_id")
    private WeatherLocation weatherLocation;

    @Column(name = "area_ha", precision = 10, scale = 2)
    private BigDecimal areaHa;

    @Column(name = "crop_type", length = 100)
    private String cropType;

    @Column(name = "irrigation_type", length = 50)
    private String irrigationType;

    @ColumnDefault("now()")
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @ColumnDefault("now()")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "geometry", columnDefinition = "geometry")
    private Point geometry;
}