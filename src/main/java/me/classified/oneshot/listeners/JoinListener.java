package me.classified.oneshot.listeners;

import me.classified.oneshot.OneShotCore;
import me.classified.oneshot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class JoinListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        e.setJoinMessage(Utils.color("&8[&a&l+&8] &a" + p.getName()));
        if(Bukkit.getServerName().equalsIgnoreCase("cannon")) {
            p.sendMessage(Utils.color(" "));
            p.sendMessage(Utils.color("&8&m----------------------------------------"));
            p.sendMessage(Utils.color(" "));
            p.sendMessage(Utils.color("    &f&lWelcome, &f" + p.getName() + " &f&lto &3&lONESHOT &b&lMC"));
            p.sendMessage(Utils.color("    &7Start your cannon training, build your bases, be creative!"));
            p.sendMessage(Utils.color("    &7Use /plot auto to claim a plot or /servers to switch servers."));
            p.sendMessage(Utils.color(" "));
            p.sendMessage(Utils.color("&8&l× &a&lSHOP: &7shop.oneshotmc.com"));
            p.sendMessage(Utils.color("&8&l× &e&lDISCORD: &7discord.oneshotmc.com"));
            p.sendMessage(Utils.color("&8&l× &b&lRECOMMENDED VERSION: &71.8.8"));
            p.sendMessage(Utils.color(" "));
            p.sendMessage(Utils.color("&8&l× &d&lSERVER VERSION: &72.5"));
            p.sendMessage(Utils.color(" "));
            p.sendMessage(Utils.color("&8&m----------------------------------------"));
        }
        p.sendMessage(Utils.color("&8&l(&a&l!&8&l) &aConnected to &7" + Bukkit.getServerName()));
        new BukkitRunnable(){
            @Override
            public void run() {
                p.setGameMode(GameMode.CREATIVE);
            }
        }.runTaskLater(OneShotCore.getInstance(), 2 * 20);
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        e.setQuitMessage(Utils.color("&8[&c&l-&8] &c" + p.getName()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerSpawn(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if(p.getWorld().getName().equalsIgnoreCase("world")){
          if(p.getLocation().getX() > 250 || p.getLocation().getX() < -250 || p.getLocation().getZ() > 250 || p.getLocation().getZ() < -250){
              p.performCommand("spawn");
          }
        }
    }

}
