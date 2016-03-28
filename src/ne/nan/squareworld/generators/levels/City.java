package ne.nan.squareworld.generators.levels;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.quadtree.Quadtree;
import ne.nan.squareworld.algos.QuadTree;
import ne.nan.squareworld.algos.UF;
import ne.nan.squareworld.model.Settlement;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;

import java.util.*;
import java.util.function.IntSupplier;


/**
 * Created by s133781 on 26-2-16.
 */
public class City extends Settlement {
    private static Map<Integer, short[][]> cache = new HashMap<>();
    private final int randompoints;
    private int prunedistance = 35;
    private int roaddistancelatch = 15;
    private int roaddistancewidth = 20;
    private Quadtree buildings = new Quadtree();


    public City(int zaad, int x, int y, int width, int height, int randompoints, int prunedistance, int roaddistancelatch, int roaddistancewidth) {
        super(zaad, x, y, width, height);
        this.randompoints = randompoints;
        this.prunedistance = prunedistance;
        this.roaddistancewidth = roaddistancewidth;
        this.roaddistancelatch = roaddistancelatch;
    }

    public City(int zaad, int x, int y, int width, int height) {
        super(zaad, x, y, width, height);
        this.randompoints = 100;
//        this.prunedistance = 45;
    }

    public Hash hash() {
        return new Hash(zaad, x, y, width, height, randompoints, prunedistance);
    }
//    constructor met settlement
//    x,y

    @Override
    public short[][] generate() {
        Integer cid = hash().hashCode();
        if (cache.containsKey(cid)) {
            return cache.get(cid);
        }

//        if (cache.size() > 50) {
//            cache.remove(cache.keySet().iterator().next());
//        }

        System.out.println("Cache miss " + x + ","+ y);
        short[][] shorts = generateCity(x, y, width, height, randompoints, prunedistance);
        System.out.println(hash().hashCode());
        cache.put(cid, shorts);
        return shorts;
    }

    //    seed
/*
    We take an x and y for starting points to create the city around
    The city has by default a size of 5000 by 5000
    It is first divided into main sections


*/
    int distance(int[] coord1, int[] coord2) {
        return (int) Math.sqrt((coord1[0] - coord2[0]) * (coord1[0] - coord2[0]) + (coord1[1] - coord2[1]) * (coord1[1] - coord2[1]));
    }

    /**
     * @param x
     * @param y
     * @param distance
     * @param city
     * @param sizeX
     * @param sizeY
     * @return empty array when no result found, xy with rects when they have been found
     */
    private int neighboorroadhorizontal(int x, int y, int distance, short[][] city, int sizeX, int sizeY) {
        for (int x1 = x - distance; x1 < x + distance; x1++) {
            if (x1 >= 0 && x1 <= sizeX - 1) {
                if (city[x1][y] != 0 && x1 != x) {
                    return (x1);
                }
            }
        }
        return (0);
    }

    private int neighboorroadvertical(int x, int y, int distance, short[][] city, int sizeX, int sizeY) {
        for (int y1 = y - distance; y1 < y + distance; y1++) {
            if (y1 >= 0 && y1 <= sizeY - 1) {
                if (city[x][y1] != 0 && y1 != y) {
                    return (y1);
                }
            }
        }
        return (0);
    }

    private short[][] drawrect(int x_1, int y_1, int x_2, int y_2, short[][] city, short value) {
        int lowestx;
        int highestx;
        int lowesty;
        int highesty;
        if (x_1 < x_2) {
            lowestx = x_1;
            highestx = x_2;
        } else {
            lowestx = x_2;
            highestx = x_1;
        }
        if (y_1 < y_2) {
            lowesty = y_1;
            highesty = y_2;
        } else {
            lowesty = y_2;
            highesty = y_1;
        }
        int count = 0;
        for (int x = lowestx; x <= highestx; x++) {
            for (int y = lowesty; y <= highesty; y++) {

                if (count == 2) {
                    city[x][y] = 1;
                    count = 0;
                } else {
                    city[x][y] = value;
                    count++;
                }
            }
        }
        return city;
    }

