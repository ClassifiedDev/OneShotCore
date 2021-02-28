package me.classified.oneshot.pvp;
/*
 * AUTHOR: Classified
 * DISCORD: Classified#0001
 * DATE: 1/7/2021
 * PROJECT: OneShotCore
 */

import me.classified.oneshot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class PvPListener implements Listener {

    private static HashMap<Player, Integer> pvpKillstreak = new HashMap<>();

    public static HashMap<Player, Integer> getPvpKillstreak() {
        return pvpKillstreak;
    }

    @EventHandler
    public void respawn(PlayerRespawnEvent e){
        Player p = e.getPlayer();
        PlayerInventory pi = p.getInventory();
        p.setGameMode(GameMode.CREATIVE);
        p.setHealth(20);
        p.setSaturation(40);
        pi.clear();
        if(PvPCommand.getPvpEnabled().contains(p)){
            PvPCommand.getPvpEnabled().remove(p);
            p.getInventory().setContents(PvPCommand.getInventoryContent().get(p));
            pi.setArmorContents(new ItemStack[]{new ItemStack(Material.AIR, 1), new ItemStack(Material.AIR, 1), new ItemStack(Material.AIR, 1), new ItemStack(Material.AIR, 1)});
            p.getInventory().setArmorContents(PvPCommand.getArmorContent().get(p));
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        Player p = e.getEntity();
        e.getDrops().clear();
        if (PvPCommand.getPvpEnabled().contains(p.getKiller())){
            ItemStack diamondHelmet = new ItemStack(Material.DIAMOND_HELMET);
            ItemMeta diamondHelmetMeta = diamondHelmet.getItemMeta();
            diamondHelmetMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
            diamondHelmetMeta.addEnchant(Enchantment.DURABILITY, 15, true);
            diamondHelmet.setItemMeta(diamondHelmetMeta);

            ItemStack diamondChestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
            ItemMeta diamondChestplateMeta = diamondChestplate.getItemMeta();
            diamondChestplateMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
            diamondChestplateMeta.addEnchant(Enchantment.DURABILITY, 15, true);
            diamondChestplate.setItemMeta(diamondChestplateMeta);

            ItemStack diamondLeggings = new ItemStack(Material.DIAMOND_LEGGINGS);
            ItemMeta diamondLeggingsMeta = diamondLeggings.getItemMeta();
            diamondLeggingsMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
            diamondLeggingsMeta.addEnchant(Enchantment.DURABILITY, 15, true);
            diamondLeggings.setItemMeta(diamondLeggingsMeta);

            ItemStack diamondBoots = new ItemStack(Material.DIAMOND_BOOTS);
            ItemMeta diamondBootsMeta = diamondBoots.getItemMeta();
            diamondBootsMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
            diamondBootsMeta.addEnchant(Enchantment.DURABILITY, 15, true);
            diamondBoots.setItemMeta(diamondBootsMeta);

            if(getPvpKillstreak().containsKey(p.getKiller())){
                getPvpKillstreak().put(p.getKiller(), getPvpKillstreak().get(p.getKiller()) + 1);
                p.getKiller().setHealth(20);
                for(Player player : PvPCommand.getPvpEnabled()){
                    player.sendMessage(Utils.color("&8&l(&c&lPVP&8&l) &c" + p.getName() + " &7has been killed by &c" + p.getKiller().getName() + " &7(Kill Streak: &c"+ getPvpKillstreak().get(p.getKiller())+"&7)"));
                }
                if(getPvpKillstreak().get(p.getKiller()) == 5){
                    for(Player player : PvPCommand.getPvpEnabled()) {
                        player.sendMessage(Utils.color("&8&l(&c&lPVP&8&l) &c" + p.getKiller().getName() + " &7is on a &c&l5 kill &7streak. They now have &b&lDIAMOND ARMOR&7!"));
                    }
                    p.getKiller().getInventory().setArmorContents(new ItemStack[]{diamondBoots, diamondLeggings, diamondChestplate, diamondHelmet});
                    p.getKiller().playSound(p.getKiller().getLocation(), Sound.HORSE_ARMOR, 10, 1);
                }
            }
            else {
                getPvpKillstreak().put(p.getKiller(), 1);
                p.getKiller().setHealth(20);
                for(Player player : PvPCommand.getPvpEnabled()) {
                    player.sendMessage(Utils.color("&8&l(&c&lPVP&8&l) &c" + p.getName() + " &7has been killed by &c" + p.getKiller().getName() + " &7(Kill Streak: &c"+ getPvpKillstreak().get(p.getKiller())+"&7)"));
                }
            }
            if(getPvpKillstreak().containsKey(p)){
                getPvpKillstreak().put(p, 0);
            }
            p.spigot().respawn();
            p.sendMessage(Utils.color("&8&l(&c&lPVP&8&l) &7PvP Mode has been &c&l&nDISABLED&7 due to your death. Type /pvp to continue to PvP!"));
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e){
        if ((e.getDamager() instanceof Player)) {
            if ((e.getEntity() instanceof Player)) {
                Player target = (Player) e.getEntity();
                Player attacker = (Player) e.getDamager();
                 if(target.getWorld().getName().equalsIgnoreCase("world")){
                     if(PvPCommand.getPvpEnabled().contains(target)){
                         if(PvPCommand.getPvpEnabled().contains(attacker)) {
                             return;
                         }
                         e.setCancelled(true);
                     }
                 }
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(PlayerQuitEvent e){
        Player p = e.getPlayer();
        if(PvPCommand.getPvpEnabled().contains(p)){
            getPvpKillstreak().remove(p);
            PvPCommand.getPvpEnabled().remove(p);
            p.getInventory().setContents(PvPCommand.getInventoryContent().get(p));
            p.getInventory().setArmorContents(PvPCommand.getArmorContent().get(p));
        }
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e){
        Player p = e.getPlayer();
        if(PvPCommand.getPvpEnabled().contains(p)){
            if(p.hasPermission("oneshot.staff.helper")) return;
            if(!e.getMessage().equalsIgnoreCase("/pvp")){
                e.setCancelled(true);
                p.sendMessage(Utils.color("&c&l(!) &cThat command is disabled while in PvP mode, type /pvp to disable PvP mode."));
            }
        }
    }

}
