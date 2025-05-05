package kg.manurov.weathergridservice.repositories;

import kg.manurov.weathergridservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}