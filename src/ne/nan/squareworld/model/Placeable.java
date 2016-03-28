package ne.nan.squareworld.model;

import com.vividsolutions.jts.geom.Envelope;
import org.bukkit.material.MaterialData;
import org.jnbt.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by nanne on 28/03/16.
 */
public abstract class Placeable {

    public abstract void setEnvelope(Envelope envelope);

    protected MaterialData[][][] getMaterialDatas(String s) {
        MaterialData[][][] materials = new MaterialData[getX()][getY()][100];

        try {
            InputStream input = getClass().getResourceAsStream("/schematics/" + s + ".schematic");

            NBTInputStream nbt;
            FileInputStream fis = null;
            if(input == null) {
                File f = new File("schematics/" + s + ".schematic");
                fis = new FileInputStream(f);
                nbt = new NBTInputStream(fis);
            } else {
                nbt = new NBTInputStream(input);
            }

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
                        materials[x][z][y] = new MaterialData((int) blocks[coord(x,y,z,width,length)],data[coord(x,y,z,width,length)]);
                    }
                }
            }

            nbt.close();
            if (input != null) {
                input.close();
            }
            if(fis != null) {
                fis.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return materials;
    }
    int coord (int x, int y, int z, int width, int length) {
        return (y*length + z)*width + x;
    }


    private static Tag getChildTag(Map<String, Tag> items, String key, Class<? extends Tag> expected) {
        Tag tag = items.get(key);
        return tag;
    }


    public abstract int getX();

    public abstract int getY();

    public abstract int width();

    public abstract int height();

    public abstract MaterialData[][][] generate();

    public abstract Envelope getEnvelope();
}
