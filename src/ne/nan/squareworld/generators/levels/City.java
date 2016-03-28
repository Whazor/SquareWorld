package ne.nan.squareworld.generators.levels;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.quadtree.Quadtree;
import ne.nan.squareworld.generators.model.Direction;
import ne.nan.squareworld.model.Placeable;
import ne.nan.squareworld.model.Settlement;
import org.bukkit.material.MaterialData;

import java.util.*;


/**
 * Created by s133781 on 26-2-16.
 */
public class City extends Settlement {
    private static Map<Integer, short[][]> cache = new HashMap<>();
    private final int randompoints;
    private int prunedistance = 20;
    private int roaddistancelatch = 15;
    private int roaddistancewidth = 20;
    private Quadtree buildings = new Quadtree();


    public List<Placeable> getBuildings() {
        List list = buildings.queryAll();
        ArrayList<Placeable> arr = new ArrayList<>();
        for (Object objBuild: list) {
            arr.add((Placeable) objBuild);
        }
        return arr;
    }


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
        return new Hash(zaad, x, y, width, height, randompoints, prunedistance, roaddistancelatch, roaddistancewidth);
    }
//    constructor met settlement
//    width,height

    @Override
    public short[][] generate() {
        Integer cid = hash().hashCode();
        if (cache.containsKey(cid)) {
            return cache.get(cid);
        }

//        if (cache.size() > 50) {
//            cache.remove(cache.keySet().iterator().next());
//        }

        short[][] shorts = generateCity(x, y, width, height, randompoints, prunedistance);
        cache.put(cid, shorts);
        return shorts;
    }

    //    seed