    private short[][] generateCity(int startingX, int startingY, int sizeX, int sizeY, int randomPoints, int prunedistance) {
        short[][] city = new short[sizeX][sizeY];
        Random rand = new Random(zaad);
        List<int[]> intersections = new ArrayList<>();
        for (int i = 0; i <= randomPoints; i++) {
            int x = rand.nextInt(sizeX - 4) + 2;
            int y = rand.nextInt(sizeY - 4) + 2;
            intersections.add(new int[]{x, y, i});
        }

//http://stackoverflow.com/questions/223918/iterating-through-a-list-avoiding-concurrentmodificationexception-when-removing
//        remove all the nodes that are too close to eachother
        for (Iterator<int[]> iterator = intersections.iterator(); iterator.hasNext(); ) {
            int[] coord = iterator.next();
//            loop through all the coordtoo coordinations and check the distance, if the distance is too large remove the coordinate and break out of the loop
            for (int[] coordtoo : intersections) {
                if (distance(coord, coordtoo) < prunedistance && distance(coord, coordtoo) != 0) {
                    iterator.remove();
                    break;
                }
            }
        }
//        make sure the points are enough distance away from each other, remove them if they aren't

        int[] centercoordinate = {(int) sizeX / 2, (int) sizeY / 2};
        //store the length of point x to the center in the i variable
        for (int[] coordtoo : intersections) {
            coordtoo[2] = distance(centercoordinate, coordtoo);
        }

        Collections.sort(intersections, (a, b) -> {
            if (a[2] < b[2]) {
                return -1;
            } else if (a[2] > b[2]) {
                return 1;
            } else {
                return 0;
            }
//                return (Integer) (a[2]).compareTo(b[2]);
        });

//        Place the points in the city 2d int[] plain
//IDEA: take the center node, and go past all the nodes in order of the length ofthe center, then draw a rectangle
// without crossing another road  by the end you should get something nice? guess try, tryout tomorrow


//        Place the points of intersections in the short[], and while we iterate over the points place the roads
        int centercoordinatex = centercoordinate[0];
        int centercoordinatey = centercoordinate[1];


        city[centercoordinatex][centercoordinatey] = 1;
        for (int[] coordtoo : intersections) {
            short[][] backupcity;
//            System.arraycopy( city, 0, backupcity, 0, city.length );

            backupcity = city.clone();
            for (int x = 0; x < city.length; x++) {
                backupcity[x] = city[x].clone();
            }
//            city[coordtoo[0]][coordtoo[1]] = 1;
            int c_x = coordtoo[0];
            int c_y = coordtoo[1];
            List<int[]> rollbacklist = new ArrayList<>();
            rollbacklist.add(new int[]{c_x, c_y});
            int minlengthx = 0;
            int minlengthy = 0;
            int count = 0;
            city[c_x][c_y] = 35;

//            while the cursor is not on the x axis of the center and the cursor has not seen a 1 spot we move along the x axis
            while (c_x != centercoordinatex || c_y != centercoordinatey) {
                boolean horizontal = true;
                if (c_x > centercoordinatex) {
                    c_x--;
                    minlengthx++;
                } else if (c_x < centercoordinatex) {
                    c_x++;
                    minlengthx++;
                } else if (c_y > centercoordinatey) {
                    c_y--;
                    horizontal = false;
                } else if (c_y < centercoordinatey) {
                    c_y++;
                    horizontal = false;
                }
                if (city[c_x][c_y] == 35) {
                    break;
                }
                rollbacklist.add(new int[]{c_x, c_y});
                if (count == 2) {
                    city[c_x][c_y] = 1;
                    count = 0;
                } else {
                    city[c_x][c_y] = 35;
                    count++;
                }
                if (!horizontal) {
                    int temp = neighboorroadhorizontal(c_x, c_y, roaddistancelatch, city, sizeX, sizeY);

                    if (temp != 0) {
//                        city[temp][c_y] = 3;
//                        for loop from t_x to c_x then break out of the loop
                        city = drawrect(c_x, c_y, temp, c_y, city, (short) 35);
                        break;
                    }
                } else {
                    int temp = neighboorroadvertical(c_x, c_y, roaddistancelatch, city, sizeX, sizeY);

                    if (temp != 0) {
//                        city[c_x][temp] = 3;
//                        for loop from t_x to c_x then break out of the loop
                        city = drawrect(c_x, c_y, c_x, temp, city, (short) 35);
                        break;
                    }
                }

            }

//            reset the cursor and do the same for the other axis
            c_x = coordtoo[0];
            c_y = coordtoo[1];
            while (c_x != centercoordinatex || c_y != centercoordinatey) {
                boolean horizontal = false;
                if (c_y > centercoordinatey) {
                    c_y--;
                    minlengthy++;
                } else if (c_y < centercoordinatey) {
                    c_y++;
                    minlengthy++;
                } else if (c_x > centercoordinatex) {
                    c_x--;
                    horizontal = true;

                } else if (c_x < centercoordinatex) {
                    c_x++;
                    horizontal = true;

                }
                if (city[c_x][c_y] == 35) {
                    break;
                }
                rollbacklist.add(new int[]{c_x, c_y});
                if (count == 2) {
                    city[c_x][c_y] = 1;
                    count = 0;
                } else {
                    city[c_x][c_y] = 35;
                    count++;
                }
                if (!horizontal) {
                    int temp = neighboorroadhorizontal(c_x, c_y, roaddistancelatch, city, sizeX, sizeY);

                    if (temp != 0) {
//                        city[temp][c_y] = 3;
//                        for loop from t_x to c_x then break out of the loop
                        city = drawrect(c_x, c_y, temp, c_y, city, (short) 35);
                        break;
                    }
                } else {
                    int temp = neighboorroadvertical(c_x, c_y, roaddistancelatch, city, sizeX, sizeY);

                    if (temp != 0) {
//                        city[c_x][temp] = 3;
//                        for loop from t_x to c_x then break out of the loop
                        city = drawrect(c_x, c_y, c_x, temp, city, (short) 35);
                        break;
                    }
                }
            }

            if (minlengthx < roaddistancewidth || minlengthy < roaddistancewidth) {
//                System.arraycopy( backupcity, 0, city, 0, backupcity.length );
                city = backupcity.clone();
                for (int x = 0; x < city.length; x++) {
                    city[x] = backupcity[x].clone();
                }

//                System.out.println("minlength = " +  Math.min(minlengthx,minlengthy));

            }

        }
        /**
         * Add surounding black blocks around the white ones as a road.
         */
        for (int x = 0; x < city.length; x++) {
            for (int y = 0; y < city[x].length; y++) {
                short value = city[x][y];
                if (value == 35) {
//                    value is white thus a road, now we will make the surroundings black


                    for (int sx = x - 2; sx <= x + 2; sx++) {
                        for (int sy = y - 2; sy <= y + 2; sy++) {
//                            prevent out of bounds exception on underlying array
                            if (sy >= 0 && sx >= 0 && sx <= sizeX - 1 && sy <= sizeY - 1) {
//                                make pixel black if the underlying pixel is empty
                                if (city[sx][sy] == 0) {
                                    city[sx][sy] = 173;
                                }
                            }
                        }
                    }
//                    city[x][y] = 1;
                }
            }
        }

        /**
         * Change 0 values to grass color, id = 2
         */
        for (short[] element : city) {
            for (int i = 0; i < element.length; i++) {
                short value = element[i];
                if (value == 0) {
                    element[i] = 2;
                }
                if (value == 1) {
                    element[i] = 173;
                }
            }
        }
        for (int i1 = 0; i1 < city.length; i1++) {
            short[] element = city[i1];
            for (int i = 0; i < element.length; i++) {
                short value = element[i];
                if (0 == i1 || i == 0 || i1 == sizeX - 1 || i == sizeY - 1) {
                    element[i] = 41;
                }
            }
        }
//        public void drawrect(int[] a,int[] b) {
//
//    }
//        for(short[] element : city) {
//            for (int i = 0; i < element.length; i++) {
//                element[i] = 1;
//            }
//        }


        for (int size = 16; size >= 7; size--) {
            for (int i = 0; i < city.length - size; i++) {
                for (int j = 0; j < city[i].length - size; j++) {
                    boolean fail = false;
                    next:
                    for (int k = 0; k < size; k++) {
                        for (int l = 0; l < size; l++) {
                            if(city[i+k][j+l] != 2) {
                                fail = true;
                                break next;
                            }
                        }
                    }
                    // look for road
                    if (!fail) {
                        boolean noroad = true;
                        boolean space_top = true;
                        boolean space_left = true;
                        boolean space_right = true;
                        boolean space_bottom = true;

                        // check top
                        if(j > 0) {
                            for (int k = 0; k < size; k++) {
                                int block = city[i+k][j-1];
                                if (block == 173) {
                                    noroad = false;
//                                    space_bottom = true;

                                    break;
                                }
                            }
                        }
                        // check bottom
                        if(j + size < city[i].length) {
                            for (int k = 0; k < size; k++) {
                                int block = city[i+k][j + size];
                                if (block == 173) {
                                    noroad = false;
//                                    space_top = true;
                                    break;
                                }
                            }
                        }
                        // check left
                        if(i > 0) {
                            for (int k = 0; k < size; k++) {
                                int block = city[i - 1][j+k];
                                if (block == 173) {
                                    noroad = false;
//                                    space_right = true;
                                    break;
                                }
                            }
                        }
                        // check right
                        if(i + size < city.length) {
                            for (int k = 0; k < size; k++) {
                                int block = city[i + size][j+k];
                                if (block == 173) {
                                    noroad = false;
//                                    space_left = true;
                                    break;
                                }
                            }
                        }
                        if(!noroad) {
                            placeBuilding(city, new Building(size), i, j);

                            if(space_top)      placeRect(city, -1, i-1,      j,        size, 1);
                            if(space_bottom)   placeRect(city, -1, i+size,   j,        size, 1);
                            if(space_left)     placeRect(city, -1, i,        j-1,      1, size);
                            if(space_right)    placeRect(city, -1, i,        j+size, 1, size);
                        }
                    }
                }
            }
        }
        replace(city, -1, 2);
        return city;
    }

