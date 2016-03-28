package ne.nan.squareworld.generators;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.SpatialIndex;
import ne.nan.squareworld.generators.levels.City;
import ne.nan.squareworld.generators.levels.Region;
import ne.nan.squareworld.model.Settlement;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

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

    public MaterialData[][][] getChunk(int x, int y) {
        SpatialIndex block = fl.getRegion(fl.round(x), fl.round(y));

        // adept per region
        int x2 = fl.mod(x);
        int y2 = fl.mod(y);

        Envelope envelope = new Envelope(
                new Coordinate(x2, y2),
                new Coordinate(x2 + chunkSize - 1, y2 + chunkSize - 1));
        List<Settlement> list = (List<Settlement>) block.query(envelope);

        Stream<Settlement> filter = fl.filter(list.stream(), envelope);

        MaterialData[][][] result = new MaterialData[chunkSize][chunkSize][100];
        for (int i = 0; i < chunkSize; i++)
            for (int j = 0; j < chunkSize; j++) {
                result[i][j][0] = new MaterialData(Material.BEDROCK);
                result[i][j][1] = new MaterialData(Material.GRASS);
            }

        Optional<Settlement> first = filter.findFirst();
        if (first.isPresent()) {
            Settlement settlement = first.get();
            short[][] generate = settlement.generate();
            int x3 = x2 - settlement.x;
            int y3 = y2 - settlement.y;
            for (int i = 0; i < chunkSize; i++) {
                for (int j = 0; j < chunkSize; j++) {
                    if(x3 + i < settlement.width && y3 + j < settlement.height) {
//                        System.out.println("j + i = " + j +"-"+ i);
                        result[i][j][1] = new MaterialData((int) generate[x3 + i][y3 + j]);
                    }
                }
            }

            MaterialData[][][] chunk = settlement.getChunk(x3, y3);
            for (int i = 0; i < chunkSize; i++) {
                for (int j = 0; j < chunkSize; j++) {
                    for (int k = 0; k < chunk[i][j].length; k++) {
                        if(chunk[i][j][k] != null) {
                            result[i][j][k+1] = chunk[i][j][k];
                        }
                    }
                }
            }
        }
        return result;
    }
}
