package me.classified.oneshot.commands;

import me.classified.oneshot.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class HelpCommand implements Listener {
	
	@EventHandler
	public void onHelpPreCommand(PlayerCommandPreprocessEvent e){
		Player p = e.getPlayer();
	  String[] args = e.getMessage().split(" ");
	  if(args[0].equalsIgnoreCase("/help")){
	      if(!p.isOp()){
              e.setCancelled(true);
          }
	    p.sendMessage(Utils.color(" "));
	    p.sendMessage(Utils.color("&8&m------------&r&8&l( &3&lONESHOT &b&lMC &8&l)&8&m------------&r"));
	    p.sendMessage(Utils.color(" "));
	    p.sendMessage(Utils.color("&8&l× &a&lSHOP: &7shop.oneshotmc.com"));
	    p.sendMessage(Utils.color("&8&l× &e&lDISCORD: &7discord.oneshotmc.com"));
	    p.sendMessage(Utils.color("&8&l× &b&lRECOMMENDED VERSION: &71.8.8"));
	    p.sendMessage(Utils.color(" "));
	    p.sendMessage(Utils.color("&8&m--------------------------------------&r"));
	    p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
	    e.setCancelled(true);
	  }
	}
}
