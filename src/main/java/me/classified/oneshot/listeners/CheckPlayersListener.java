package me.classified.oneshot.listeners;
/*
 * AUTHOR: Classified
 * DISCORD: Classified#0001
 * DATE: 12/19/2020
 * PROJECT: OneShotCore
 */

import me.classified.oneshot.commands.CheckPlayersCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

public class CheckPlayersListener implements Listener {
    @EventHandler
    public void onCheck(PlayerInteractEvent e) {
        if (CheckPlayersCommand.activeCheck.containsKey(e.getPlayer())) {
            Player p = e.getPlayer();
            if ((e.getAction() == Action.LEFT_CLICK_AIR) || (e.getAction() == Action.LEFT_CLICK_BLOCK)) {
                if (CheckPlayersCommand.activeCheck.get(e.getPlayer()) > CheckPlayersCommand.online.size()-1) {
                    CheckPlayersCommand.activeCheck.remove(p);
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&3&l(!)&3 Check players is toggled &c&lOFF"
                    ));
                } else {
                    if (CheckPlayersCommand.activeCheck.get(e.getPlayer()) < 0) {
                        CheckPlayersCommand.activeCheck.replace(e.getPlayer(), 0);
                    }
                    p.teleport(CheckPlayersCommand.online.get(CheckPlayersCommand.activeCheck.get(p)));
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&3&l(!)&7 Teleported to &b" + CheckPlayersCommand.online.get(CheckPlayersCommand.activeCheck.get(p)).getName()
                    ));
                    CheckPlayersCommand.activeCheck.replace(p, CheckPlayersCommand.activeCheck.get(p)+1);
                }
            } else if ((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.LEFT_CLICK_BLOCK)) {
                if (CheckPlayersCommand.activeCheck.get(e.getPlayer()) > CheckPlayersCommand.online.size()-1) {
                    CheckPlayersCommand.activeCheck.replace(e.getPlayer(), CheckPlayersCommand.online.size()-1);
                }
                if (CheckPlayersCommand.activeCheck.get(e.getPlayer()) < 0) {
                    CheckPlayersCommand.online = new ArrayList<Player>(Bukkit.getOnlinePlayers());
                    CheckPlayersCommand.activeCheck.replace(p, CheckPlayersCommand.online.size()-1);
                }
                p.teleport(CheckPlayersCommand.online.get(CheckPlayersCommand.activeCheck.get(p)));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&3&l(!)&7 Teleported to &b" + CheckPlayersCommand.online.get(CheckPlayersCommand.activeCheck.get(p)).getName()
                ));
                CheckPlayersCommand.activeCheck.replace(p, CheckPlayersCommand.activeCheck.get(p)-1);
            }
            e.setCancelled(true);
        }
    }
}
