package kg.manurov.weathergridservice.repositories;

import io.lettuce.core.dynamic.annotation.Param;
import kg.manurov.weathergridservice.entities.Field;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {
    @Query(value = """
SELECT count(*) > 0
FROM fields\s
WHERE ST_Equals(geometry, ST_SetSRID(ST_MakePoint(:lon, :lat), 4326))
""",nativeQuery = true)
    boolean existsByGeometry(double lat, double lon);

    @Query(value = """
  SELECT ST_Within(
                 ST_SetSRID(ST_MakePoint(:lon, :lat), 4326),
                 (SELECT geom FROM country_boundaries WHERE name = 'Kyrgyzstan')
         )
""", nativeQuery = true)
    boolean isPointInKyrgyzstan(@Param("lon") double lon,
                                @Param("lat") double lat);
}