package ne.nan.squareworld.generators;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.SpatialIndex;
import ne.nan.squareworld.generators.levels.Region;
import ne.nan.squareworld.model.Settlement;
import org.bukkit.Material;

import java.util.List;
import java.util.Optional;
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

        // adept per region
        int x2 = fl.mod(x);
        int y2 = fl.mod(y);

        Envelope envelope = new Envelope(
                new Coordinate(x2, y2),
                new Coordinate(x2 + chunkSize - 1, y2 + chunkSize - 1));
        List<Settlement> list = (List<Settlement>) block.query(envelope);

//        Stream<Settlement> stream = fl.filter(list.stream(), envelope);

        Stream<Settlement> filter = fl.filter(list.stream(), envelope);

//
//
        Material[][] result = new Material[chunkSize][chunkSize];
        for (int i = 0; i < chunkSize; i++)
            for (int j = 0; j < chunkSize; j++) {
                result[i][j] = Material.GRASS;
            }

        Optional<Settlement> first = filter.findFirst();
        if (first.isPresent()) {
            Settlement settlement = first.get();
            short[][] generate = settlement.generate();
            int x3 = x2 - settlement.x;
            int y3 = y2 - settlement.y;
            for (int i = 0; i < chunkSize; i++) {
                for (int j = 0; j < chunkSize; j++) {
                    try {
                        result[i][j] = Material.getMaterial((int) generate[x3 + i][y3 + j]);
                    }catch(ArrayIndexOutOfBoundsException ignored) {
                        // huil
//                        System.out.println("Faal");
                    }
                }
            }
        }
////        System.out.println("chunk");
        return result;

//        if(stream.count() > 0) {
//            return Material.GRASS;
//        } else {
//            return Material.BEDROCK;
//        }
    }
}
