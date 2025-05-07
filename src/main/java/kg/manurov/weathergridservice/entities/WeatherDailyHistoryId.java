package kg.manurov.weathergridservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class WeatherDailyHistoryId implements Serializable {
    private static final long serialVersionUID = 2799902972488171253L;
    @Column(name = "location_id")
    private Long locationId;

    @Column(name = "date")
    private LocalDate date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        WeatherDailyHistoryId entity = (WeatherDailyHistoryId) o;
        return Objects.equals(this.date, entity.date) &&
               Objects.equals(this.locationId, entity.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, locationId);
    }
}