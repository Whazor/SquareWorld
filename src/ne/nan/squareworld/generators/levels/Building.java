package ne.nan.squareworld.generators.levels;

import com.vividsolutions.jts.geom.Envelope;
import javafx.scene.paint.Material;
import org.bukkit.material.MaterialData;
import org.jnbt.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by nanne on 21/03/16.
 */
public class Building {
    private int asInt;
    private Envelope envelope;
    private int type;
    private int x;
    private int y;

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

    int coord (int x, int y, int z, int width, int length) {
        return (y*length + z)*width + x;
    }

    public MaterialData[][][] generate() {
        MaterialData[][][] materials = new MaterialData[asInt][asInt][10];



        //check the size of the building and the type
//           @asint = size
//           @type = type of the building

        try {
            System.out.println("Loading building");
            File f = new File("schematics/" + type + "_" + asInt + ".schematic");

            FileInputStream fis = new FileInputStream(f);
            NBTInputStream nbt = new NBTInputStream(fis);
            CompoundTag backuptag = (CompoundTag) nbt.readTag();
            Map<String, Tag> tagCollection = backuptag.getValue();

            short width = (Short) getChildTag(tagCollection, "Width", ShortTag.class).getValue();
            short height = (Short) getChildTag(tagCollection, "Height", ShortTag.class).getValue();
            short length = (Short) getChildTag(tagCollection, "Length", ShortTag.class).getValue();

            byte[] blocks = (byte[]) getChildTag(tagCollection, "Blocks", ByteArrayTag.class).getValue();
            byte[] data = (byte[]) getChildTag(tagCollection, "Data", ByteArrayTag.class).getValue();
//            System.out.println(entities);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    for (int z = 0; z < length; z++) {
                        materials[x][z][y] = new MaterialData((int) blocks[coord(x,y,z,asInt,asInt)],data[coord(x,y,z,asInt,asInt)]);
                    }
                }
            }

//            List entities = (List) getChildTag(tagCollection, "Entities", ListTag.class).getValue();
//            List tileentities = (List) getChildTag(tagCollection, "TileEntities", ListTag.class).getValue();
            nbt.close();
            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return materials;
    }

    private static Tag getChildTag(Map<String, Tag> items, String key, Class<? extends Tag> expected) {
        Tag tag = items.get(key);
        return tag;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
