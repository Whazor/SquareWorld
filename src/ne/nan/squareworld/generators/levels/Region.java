package ne.nan.squareworld.generators.levels;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.index.SpatialIndex;
import com.vividsolutions.jts.index.quadtree.Quadtree;
import ne.nan.squareworld.model.Settlement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by nanne on 24/02/16.
 */
public class Region {
    private long seed;

    public Region(long seed) {
        this.seed = seed;
    }
    int minCitySize = 300;
    int maxCitySize = 500;
    int maxCities = 10;
    int minCities = 3;
    int blockSize = maxCitySize * 10;

    public int getBlockSize() { return blockSize; }

    public SpatialIndex getRegion(int x, int y) {
        Coordinate coordinate = new Coordinate(x, y);
        Random random = new Random(seed + coordinate.hashCode());


        Quadtree qt = new Quadtree();

        for (int i = 0; i < minCities + random.nextInt(maxCities - minCities + 1); i++) {
            int city_x = Math.round(random.nextFloat() * blockSize);
            int city_y = Math.round(random.nextFloat() * blockSize);

            int difference = maxCitySize - minCitySize;

            int width = minCitySize + Math.round(random.nextFloat() * difference);
            int height = minCitySize + Math.round(random.nextFloat() * difference);

            Settlement settlement = new Settlement(city_x, city_y, width, height);
            qt.insert(settlement.getEnvelope(), settlement);
        }

        return qt;
    }

}