    private void put(short[][] city, short[][] shorts, int x, int y) {
        for (int i = 0; i < shorts.length; i++) {
            for (int j = 0; j < shorts[i].length; j++) {
                if (shorts[i][j] > 0)
                    city[i+x][j+y] = shorts[i][j];
            }
        }
    }

    int minSizeBuilding = 4;
    int maxSizeBuilding = 8;

    public MaterialData[][][] getChunk(int x, int y) {
        Envelope search = new Envelope(x, y, 16, 16);
        List list = buildings.query(search);

        MaterialData[][][] data = new MaterialData[16][16][100];


        for (Object obj : list) {
            Building building = (Building) obj;
            Envelope envelope = building.getEnvelope();
            if (envelope.intersects(search)) {
                MaterialData[][][] build = new MaterialData[16][16][10]; // = building.generate();
                Envelope intersection = envelope.intersection(search);
                int minX = (int) intersection.getMinX();
                int minY = (int) intersection.getMinY();
                int maxY = (int) intersection.getMaxY();
                int maxX = (int) intersection.getMaxX();


                int buildX = (int) (minX - search.getMinX());
                int buildY = (int) (minY - search.getMinY());

                for (int i = buildX; i < buildX + maxX; i++) {
                    System.arraycopy(build[i], buildY, data[minX + i], minY + buildY, buildY + maxY - buildY);
                }
            }
        }

        return data;
    }


