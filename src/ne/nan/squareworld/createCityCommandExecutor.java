package ne.nan.squareworld;

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

                return true;
                // do something
            }
            return true;


        } //If this has happened the function will return true.
        // If this hasn't happened the value of false will be returned.
        return false;
    }


}
