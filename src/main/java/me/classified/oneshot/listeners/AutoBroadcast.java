package me.classified.oneshot.listeners;

import me.classified.oneshot.OneShotCore;
import me.classified.oneshot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class AutoBroadcast implements Listener {

    public AutoBroadcast(){
        startAutoBroadcast();
    }

    static HashMap<Integer, String> broadcasts = new HashMap<Integer, String>();
    static int id = 0;

    public static void startAutoBroadcast(){
        broadcasts.put(0, "&3Join the official OneShot MC Discord! Join now: &bdiscord.oneshotmc.com&3.");
        broadcasts.put(1, "&bCheck out our new server store at &3shop.oneshotmc.com &bwhere you can purchase ranks and other perks.");
        broadcasts.put(2, "&3Did you know that OneShot MC is releasing a factions server? Find more info at &bdiscord.oneshotmc.com&3.");
        broadcasts.put(3, "&bYou can use the following protection blocks to avoid having to water your cannon: Bedrock, Emerald Block, Gold Block (Shows Debug).");

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(OneShotCore.getInstance(), new Runnable(){

            public void run(){
                Bukkit.broadcastMessage(Utils.color( " "));
                Bukkit.broadcastMessage(Utils.color("&3&l(&b&l!&3&l) " + broadcasts.get(id)));
                Bukkit.broadcastMessage(Utils.color(" "));

                if(id == (broadcasts.size() - 1)){
                    id = 0;
                }
                else{
                    id++;
                }
            }

        }, 20L, 600 * 20L);
    }

}
