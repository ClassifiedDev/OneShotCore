package me.classified.oneshot.listeners;

import me.classified.oneshot.OneShotCore;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherHandler implements Listener {

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e){
        e.setCancelled(true);
    }

    public static void alwaysDay(){
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(OneShotCore.getInstance(), new Runnable(){
            public void run() {
                Bukkit.getServer().getWorld("world").setTime(0);
                Bukkit.getServer().getWorld("plots").setTime(0);
            }
        }, 0L, 12000L);
    }


}
