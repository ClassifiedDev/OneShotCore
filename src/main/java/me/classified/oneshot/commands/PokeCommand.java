package me.classified.oneshot.commands;

import me.classified.oneshot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PokeCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(!(sender instanceof Player)){
            return false;
        }
        Player p = (Player) sender;
        if(cmd.getName().equalsIgnoreCase("poke")){
            if(!p.hasPermission("oneshot.poke")){
                p.sendMessage(Utils.color("&c&l(!) &cYou do not have permission to use this command!"));
                return false;
            }
            if(args.length != 1){
                sender.sendMessage(Utils.color("&3&l(!) &3Usage: &7/poke <player> "));
                sender.sendMessage(Utils.color("&3&l* &7Sends the player a poke message and plays a sound effect."));
                return false;
            }
            Player target = Bukkit.getServer().getPlayer(args[0]);
            if(target == null){
                sender.sendMessage(Utils.color("&c&c&l(!) &cThat player does can not be found!"));
                return false;
            }
            target.playSound(target.getLocation(), Sound.SUCCESSFUL_HIT, 10 , 1);
            target.playSound(target.getLocation(), Sound.LEVEL_UP, 10 , 2);
            target.sendMessage(Utils.color("&8&l(&3&l!&8&l) &7You have been poked by &3&l" + p.getDisplayName() + "&7!"));
            p.sendMessage(Utils.color("&8&l(&3&l!&8&l) " + target.getDisplayName() + " &7has been poked!"));
            return false;
        }
        return false;
    }

}
