package ne.nan.squareworld.model;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Created by nanne on 24/02/16.
 */
public class Settlement {
    int x;
    int y;
    int width;
    int height;
    Polygon pol;

    public Settlement(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coordinate1 = new Coordinate(x, y);
        Coordinate coordinate2 = new Coordinate(x + width, y);
        Coordinate coordinate3 = new Coordinate(x, y + height);
        Coordinate coordinate4 = new Coordinate(x + width, y + height);

        this.pol = geometryFactory.createPolygon(new Coordinate[]{
                coordinate1,
                coordinate2,
                coordinate3,
                coordinate4
        });
    }
}
