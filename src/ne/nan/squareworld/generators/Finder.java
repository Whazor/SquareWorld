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

    int chunkSize = Region.chunkSize;

    public Material[][] getChunk(int x, int y) {
        SpatialIndex block = fl.getRegion(fl.round(x), fl.round(y));

        Envelope envelope = new Envelope(
                new Coordinate(x, y),
                new Coordinate(x + chunkSize, y + chunkSize));
        List<Settlement> list = (List<Settlement>) block.query(envelope);

//        Stream<Settlement> stream = fl.filter(list.stream(), envelope);

        Material[][] result = new Material[chunkSize][chunkSize];
        for (int i = 0; i < chunkSize; i++)
            for (int j = 0; j < chunkSize; j++) {
                Envelope env = new Envelope(new Coordinate(fl.mod(x) + i, fl.mod(y) + j));
                Stream<Settlement> filter = fl.filter(list.stream(), env);

                if(filter.count() > 0) {
                    result[i][j] = Material.GRASS;
                } else {
                    result[i][j] = Material.BEDROCK;
                }
            }
//        System.out.println("chunk");
        return result;

//        if(stream.count() > 0) {
//            return Material.GRASS;
//        } else {
//            return Material.BEDROCK;
//        }
    }
}
