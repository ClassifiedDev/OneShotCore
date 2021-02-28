package me.classified.oneshot.commands;
/*
 * AUTHOR: Classified
 * DISCORD: Classified#0001
 * DATE: 8/27/2020
 * PROJECT: OneShotCore
 */

import me.classified.oneshot.utils.Utils;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UploadCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(!(sender instanceof Player)){
            return false;
        }
        Player p = (Player) sender;
        if(cmd.getName().equalsIgnoreCase("upload")){
           p.sendMessage(Utils.color("&c&l(!) &cOur /upload feature is currently being reworked and will be available to all players very soon."));
            p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 10, 2);
        }
        return false;
    }

}