    private void replace(short[][]city, int from, int to) {
        for (int i = 0; i < city.length; i++) {
            for (int j = 0; j < city[i].length; j++) {
                if(city[i][j] == from) {
                    city[i][j] = (short) to;
                }
            }
        }
    }
    private void placeRect(short[][] city, int block, int x, int y, int width, int height) {
        for (int i = x; i < x+width; i++) {
            for (int j = y; j < y + height; j++) {
                if(i > 0 && i < city.length & j > 0 && j < city.length) {
                    city[i][j] = (short) block;
                }
            }
        }
    }
    private void placeBuilding(short[][] city, Building build, int x, int y) {
        Envelope envelope = new Envelope(x, y, build.x(), build.y());
        build.setEnvelope(envelope);
        buildings.insert(envelope, build);

        for (int i = 0; i < build.x(); i++) {
            for (int j = 0; j < build.y(); j++) {
                city[x + i][y + j] = 3;
            }
        }
    }

    private enum Direction { TOP, LEFT, RIGHT, BOTTOM }
    private class Hash {
        private final int zaad;
        private final int x;
        private final int y;
        private final int width;
        private final int height;
        private final int randompoints;
        private final int prunedistance;

        public Hash(int zaad, int x, int y, int width, int height, int randompoints, int prunedistance) {
            this.zaad = zaad;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.randompoints = randompoints;
            this.prunedistance = prunedistance;
        }

        @Override
        public int hashCode() {
            return Objects.hash(zaad, x, y, width, height, randompoints, prunedistance);
        }
    }
}
