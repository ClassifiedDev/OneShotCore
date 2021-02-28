package me.classified.oneshot.listeners;

import me.classified.oneshot.OneShotCore;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class ExplosionListener implements Listener {


	public static boolean isTntEnabled(){
        return OneShotCore.getConfigFile().getBoolean("tnt-enabled");
    }

    public static boolean isCreepersEnabled(){
        return OneShotCore.getConfigFile().getBoolean("creepers-enabled");
    }


    public static void setTntEnabled(boolean tntEnabled) {
        OneShotCore.getConfigFile().set("tnt-enabled", tntEnabled);
        OneShotCore.saveConfigFile();
    }


    public static void setCreepersEnabled(boolean creepersEnabled) {
        OneShotCore.getConfigFile().set("creepers-enabled", creepersEnabled);
        OneShotCore.saveConfigFile();
    }

    @EventHandler
	public void explosionEvent(EntityExplodeEvent e){
		Entity entity = e.getEntity();
		if(entity instanceof TNTPrimed) {
			if(!isTntEnabled()) {
				e.blockList().clear();
				e.setCancelled(true);
			}	
		}
		if(entity instanceof Creeper) {
			if(!isCreepersEnabled()) {
				e.blockList().clear();
				e.setCancelled(true);
			}	
		}
	}

    @EventHandler
    public void playerTNTDamge(EntityDamageByEntityEvent e) {
	    if(e.getEntity() instanceof Player) {
            if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
                e.setCancelled(true);
            }
        }
    }


}
