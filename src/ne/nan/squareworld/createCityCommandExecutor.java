package ne.nan.squareworld;

import ne.nan.squareworld.generators.levels.City;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
//                correct arguments: /creatcity x y

                if (args.length > 2) {
                    sender.sendMessage("Too many arguments!");
                    return false;
                }
                if (args.length < 2) {
                    sender.sendMessage("Not enough arguments!");
                    return false;
                }
                System.out.println("Starting city creation");
                Player player = (Player) sender;

//                generate the city in destad
                City stad = new City(1234,0,0,Integer.parseInt(args[1]),Integer.parseInt(args[2]));
                short[][] destad = stad.generate();

//                get the players location and store them in x z integers

                Location loc = player.getLocation();
                int lx = (int) loc.getX();
                int lz = (int) loc.getZ();
                int ly = (int) loc.getY();
                World lw = loc.getWorld();



                Block bc = loc.getBlock();
                bc.setType(Material.getMaterial(1));
                loc.setX(loc.getX() + 2);
                bc = loc.getBlock();
                bc.setType(Material.getMaterial(2));


                System.out.println("set location stone");


                // Sets the block to type id 1 (stone).
                for (int x = 0; x < destad.length; x++) {
                    short[] xrow = destad[x];
                    for (int z = 0; z < xrow.length; z++) {
                        short value = xrow[z];

                        Location temploc = new Location(lw,lx + x,ly,lz + z);
                        System.out.println("temploc = " + temploc);
                        System.out.println("value = " + value);
                        temploc.setX(loc.getX());
                        temploc.setZ(loc.getZ());

                        Block b = temploc.getBlock();
                        b.setType(Material.getMaterial((int) value));

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
