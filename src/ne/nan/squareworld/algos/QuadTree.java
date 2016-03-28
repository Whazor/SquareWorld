package ne.nan.squareworld.algos;

/**
 * Created by nanne on 22/03/16.
 */

public class QuadTree<Key extends Comparable<Key>, Value>  {
    private Node root;

    // helper node data type
    private class Node {
        Key x, y;              // width- and height- coordinates
        Node NW, NE, SE, SW;   // four subtrees
        Value value;           // associated data

        Node(int level, Key x, Key y, Value value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }
    }


    /***********************************************************************
     *  Insert (width, height) into appropriate quadrant
     ***************************************************************************/
    public void insert(Key x, Key y, Value value) {
        root = insert(0, root, x, y, value);
    }

    private Node insert(int level, Node h, Key x, Key y, Value value) {
        if (h == null) {
            return new Node(level, x, y, value);
        }
            //// if (eq(width, h.width) && eq(height, h.height)) h.value = value;  // duplicate
        else if ( less(x, h.x) &&  less(y, h.y)) h.SW = insert(level + 1, h.SW, x, y, value);
        else if ( less(x, h.x) && !less(y, h.y)) h.NW = insert(level + 1, h.NW, x, y, value);
        else if (!less(x, h.x) &&  less(y, h.y)) h.SE = insert(level + 1, h.SE, x, y, value);
        else if (!less(x, h.x) && !less(y, h.y)) h.NE = insert(level + 1, h.NE, x, y, value);
        return h;
    }


    /***********************************************************************
     *  Range search.
     ***************************************************************************/

//    public void query2D(Interval2D<Key> rect) {
//        query2D(root, rect);
//    }
//
//    private void query2D(Node h, Interval2D<Key> rect) {
//        if (h == null) return;
//        Key xmin = rect.intervalX.min();
//        Key ymin = rect.intervalY.min();
//        Key xmax = rect.intervalX.max();
//        Key ymax = rect.intervalY.max();
//        if (rect.contains(h.width, h.height))
//            StdOut.println("    (" + h.width + ", " + h.height + ") " + h.value);
//        if ( less(xmin, h.width) &&  less(ymin, h.height)) query2D(h.SW, rect);
//        if ( less(xmin, h.width) && !less(ymax, h.height)) query2D(h.NW, rect);
//        if (!less(xmax, h.width) &&  less(ymin, h.height)) query2D(h.SE, rect);
//        if (!less(xmax, h.width) && !less(ymax, h.height)) query2D(h.NE, rect);
//    }

    private boolean less(Key k1, Key k2) { return k1.compareTo(k2) <  0; }
    private boolean eq  (Key k1, Key k2) { return k1.compareTo(k2) == 0; }


}
