package ne.nan.squareworld.generators.levels;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by s133781 on 26-2-16.
 */
public class City {
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

    private int generateCity(int startingX, int startingY, int sizeX, int sizeY, int randomPoints) {
//        short[][] intersections = new short[randomPoints][2];
//
////        Create randompoints to create the city around
//        for(int i = 0; i <= randomPoints; i++) {
//            Random rand = new Random();
//            short x = (short) (rand.nextInt(sizeX) + 1);
//            short y = (short) (rand.nextInt(sizeY) + 1);
//            intersections[i] = new short[]{x, y};
//        }

        int[][] city = new int[sizeX][sizeY];

        List<int[]> intersections = new ArrayList<>();
        for(int i = 0; i <= randomPoints; i++) {
            Random rand = new Random();
            int x = rand.nextInt(sizeX) + 1;
            int y = rand.nextInt(sizeY) + 1;
            intersections.add(new int[]{x, y});
        }
//http://stackoverflow.com/questions/223918/iterating-through-a-list-avoiding-concurrentmodificationexception-when-removing
        for(Iterator<int[]> iterator = intersections.iterator(); iterator.hasNext();) {
            int[] coord = iterator.next();
//            loop through all the coordtoo coordinations and check the distance, if the distance is too large remove the coordinate and break out of the loop
            for(int[] coordtoo : intersections) {
                if(distance(coord,coordtoo) < 50) {
                    iterator.remove();
                    break;
                }
            }
        }
//        make sure the points are enough distance away from each other, remove them if they aren't


//        Place the points in the city 2d int[] plain


    }







}
