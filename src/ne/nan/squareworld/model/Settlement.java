package ne.nan.squareworld.model;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
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

    private Envelope envelope;

    public Settlement(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        Coordinate coordinateMin = new Coordinate(x, y);
        Coordinate coordinateMax = new Coordinate(x + width, y + height);
        this.envelope = new Envelope(coordinateMin, coordinateMax);
    }


    public Envelope getEnvelope() {
        return envelope;
    }
}
