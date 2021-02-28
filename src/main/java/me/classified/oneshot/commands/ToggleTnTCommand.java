package me.classified.oneshot.commands;

import me.classified.oneshot.listeners.ExplosionListener;
import me.classified.oneshot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleTnTCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(!(sender instanceof Player)){
			return false;
		}
		Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("toggletnt")){
            if(!p.hasPermission("oneshot.toggletnt")){
                p.sendMessage(Utils.color("&c&l(!) &cYou do not have permission to use this command!"));
                return false;
            }
			if(ExplosionListener.isTntEnabled()) {
				ExplosionListener.setTntEnabled(false);
				for(Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(Utils.color(" "));
                    player.sendMessage(Utils.color("&8&l(&4&l!&8&l) &4TNT explosion damage has been &c&l&nDISABLED&r &4by &7" + p.getDisplayName()));
                    player.sendMessage(Utils.color(" "));
                    if(p.hasPermission("oneshot.toggletnt")) {
                        player.sendMessage(Utils.color("&c&lSTAFF NOTICE: &7Use /toggletnt to ENABLE tnt explosions if this was a mistake."));
                    }
				}
				return false;
			}
            ExplosionListener.setTntEnabled(true);
			for(Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(Utils.color(" "));
                player.sendMessage(Utils.color("&8&l(&4&l!&8&l) &4TNT explosion damage has been &a&l&nENABLED&r &4by &7" + p.getDisplayName()));
                player.sendMessage(Utils.color(" "));
				if(p.hasPermission("oneshot.toggletnt")) {
                    player.sendMessage(Utils.color("&c&lSTAFF NOTICE: &7Use /toggletnt to DISABLE tnt explosions if this was a mistake."));
				}
			}
			return false;
		}
		return false;
	}

}
