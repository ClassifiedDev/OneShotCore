package me.classified.oneshot.commands;

import me.classified.oneshot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.ArrayList;

public class BlockedCommands implements Listener {


	private static ArrayList<String> blockedCommands = new  ArrayList<String>();

	public static void loadBlockedCommands() {
		blockedCommands.add("/me");
		blockedCommands.add("/?");
		blockedCommands.add("/version");
		blockedCommands.add("/ver");
        blockedCommands.add("/spigot");
        blockedCommands.add("/plugins");
        blockedCommands.add("/icanhasbukkit");
        blockedCommands.add("/bukkit");
	}

	@EventHandler
	public void onBlockedCommand(PlayerCommandPreprocessEvent e){
		Player p = e.getPlayer();
		String[] args = e.getMessage().split(" ");
		if(blockedCommands.contains(args[0].toLowerCase())){
            if(p.hasPermission("oneshot.cmdbypass")) {
                p.sendMessage(Utils.color("&c&l(!) &cYou have used a blocked command but your rank has bypassed this restriction."));
                return;
            }
			e.setCancelled(true);
			p.sendMessage(Utils.color("&c&l(!) &cThe &7" + args[0] + " &ccommand is currently blocked or disabled."));
		}

	}

    @EventHandler
    public void onPluginPreCommand(PlayerCommandPreprocessEvent e){
        Player p = e.getPlayer();
        String[] args = e.getMessage().split(" ");
        if(args[0].equalsIgnoreCase("/pl") || args[0].equalsIgnoreCase("/plugins") || args[0].equalsIgnoreCase("/oneshotplugins") || args[0].equalsIgnoreCase("/plugin")){
            if(p.isOp() && !args[0].equalsIgnoreCase("/oneshotplugins")){
                return;
            }
            e.setCancelled(true);
            p.sendMessage(Utils.color(" "));
            p.sendMessage(Utils.color("&8&m-------------&r&8&l( &b&lONESHOT &b&lMC &8&l)&8&m-------------&r"));
            p.sendMessage(Utils.color(" "));
            p.sendMessage(Utils.color("&8&l* &7OneShot MC's custom plugins have been developed by the &cOneShot Development Team&7."));
            p.sendMessage(Utils.color(" "));
            for(Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins()){
                PluginDescriptionFile pluginDescriptionFile = plugin.getDescription();
                if(pluginDescriptionFile.getName().contains("Atlas")){
                    p.sendMessage(Utils.color("&8&l* &a" + pluginDescriptionFile.getName() + " &8(&7" + pluginDescriptionFile.getVersion() + "&8)"));
                }
            }
            for(Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins()){
                PluginDescriptionFile pluginDescriptionFile = plugin.getDescription();
                if(pluginDescriptionFile.getName().startsWith("OneShot")){
                    p.sendMessage(Utils.color("&8&l* &a" + pluginDescriptionFile.getName() + " &8(&7" + pluginDescriptionFile.getVersion() + "&8)"));
                }
            }
            p.sendMessage(Utils.color(" "));
            p.sendMessage(Utils.color("&8&m-------------------------------------&r"));
        }
    }
	
}
