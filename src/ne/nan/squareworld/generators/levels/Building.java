package ne.nan.squareworld.generators.levels;

import com.vividsolutions.jts.geom.Envelope;
import javafx.scene.paint.Material;
import ne.nan.squareworld.model.Placeable;
import org.bukkit.material.MaterialData;
import org.jnbt.*;

import javax.annotation.processing.FilerException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by nanne on 21/03/16.
 */
public class Building extends Placeable {
    private int asInt;
    private Envelope envelope;
    private int type;
    private int x;
    private int y;

    private static int types = 1;

    public Building(int asInt, int type) {

        this.asInt = asInt;
        this.type = type;
    }

    public int width() {
        return asInt;
    }

    public int height() {
        return asInt;
    }

    public void setEnvelope(Envelope envelope) {
        this.envelope = envelope;
        this.x = (int)envelope.getMinX();
        this.y = (int)envelope.getMinY();
    }

    public Envelope getEnvelope() {
        return envelope;
    }


    public MaterialData[][][] generate() {
        MaterialData[][][] materials = getMaterialDatas(type + "_" + asInt);

        return materials;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static int getTypes() {
        return types;
    }
}
