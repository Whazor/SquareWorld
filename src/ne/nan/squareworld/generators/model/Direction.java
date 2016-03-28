package ne.nan.squareworld.generators.model;

/**
 * Created by nanne on 28/03/16.
 */
public enum Direction {
    RIGHT(3),
    BOTTOM(2),
    LEFT(1),
    TOP(0);

    private int i;

    Direction(int i) {
        this.i = i;
    }

    public int toInt() {
        return this.i;
    }
}
