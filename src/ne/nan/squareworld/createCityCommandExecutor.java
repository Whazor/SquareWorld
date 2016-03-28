package ne.nan.squareworld;

import ne.nan.squareworld.generators.levels.Building;
import ne.nan.squareworld.generators.levels.City;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

/**
 * Created by s133781 on 20-3-16.
 */
public class createCityCommandExecutor implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("createcity")) { // If the player typed /basic then do the following...
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command can only be run by a player.");
            } else {
//                check if there were enough arguments
//                correct arguments: /creatcity width height

                if (args.length > 7) {
                    sender.sendMessage("Too many arguments! /createcity <width> <height> <seed> <intersections> <prunedistance> <roadlatchdistance> <minblockwidth>");
                    return false;
                }
                if (args.length < 7) {
                    sender.sendMessage("Not enough arguments! /createcity <width> <height> <seed> <intersections> <prunedistance> <roadlatchdistance> <minblockwidth>");
                    return false;
                }
                System.out.println("Starting city creation");
                Player player = (Player) sender;

//                generate the city in destad
                int sizeX = Integer.parseInt(args[0]);
                int sizeY = Integer.parseInt(args[1]);
                int seed = Integer.parseInt(args[2]);
                int randompoints = Integer.parseInt(args[3]);
                int prunedistance = Integer.parseInt(args[4]);
                int roaddistancelatch = Integer.parseInt(args[5]);
                int roaddistancewidth = Integer.parseInt(args[6]);

                City stad = new City(seed,0,0,sizeX,sizeY,randompoints,prunedistance,roaddistancelatch,roaddistancewidth);
//                City stad = new City(1234,0,0,150,150);

                short[][] destad = stad.generate();

//                get the players location and store them in width z integers

                Location loc = player.getLocation();
                int lx = (int) loc.getX() - sizeX / 2;
                int lz = (int) loc.getZ() - sizeY / 2;
                int ly = (int) loc.getY();
                World lw = loc.getWorld();



                Block bc = loc.getBlock();
//                bc.setType(Material.STONE);
//                loc.setX(loc.getX() + 2);
//                bc = loc.getBlock();
//                bc.setType(Material.getMaterial("GRASS"));

                System.out.println("starting cleanup");
//clean up the city first
                for (int x = 0; x < destad.length; x++) {
                    for (int y = 0; y < destad[x].length; y++) {
                        for (int z = 1; z < 10; z++) {
                            Location temploc = new Location(lw,lx + x,z ,lz + y);
                            Block b = temploc.getBlock();
                            if(b.getType() != Material.AIR) {
                                b.setType(Material.AIR);
                            }
                        }
                    }
                }
                System.out.println("starting city roadmap");
                // Sets the block to type id 1 (stone).
                for (int x = 0; x < destad.length; x++) {
                    short[] xrow = destad[x];
                    for (int z = 0; z < xrow.length; z++) {
                        short value = xrow[z];
                        ly = 0;
                        Location temploc = new Location(lw,lx + x,ly ,lz + z);
//                        System.out.println("temploc = " + temploc);
//                        System.out.println("value = " + value);
//                        temploc.setX(loc.getX());
//                        temploc.setZ(loc.getZ());

                        Block b = temploc.getBlock();
//                        b.setType(Material.STONE);
                        if(value == 3) {
                            value = 2;
                        }
                        b.setType(Material.getMaterial(value));

                    }
                }

                System.out.println("starting placing buildings");
                for(Building gebouw: stad.getBuildings()) {
                    int s_x = gebouw.getX();
                    int s_y = gebouw.getY();
                    int width  = gebouw.width();
                    World lworld = loc.getWorld();

                    MaterialData[][][] materials = gebouw.generate();
                    for (int x = 0; x < materials.length; x++) {
                        for (int y = 0; y < materials[x].length; y++) {
                            for (int z = 0; z < materials[x][y].length; z++) {
                                Location temploc = new Location(lworld,x + s_x + lx,z +1,y + s_y + lz);
                                Block b = temploc.getBlock();
                                if(materials[x][y][z] != null && materials[x][y][z].getItemType() != null) {
                                    b.setType(materials[x][y][z].getItemType());
                                    b.setData(materials[x][y][z].getData());
                                }
                            }
                        }

                    }

                }
                return true;
                // do something
            }
            return true;


        } //If this has happened the function will return true.
        // If this hasn't happened the value of false will be returned.
        return false;
    }


}
