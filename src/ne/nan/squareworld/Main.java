package ne.nan.squareworld;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by nanne on 19/02/16.
 */
public class Main extends JavaPlugin {
    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id)
    {
        return new YourGenerator();
    }

    private class YourGenerator extends ChunkGenerator {
        private Boolean generateBedrockBlock;

        public YourGenerator() {
            this.generateBedrockBlock = true;
        }

        @Override
        public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
            ChunkData data = createChunkData(world);
            if (generateBedrockBlock && x == 0 && z == 0) {
                data.setBlock(0, 63, 0, Material.BEDROCK);
            }
            return data;
        }

        @Override
        public List<BlockPopulator> getDefaultPopulators(World world) {
            return new ArrayList<>(0);
        }

        @Override
        public Location getFixedSpawnLocation(World world, Random random) {
            if (generateBedrockBlock) {
                return new Location(world, 0.5, 64, 0.5);
            } else {
                return new Location(world, 0, 64, 0);
            }
        }
    }
}