package ne.nan.squareworld.generators.levels;

import com.vividsolutions.jts.geom.Envelope;
import javafx.scene.paint.Material;
import ne.nan.squareworld.generators.model.Direction;
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

    private int dic;

    Building(int asInt, Direction dic, int typ) {
        this.asInt = asInt;
        this.type = type;
        this.dic = dic.toInt();
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

//        System.out.println("type = " + type);
//        return getMaterialDatas(0 + "_" + asInt + "_" + dic);
        return getMaterialDatas(0 + "_" + asInt + "_" + dic);
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    static int getTypes() {
        return 1;
    }
}