/*
    We take an width and height for starting points to create the city around
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
            int x = rand.nextInt(sizeX - 32) + 16;
            int y = rand.nextInt(sizeY - 32) + 16;
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
        //store the length of point width to the center in the i variable
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

//            while the cursor is not on the width axis of the center and the cursor has not seen a 1 spot we move along the width axis
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
        int roadwidth =1; // this value *2 + decides the width of the road
        for (int x = 0; x < city.length; x++) {
            for (int y = 0; y < city[x].length; y++) {
                short value = city[x][y];
                if (value == 35) {
//                    value is white thus a road, now we will make the surroundings black


                    for (int sx = x - roadwidth; sx <= x + roadwidth; sx++) {
                        for (int sy = y - roadwidth; sy <= y + roadwidth; sy++) {
//                            prevent out of bounds exception on underlying array
                            if (sy >= 0 && sx >= 0 && sx <= sizeX - 1 && sy <= sizeY - 1) {
//                                make pixel black if the underlying pixel is empty
                                if (city[sx][sy] == 0) {
                                    city[sx][sy] = 173;
                                }
                            }
                        }
                    }
//                    city[width][height] = 1;
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

        //change road to more natural look



//        adds a border around the city
//
//        public void drawrect(int[] a,int[] b) {
//
//    }
//        for(short[] element : city) {
//            for (int i = 0; i < element.length; i++) {
//                element[i] = 1;
//            }
//        }


        Random rnd = new Random(zaad);
        LinkedList<Integer> integers = new LinkedList<>();
        for (int k = 7; k <= 12; k++) {
            integers.add(k);
        }
//        integers.add(12);
//        integers.add(11);

        int width = city.length - 12;
        int height = city[0].length - 12;

        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < width * height; i++) {
            list.add(i);
        }

        Collections.shuffle(list, rnd);
//
        for(int m : list) {
            int i = m % width;
            int j = (int) Math.floor(m / width);
//        for (int i = 0; i < city.length - 12; i++) {
//            for (int j : heightlist) {
//            for (int j = 0; j < city[i].length - 12; j++) {

                Collections.shuffle(integers, rnd);
//                System.out.println(integers);
                for (int size: integers.subList(0, 3)) {
//                    for (int size = 12; size >= 7; size--) {
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

                        Direction dic = null;
                        // check top
                        if(j > 0) {
                            for (int k = 0; k < size; k++) {
                                int block = city[i+k][j-1];
                                if (block == 173) {
                                    noroad = false;
                                    dic = Direction.TOP;
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
                                    dic = Direction.BOTTOM;
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
                                    dic = Direction.LEFT;
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
                                    dic = Direction.RIGHT;
                                    break;
                                }
                            }
                        }
                        if(!noroad) {

                            placeBuilding(city, new Building(size,dic, rnd.nextInt(Building.getTypes())+1), i, j);

//                            if(space_top)      placeRect(city, -1, i-1,      j,        size, 1);
//                            if(space_bottom)   placeRect(city, -1, i+size,   j,        size, 1);
//                            if(space_left)     placeRect(city, -1, i,        j-1,      1, size);
//                            if(space_right)    placeRect(city, -1, i,        j+size, 1, size);
                        }
                    }
                }
            }
//        }
        replace(city, -1, 2);
        replace(city,35,13);
        replace(city,173,13);
        replacechange(city,13,2);


        //////////////////////// place trees

        int prunetreedistance = 4;
        int placetrees = 2950;
        int lengtdistancetree = 15; //checks the surroundings of a tree if it should place a tree or not


        intersections = new ArrayList<>();
        for (int i = 0; i <= placetrees; i++) {
            int x = rand.nextInt(sizeX - 32) + 16;
            int y = rand.nextInt(sizeY - 32) + 16;
            intersections.add(new int[]{x, y, i});
        }

//http://stackoverflow.com/questions/223918/iterating-through-a-list-avoiding-concurrentmodificationexception-when-removing
//        remove all the nodes that are too close to eachother


        for (Iterator<int[]> iterator = intersections.iterator(); iterator.hasNext(); ) {
            int[] coord = iterator.next();
//            loop through all the coordtoo coordinations and check the distance, if the distance is too large remove the coordinate and break out of the loop
            for (int[] coordtoo : intersections) {
                if (distance(coord, coordtoo) < prunetreedistance && distance(coord, coordtoo) != 0) {
                    iterator.remove();
                    break;
                }
            }
        }

        for (int[] coordtoo : intersections) {
            coordtoo[2] = distance(centercoordinate, coordtoo);
//            check if the tree is to close to a road
            boolean treevalid = true;
            hhjhjh:
            for (int y_s = coordtoo[1]; y_s <= coordtoo[1] + 5; y_s++) {
                for (int x_s = coordtoo[0]; x_s <= coordtoo[0] + 5; x_s++) {
                    if(city[x_s][y_s] != 2) {
                        treevalid = false;
                        break hhjhjh;
                    }
                }
            }




            if(treevalid) {
                treevalid = false;

                //remove trees on the sides
                int o_x = coordtoo[0];
                int o_y = coordtoo[1];

                for (int i = o_x; i < o_x +lengtdistancetree; i++) {
                    if(i > 0 && i < city.length && city[i][o_y] != 2) {
                        treevalid = true;
                    }
                }
                for (int i = o_x; i < o_x -lengtdistancetree; i--) {
                    if(i > 0 && i < city.length && city[i][o_y] != 2) {
                        treevalid = true;
                    }
                }
                for (int i = o_y; i < o_y +lengtdistancetree; i++) {
                    if(i > 0 && i < city[o_x].length && city[o_x][i] != 2) {
                        treevalid = true;
                    }
                }
                for (int i = o_y; i < o_y -lengtdistancetree; i--) {
                    if(i > 0 && i < city[o_x].length && city[o_x][i] != 2) {
                        treevalid = true;
                    }
                }
                if(treevalid) {
                    placeBuilding(city, new Tree(coordtoo[0], coordtoo[1], 5, 5), coordtoo[0], coordtoo[1]);
                }
            }
        }

        return city;
    }

    private Integer distanceBlaat(int[] i1, int[] i2) {
        return Math.min(i1[0] - i2[0], i1[1] - i2[1]);
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
        Envelope search = new Envelope(x, x+16, y, y + 16);
        List list = buildings.query(search);


        int height = 100;
        MaterialData[][][] data = new MaterialData[16][16][height];

        for (Object obj : list) {
            Placeable building = (Placeable) obj;
            Envelope envelope = building.getEnvelope();
            if (envelope.intersects(search)) {
//                System.out.println(building.getX() + " " + building.getY() + " - " + building.width());

                MaterialData[][][] build = building.generate();
                Envelope intersection = envelope.intersection(search);
                int searchX = (int) search.getMinX();
                int searchY = (int) search.getMinY();

                int minX = (int) intersection.getMinX();
                int minY = (int) intersection.getMinY();
                int maxY = (int) intersection.getMaxY();
                int maxX = (int) intersection.getMaxX();


                for (int i = minX; i < maxX; i++) {
                    for (int j = minY; j < maxY; j++) {
                        int buildX = i - building.getX();
                        int buildY = j - building.getY();

                        if(buildX < build.length && buildY < build[buildX].length) {
                            for (int k = 0; k < Math.min(height, build[buildX][buildY].length); k++) {
                                MaterialData data1 = build[buildX][buildY][k];

                                data[i - x][j - y][k] = data1;
                            }
                        }
                    }
//                    System.arraycopy(build[i], buildY, data[minX + i], minY + buildY, buildY + maxY - buildY);
                }
            }
        }

        return data;
    }

    private void replacechange(short[][]city, int from, int to) {
        Random rand = new Random(zaad);
        for (int i = 0; i < city.length; i++) {
            for (int j = 0; j < city[i].length; j++) {
                if(city[i][j] == from) {
                    int num = rand.nextInt(100);
                    if(num > 70) {
                        city[i][j] = (short) to;
                    }
                }
            }
        }
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
    private void placeBuilding(short[][] city, Placeable build, int x, int y) {
        Envelope envelope = new Envelope(x, x+build.width(), y, y + build.height());
        build.setEnvelope(envelope);
        buildings.insert(envelope, build);

        for (int i = 0; i < build.width(); i++) {
            for (int j = 0; j < build.height(); j++) {
                city[x + i][y + j] = 3;
            }
        }
    }

    private class Hash {
        private final int zaad;
        private final int x;
        private final int y;
        private final int width;
        private final int height;
        private final int randompoints;
        private final int prunedistance;
        private final int roaddistancelatch;
        private final int roaddistancewidth;


        public Hash(int zaad, int x, int y, int width, int height, int randompoints, int prunedistance, int roaddistancelatch, int roaddistancewidth) {
            this.zaad = zaad;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.randompoints = randompoints;
            this.prunedistance = prunedistance;
            this.roaddistancelatch = roaddistancelatch;
            this.roaddistancewidth = roaddistancewidth;
        }

        @Override
        public int hashCode() {
            return Objects.hash(zaad, x, y, width, height, randompoints, prunedistance, roaddistancelatch, roaddistancewidth);
        }
    }
}
