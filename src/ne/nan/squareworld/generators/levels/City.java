package ne.nan.squareworld.generators.levels;

import ne.nan.squareworld.model.Settlement;

import java.util.*;

/**
 * Created by s133781 on 26-2-16.
 */
public class City extends Settlement {
    private final int randompoints;
    private final int prunedistance;

    public City(int zaad, int x, int y, int width, int height, int randompoints, int prunedistance) {
        super(zaad, x, y, width, height);
        this.randompoints = randompoints;
        this.prunedistance = prunedistance;
    }
    public City(int zaad, int x, int y, int width, int height) {
        super(zaad, x, y, width, height);
        this.randompoints = 100;
        this.prunedistance = 40;
    }

    private class Hash {
        private final int zaad;
        private final int x;
        private final int y;
        private final int width;
        private final int height;
        private final int randompoints;
        private final int prunedistance;

        public Hash(int zaad, int x, int y, int width, int height, int randompoints, int prunedistance){
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

    static Map<Hash, short[][]> cache = new HashMap<>();

    public Hash hash() {
        return new Hash(zaad, x, y, width, height, randompoints, prunedistance);
    }

    @Override
    public short[][] generate() {
        if(cache.containsKey(hash())) {
            return cache.get(hash());
        }

        if(cache.size() > 20) {
            cache.remove(cache.keySet().iterator().next());
        }

        short[][] shorts = generateCity(x, y, width, height, randompoints, prunedistance);
        cache.put(hash(), shorts);
        return shorts;
    }
//    constructor met settlement
//    x,y

//    seed
/*
    We take an x and y for starting points to create the city around
    The city has by default a size of 5000 by 5000
    It is first divided into main sections


*/
    int distance(int[] coord1, int[] coord2) {
        return (int) Math.sqrt((coord1[0]-coord2[0])*(coord1[0]-coord2[0]) + (coord1[1]-coord2[1])*(coord1[1]-coord2[1]));
    }

    private short[][] generateCity(int startingX, int startingY, int sizeX, int sizeY, int randomPoints, int prunedistance) {
//        short[][] intersections = new short[randomPoints][2];
//
////        Create randompoints to create the city around
//        for(int i = 0; i <= randomPoints; i++) {
//            Random rand = new Random();
//            short x = (short) (rand.nextInt(sizeX) + 1);
//            short y = (short) (rand.nextInt(sizeY) + 1);
//            intersections[i] = new short[]{x, y};
//        }

        short[][] city = new short[sizeX][sizeY];
        Random rand = new Random(zaad);
        List<int[]> intersections = new ArrayList<>();
        for(int i = 0; i <= randomPoints; i++) {
            int x = rand.nextInt(sizeX);
            int y = rand.nextInt(sizeY);
            intersections.add(new int[]{x, y, i});
        }

//http://stackoverflow.com/questions/223918/iterating-through-a-list-avoiding-concurrentmodificationexception-when-removing
//        remove all the nodes that are too close to eachother
        for(Iterator<int[]> iterator = intersections.iterator(); iterator.hasNext();) {
            int[] coord = iterator.next();
//            loop through all the coordtoo coordinations and check the distance, if the distance is too large remove the coordinate and break out of the loop
            for(int[] coordtoo : intersections) {
                if(distance(coord,coordtoo) < prunedistance && distance(coord,coordtoo) != 0) {
                    iterator.remove();
                    break;
                }
            }
        }
//        make sure the points are enough distance away from each other, remove them if they aren't

        int[] centercoordinate = {(int) sizeX / 2, (int) sizeY / 2};
        //store the length of point x to the center in the i variable
        for(int[] coordtoo : intersections) {
           coordtoo[2] =  distance(centercoordinate,coordtoo);
        }

        Collections.sort(intersections, (a, b) -> {
            if(a[2] < b[2]) { return -1; }
            else if(a[2] > b[2]) { return 1; }
            else { return 0; }
//                return (Integer) (a[2]).compareTo(b[2]);
        });

//        Place the points in the city 2d int[] plain
//IDEA: take the center node, and go past all the nodes in order of the length ofthe center, then draw a rectangle
// without crossing another road  by the end you should get something nice? guess try, tryout tomorrow


//        Place the points of intersections in the short[], and while we iterate over the points place the roads
        int centercoordinatex = centercoordinate[0];
        int centercoordinatey = centercoordinate[1];

        city[centercoordinatex][centercoordinatey] = 1;
        for(int[] coordtoo : intersections) {
//            city[coordtoo[0]][coordtoo[1]] = 1;
            int c_x = coordtoo[0];
            int c_y = coordtoo[1];
            List<int[]> rollbacklist = new ArrayList<>();
            rollbacklist.add(new int[]{c_x, c_y});
            int minlengthx = 0;
            int minlengthy = 0;

            city[c_x][c_y] = 35;
//            while the cursor is not on the x axis of the center and the cursor has not seen a 1 spot we move along the x axis
            while(c_x != centercoordinatex || c_y != centercoordinatey) {

                if(c_x > centercoordinatex) {
                    c_x--;
                    minlengthx++;
                } else if(c_x < centercoordinatex) {
                    c_x++;
                    minlengthx++;
                } else if(c_y > centercoordinatey) {
                    c_y--;
                } else if(c_y < centercoordinatey) {
                    c_y++;
                }
                if(city[c_x][c_y] == 35) {
                    break;
                }
                rollbacklist.add(new int[]{c_x, c_y});
                city[c_x][c_y] = 35;
            }

//            reset the cursor and do the same for the other axis
            c_x = coordtoo[0];
            c_y = coordtoo[1];
            while (c_x != centercoordinatex || c_y != centercoordinatey) {

                if(c_y > centercoordinatey) {
                    c_y--;
                    minlengthy++;
                } else if(c_y < centercoordinatey) {
                    c_y++;
                    minlengthy++;
                } else if(c_x > centercoordinatex) {
                    c_x--;

                } else if(c_x < centercoordinatex) {
                    c_x++;

                }
                if(city[c_x][c_y] == 35) {
                    break;
                }
                rollbacklist.add(new int[]{c_x, c_y});
                city[c_x][c_y] = 35;
            }

            if(minlengthx < 15 || minlengthy < 15) {
                for(int[] co: rollbacklist) {
                    city[co[0]][co[1]] = 0;
                }
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


                    for(int sx = x-1; sx <= x+1; sx++) {
                        for(int sy = x-1; sy <= x+1; sy++) {
//                            prevent out of bounds exception on underlying array
                            if(sy >= 0 && sx >= 0 && sx <= sizeX && sy <= sizeY) {
//                                make pixel black if the underlying pixel is empty
                                if(city[sx][sy] == 0) {
                                    city[sx][sy] = 173;
                                }
                            }
                        }
                    }
                    city[x][y] = 1;
                }
            }
        }

        /**
         * Change 0 values to grass color, id = 2
         */
        for(short[] element : city) {
            for (int i = 0; i < element.length; i++) {
                short value = element[i];
                if (value == 0) {
                    element[i] = 2;
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
        return city;
    }







}
