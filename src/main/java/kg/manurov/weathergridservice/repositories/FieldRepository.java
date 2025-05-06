package kg.manurov.weathergridservice.repositories;

import kg.manurov.weathergridservice.entities.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {
}