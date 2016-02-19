package ne.nan.squareworld;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

/**
 * Created by nanne on 19/02/16.
 */
public class Main extends JavaPlugin {

    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id)
    {
        return new YourGenerator();
    }

    private class YourGenerator extends ChunkGenerator {
        @Override
        public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {

            ChunkData chunkData = createChunkData(world);
            getGenerator(world).generateChunkData(random, mx, mz, chunkData);
            return chunkData;



            return super.generateChunkData(world, random, x, z, biome);
        }
    }
}