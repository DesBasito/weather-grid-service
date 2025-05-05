package kg.manurov.weathergridservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private Integer username;

    @OneToMany(mappedBy = "user")
    private Set<Field> fields = new LinkedHashSet<>();

}