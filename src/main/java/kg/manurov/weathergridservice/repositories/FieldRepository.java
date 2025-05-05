package kg.manurov.weathergridservice.repositories;

import kg.manurov.weathergridservice.entities.Field;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FieldRepository extends JpaRepository<Field, Long> {
}