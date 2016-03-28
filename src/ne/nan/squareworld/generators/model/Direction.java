package ne.nan.squareworld.generators.model;

/**
 * Created by nanne on 28/03/16.
 */
public enum Direction {
    LEFT(0),
    TOP(1),
    RIGHT(2),
    BOTTOM(3);

    private int i;

    Direction(int i) {
        this.i = i;
    }

    public int toInt() {
        return this.i;
    }
}
