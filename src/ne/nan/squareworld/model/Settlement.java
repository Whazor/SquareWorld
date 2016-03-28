package ne.nan.squareworld.model;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import org.bukkit.material.MaterialData;

/**
 * Created by nanne on 24/02/16.
 */
public abstract class Settlement {
    public final int zaad;
    public int x;
    public final int y;
    public final int width;
    public final int height;

    private Envelope envelope;

    public Settlement(int zaad, int x, int y, int width, int height) {
        this.zaad = zaad;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        Coordinate coordinateMin = new Coordinate(x, y);
        Coordinate coordinateMax = new Coordinate(x + width, y + height);
        this.envelope = new Envelope(coordinateMin, coordinateMax);
    }

    public abstract short[][] generate();

    public abstract MaterialData[][][] getChunk(int x, int y);

    public Envelope getEnvelope() {
        return envelope;
    }
}
