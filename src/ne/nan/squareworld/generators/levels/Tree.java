package ne.nan.squareworld.generators.levels;

import com.vividsolutions.jts.geom.Envelope;
import ne.nan.squareworld.model.Placeable;
import org.bukkit.material.MaterialData;

/**
 * Created by nanne on 28/03/16.
 */
public class Tree extends Placeable {
    private int x;
    private int y;
    private int width;
    private int height;
    private Envelope envelope;

    public Tree(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void setEnvelope(Envelope envelope) {
        x = (int) envelope.getMinX();
        y = (int) envelope.getMinY();
        this.envelope = envelope;
    }


    @Override
    public Envelope getEnvelope() {
        return envelope;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
    }

    @Override
    public MaterialData[][][] generate() {
        return getMaterialDatas("tree_1");
    }

}
