package me.classified.oneshot.commands;

import me.classified.oneshot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class AnnounceCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(cmd.getName().equalsIgnoreCase("announce")){
            if(!sender.hasPermission("oneshot.announce")){
                sender.sendMessage(Utils.color("&c&l(!) &cYou do not have permission to use this command!"));
                return false;
            }
            if(args.length == 0){
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l(!) &cPlease enter a message to announce."));
                return false;
            }
            StringBuilder builder = new StringBuilder();
            Arrays.stream(args).forEach(arg -> builder.append(arg).append(" "));
            for(Player op : Bukkit.getServer().getOnlinePlayers()){
                op.playSound(op.getLocation(), Sound.SUCCESSFUL_HIT, 1, 2);
            }
            Bukkit.broadcastMessage(Utils.color("&8&m-------------------- &8( &3&lONESHOT &b&lMC &8)&8&m--------------------"));
            Bukkit.broadcastMessage(Utils.color( " "));
            Bukkit.broadcastMessage(Utils.color("&f     " + builder.toString().trim()));
            Bukkit.broadcastMessage(Utils.color(" "));
            Bukkit.broadcastMessage(Utils.color("&8&m------------------------------------------------------"));
            return false;
        }
        return false;
    }

}
