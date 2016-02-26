package ne.nan.squareworld.generators.levels;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.index.SpatialIndex;
import com.vividsolutions.jts.index.quadtree.Quadtree;
import com.vividsolutions.jts.index.strtree.STRtree;
import ne.nan.squareworld.model.Settlement;

import java.util.LinkedHashMap;
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
    int maxCities = 100;
    int minCities = 30;
    int regionSize = maxCitySize * 10;

    LinkedHashMap<Coordinate, SpatialIndex> map = new LinkedHashMap<>();

    public int round(int i) { return (int) Math.floor(i / getRegionSize()); }
    public int getRegionSize() { return regionSize; }


    public SpatialIndex getRegion(int x, int y) {
        Coordinate coordinate = new Coordinate(x, y);
        if(map.containsKey(coordinate)) {
            return map.get(coordinate);
        } else {
            if(map.size() > 5) {
                Coordinate o = (Coordinate) map.keySet().toArray()[0];
                map.remove(o);
            }

            SpatialIndex generate = generate(coordinate);
            map.put(coordinate, generate);
            return generate;
        }
    }

    public SpatialIndex generate(Coordinate coordinate) {
        Random random = new Random(seed + coordinate.hashCode());

        SpatialIndex si = new STRtree();


        for (int i = 0; i < minCities + random.nextInt(maxCities - minCities + 1); i++) {
            int city_x = Math.round(random.nextFloat() * regionSize);
            int city_y = Math.round(random.nextFloat() * regionSize);

            int difference = maxCitySize - minCitySize;

            int width = minCitySize + Math.round(random.nextFloat() * difference);
            int height = minCitySize + Math.round(random.nextFloat() * difference);

            Settlement settlement = new Settlement(city_x, city_y, width, height);
            si.insert(settlement.getEnvelope(), settlement);
        }

        return si;
    }

}


