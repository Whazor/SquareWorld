package ne.nan.squareworld.generators;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.SpatialIndex;
import ne.nan.squareworld.generators.levels.Region;
import ne.nan.squareworld.model.Settlement;
import org.bukkit.Material;

import java.util.List;
import java.util.stream.Stream;

/**
 * Created by nanne on 25/02/16.
 */
public class Finder {

    private Region fl;

    public Finder(Region fl) {
        this.fl = fl;
    }

    public Material getBlock(int x, int y) {
        int rx = fl.round(x);
        int ry = fl.round(y);

        SpatialIndex block = fl.getRegion(fl.round(x), fl.round(y));

        Envelope envelope = new Envelope(new Coordinate(x - rx, y - ry));
        List list = block.query(envelope);

//        Stream<Settlement> stream = list.stream().filter(p -> ((Settlement)p).getEnvelope().intersects(envelope));
        if(list.size() > 0) {
            return Material.GRASS;
        } else {
            return Material.BEDROCK;
        }
    }
}
