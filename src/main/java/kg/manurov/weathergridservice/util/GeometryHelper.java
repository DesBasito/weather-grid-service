package kg.manurov.weathergridservice.util;

import lombok.experimental.UtilityClass;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

@UtilityClass
public class GeometryHelper {
    private static final GeometryFactory geometryFactory = new GeometryFactory();
    public static final int SRID_WGS84 = 4326;
    private static final double GRID_STEP = 0.05;
    private static final String[] CARDINALS =
            {"N","NE","E","SE","S","SW","W","NW"};

    public String toCardinal(double degrees) {
        int idx = (int)((degrees + 22.5) / 45.0) % 8;
        return CARDINALS[idx];
    }

    public static Point createPoint(double latitude, double longitude) {
        Coordinate coord = new Coordinate(longitude, latitude); // сначала X (lon), потом Y (lat)
        Point point = geometryFactory.createPoint(coord);
        point.setSRID(SRID_WGS84);
        return point;
    }

    public double roundToCenter(double coord) {
        double idx = Math.floor(coord / GRID_STEP);
        return (idx + 0.5) * GRID_STEP;
    }
}

