package ne.nan.squareworld.generators.levels;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Created by nanne on 21/03/16.
 */
public class Building {
    private int asInt;
    private Envelope envelope;

    public Building(int asInt) {

        this.asInt = asInt;
    }

    public int x() {
        return asInt;
    }

    public int y() {
        return asInt;
    }

    public void setEnvelope(Envelope envelope) {
        this.envelope = envelope;
    }

    public Envelope getEnvelope() {
        return envelope;
    }
}
