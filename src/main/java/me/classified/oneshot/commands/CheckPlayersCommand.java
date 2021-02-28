package me.classified.oneshot.commands;
/*
 * AUTHOR: Classified
 * DISCORD: Classified#0001
 * DATE: 8/31/2020
 * PROJECT: OneShotCore
 */

import me.classified.oneshot.OneShotCore;
import me.classified.oneshot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CheckPlayersCommand implements CommandExecutor {

    //ArrayList<Player> stop = new ArrayList<>();
   // HashMap<Player, ArrayList<Player>> checkingPlayers = new HashMap<>();

    /*public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(!(sender instanceof Player)){
            return false;
        }
        Player p = (Player) sender;
        if(cmd.getName().equalsIgnoreCase("checkplayers")){
            if(!p.hasPermission("oneshot.staff")) return false;
            p.performCommand("vanish on");
            if(checkingPlayers.containsKey(p)){
                stop.add(p);
                p.sendMessage(Utils.color("&c&l(!) &cStopping check players."));
                return false;
            }
            ArrayList<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
            checkingPlayers.put(p, players);
            new BukkitRunnable(){
                @Override
                public void run() {
                    if(stop.contains(p)){
                        checkingPlayers.remove(p);
                        cancel();
                        return;
                    }
                    if(checkingPlayers.get(p).isEmpty()){
                        p.sendMessage(Utils.color("&c&l(!) &cCheck all players, stopping check players."));
                        cancel();
                        return;
                    }
                    p.teleport(checkingPlayers.get(p).get(0));
                    p.sendMessage(Utils.color("&b&l(!) &bNow checking player: &7" + checkingPlayers.get(p).get(0).getName()));
                    p.playSound(p.getLocation(), Sound.NOTE_PLING, 10, 1);
                    checkingPlayers.get(p).remove(0);
                }
            }.runTaskTimer(OneShotCore.getInstance(), 20, 5 * 20);
            p.sendMessage(Utils.color("&a&l(!) &aStaring to check all online players. Use /checkplayers again to stop."));
        }
        return false;
    }*/

    public static HashMap<Player, Integer> activeCheck = new HashMap<>();
    public static ArrayList<Player> online = new ArrayList<>(Bukkit.getOnlinePlayers());

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            online = new ArrayList<Player>(Bukkit.getOnlinePlayers());
            if (cmd.getName().equalsIgnoreCase("checkplayers") && p.hasPermission("oneshot.staff")) {
                p.performCommand("vanish on");
                if (activeCheck.containsKey(p)) {
                    activeCheck.remove(p);
                    p.sendMessage(Utils.color("&3&l(!) &3Check players is toggled &c&lOFF&3."));
                } else {
                    activeCheck.put(p, 0);
                    p.sendMessage(Utils.color("&3&l(!) &3Check players is toggled &a&lON&3."));
                }
            }
        }

        return true;
    }

}
