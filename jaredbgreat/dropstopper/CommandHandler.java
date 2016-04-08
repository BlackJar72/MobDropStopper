package jaredbgreat.dropstopper;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author jared
 */
public class CommandHandler implements CommandExecutor {
    MobDropStopper owner;
    
    
    CommandHandler(MobDropStopper owner) {
        this.owner = owner;
    }
    
    
    public static final String[] help = new String[]{
        "MobDropStoper Commands: ",
        "   mbdropstopper remove [item] : mobs will no longer drop [item]",
        "   mbdropstopper replace [item] [repacement]: [item] will be replace " 
                    + "in mob drops",
        "   mbdropstopper allow [item] : [item] will appear in mobs drops " 
                    + "(undoes remove / replace)",        
        "   mbdropstopper commit : will save the plugin config data",        
        "   mbdropstopper save : the same as commit (above)",                
        "   mbdropstopper reload : reloads config, reverting to last save (if any)",
        "   mbdropstopper help : get this message"        
    };
    

    @Override
    public boolean onCommand(CommandSender sender, Command command, 
                             String label, String[] args) {
        boolean valid = false;
        
        if(args.length < 1) {
                   sender.sendMessage("Warning: mobdropstopper requires arguments!");
                   giveHelp((Player)sender);
                   return false;
               }
        
        for(int i = 0; i < args.length; i++) {
            args[i] = args[i].toUpperCase();
        }
        
        if(args[0].toUpperCase().equals("REMOVE")) {
            if(args.length < 2) {
                sender.sendMessage("Warning: item not specified; try " + label 
                        + " remove [item]");
            } else {
                valid = owner.addRemoval(args[1]);
                sender.sendMessage("Removing " + args[1] 
                        + " from future mob drops");
            }
            if(!valid) {
                sender.sendMessage("Warning: " + args[1] + " is not a valid " + 
                        "item name from org.bukkit.Material!");
            }
        } else if(args[0].toUpperCase().equals("REPLACE")) {
            if(args.length != 3) {
                sender.sendMessage("Warning: item not specified; try " + label 
                        + " replace [item] [replacement]");
            } else {
                valid = owner.addReplacement(args[1], args[2]);
                sender.sendMessage("Replacing " + args[1] + " with " + args[2]
                        + " in future mob drops");
            } 
            if(!valid) {
                sender.sendMessage("Warning: " + args[1] + " or " + args[2] 
                        + " is not a valid item name from org.bukkit.Material!");
            }           
        } else if(args[0].toUpperCase().equals("ALLOW")) {
            if(args.length < 2) {
                sender.sendMessage("Warning: item not specified; try " + label 
                        + " allow [item]");
            } else {
                valid = owner.allowDrop(args[1]);
                sender.sendMessage("Returning " + args[1] 
                        + " to future mob drops");
            }
            if(!valid) {
                sender.sendMessage("Warning: " + args[1] + " is not a valid " + 
                        "item name from org.bukkit.Material!");
            }
        } else if(args[0].toUpperCase().equals("HELP")) {
            giveHelp((Player)sender);
            valid = true;
        } else if(args[0].toUpperCase().equals("SAVE") || args[0].equals("COMMIT")) {
            owner.saveMyConfig();
            sender.sendMessage("MobDropStopper config saved");
            valid = true;
        } else if(args[0].toUpperCase().equals("RELOAD")) {
            owner.reloadConfig();
            owner.configure();
            sender.sendMessage("MobDropStopper config reloaded " 
                    + "/ reverted to last save");
            valid = true;
        }
        
        return valid;
    }
    
    
    public void giveHelp(Player sender) {
        sender.sendMessage(help);
    }
    
}
