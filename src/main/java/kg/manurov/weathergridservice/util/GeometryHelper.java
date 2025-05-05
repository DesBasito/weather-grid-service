package kg.manurov.weathergridservice.util;

import lombok.experimental.UtilityClass;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

@UtilityClass
public class GeometryHelper {
    private static final GeometryFactory geometryFactory = new GeometryFactory();
    public static final int SRID_WGS84 = 4326;

    public static Point createPoint(double latitude, double longitude) {
        Coordinate coord = new Coordinate(longitude, latitude); // сначала X (lon), потом Y (lat)
        Point point = geometryFactory.createPoint(coord);
        point.setSRID(SRID_WGS84);
        return point;
    }
}

