package ne.nan.squareworld;

import ne.nan.squareworld.generators.levels.City;
import org.bukkit.Location;
import org.bukkit.Material;
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
                City stad = new City(1234,0,0,500,500);
                short[][] destad = stad.generate();

//                get the players location and store them in x z integers

                Location loc = player.getLocation();
                int lx = (int) loc.getX();
                int lz = (int) loc.getZ();

                // Sets the block to type id 1 (stone).
                for (int x = 0; x < destad.length; x++) {
                    short[] xrow = destad[x];
                    for (int z = 0; z < xrow.length; z++) {
                        short value = xrow[z];

                        Location newloc = loc;
                        newloc.setX(loc.getX() + x);
                        newloc.setZ(loc.getZ() + z);

                        System.out.println("newloc = " +newloc);

                        Block b = newloc.getBlock();
                        b.setType(Material.getMaterial(value));
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
