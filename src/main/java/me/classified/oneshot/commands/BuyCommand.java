package me.classified.oneshot.commands;

import me.classified.oneshot.utils.Utils;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuyCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(!(sender instanceof Player)){
            return false;
        }
        Player p = (Player) sender;
        if(cmd.getName().equalsIgnoreCase("buy")){
            p.sendMessage(Utils.color(" "));
            p.sendMessage(Utils.color("&8&m----------------------------------"));
            p.sendMessage(Utils.color("&7          &8&l×&a&l×&8&l× &a&lSERVER SHOP &8&l×&a&l×&8&l×"));
            p.sendMessage(Utils.color(" &7&l» &f&nhttps://shop.oneshotmc.com/ &7&l«"));
            p.sendMessage(Utils.color("&8&m----------------------------------"));
            p.sendMessage(Utils.color(" "));

            p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 10, 2);
        }
        return false;
    }

}
