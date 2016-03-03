package ne.nan.squareworld.generators.levels;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.SpatialIndex;
import com.vividsolutions.jts.index.quadtree.Quadtree;
import ne.nan.squareworld.model.Settlement;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;


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
            if(map.size() > 100) {
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

        SpatialIndex si = new Quadtree();


        for (int i = 0; i < minCities + random.nextInt(maxCities - minCities + 1); i++) {
            boolean isIntersected;
            do {
                isIntersected = false;
                int city_x = Math.round(random.nextFloat() * regionSize);
                int city_y = Math.round(random.nextFloat() * regionSize);

                int difference = maxCitySize - minCitySize;

                int width = minCitySize + Math.round(random.nextFloat() * difference);
                int height = width;//minCitySize + Math.round(random.nextFloat() * difference);

                Settlement settlement = new Settlement(city_x, city_y, width, height);
                Envelope envelope = settlement.getEnvelope();
                List list = si.query(envelope);

                Stream<Settlement> stream = filter(list, envelope);
                if(stream.count() == 0) {
                    si.insert(settlement.getEnvelope(), settlement);
                } else {
                    isIntersected = true;
                }
            } while(isIntersected);
        }

        return si;
    }

    public Stream<Settlement> filter(List list, Envelope envelope) {
        return list.stream().filter(p -> ((Settlement)p).getEnvelope().intersects(envelope));
    }
}


