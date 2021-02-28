package me.classified.oneshot.utils;
/*
 * AUTHOR: Classified
 * DISCORD: Classified#0001
 * DATE: 8/23/2020
 * PROJECT: OneShotPatches
 */

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static String color(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String stripColor(String msg){
        return ChatColor.stripColor(msg);
    }

    public static int randomInt(int min, int max){
        Random rand = new Random();
        return rand.nextInt(max - min + 1) + min;
    }

    public static double randomDouble(double min, double max){
        Random r = new Random();
        return min + (max - min) * r.nextDouble();
    }

    public static String formatBalance(Double price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.00");
        return formatter.format(price);
    }

    public static boolean isProtected(Player p){
        RegionManager regionManager = WorldGuardPlugin.inst().getRegionManager(p.getWorld());
        ApplicableRegionSet regions = regionManager.getApplicableRegions(p.getLocation());
        for(ProtectedRegion protectedRegion : regions.getRegions()){
            if(protectedRegion.getId().equalsIgnoreCase("spawn")){
                return true;
            }
            return false;
        }
        return false;
    }

    public static boolean isSpawn(Player p){
        RegionManager regionManager = WorldGuardPlugin.inst().getRegionManager(p.getWorld());
        ApplicableRegionSet regions = regionManager.getApplicableRegions(p.getLocation());
        for(ProtectedRegion protectedRegion : regions.getRegions()){
            return protectedRegion.getId().equalsIgnoreCase("spawn");
        }
        return false;
    }

    public static String getDateAndTime(){
        DateFormat df = new SimpleDateFormat("MM/dd/yy hh:mm:ss a");
        Date d = new Date();
        String fd = df.format(d);
        String[] fds = fd.split(" ");
        if(fds[0].startsWith("0")){
            fds[0] = fds[0].replaceFirst("0", "");
        }
        if(fds[1].startsWith("0")){
            fds[1] = fds[1].replaceFirst("0", "");
        }
        String ffd = fds[0] + " at " + fds[1] + " " + fds[2];
        return ffd;
    }

    public static String getDate(){
        DateFormat df = new SimpleDateFormat("MM/dd/yy");
        Date d = new Date();
        String fd = df.format(d);
        String[] fds = fd.split(" ");
        if(fds[0].startsWith("0")){
            fds[0] = fds[0].replaceFirst("0", "");
        }
        String ffd = fds[0];
        return ffd;
    }

    public static boolean isInvEmpty(Player p){
        for(ItemStack item : p.getInventory().getContents()){
            if(item != null && !item.getType().equals(Material.AIR)){
                return false;
            }
        }
        return true;
    }

    public static boolean isArmorInvEmpty(Player p){
        for(ItemStack item : p.getInventory().getArmorContents()){
            if(item != null && !item.getType().equals(Material.AIR)){
                return false;
            }
        }
        return true;
    }

    public static int getAvaliableInvSlots(Player p){
        PlayerInventory pi = p.getInventory();
        int slots = 0;
        for(ItemStack is : pi){
            if(is == null || is.getType().equals(Material.AIR)){
                slots++;
            }
        }
        return slots;
    }

    public static String calculateCooldown(long seconds){
        int daysLeft = (int) TimeUnit.SECONDS.toDays(seconds);
        long hoursLeft = TimeUnit.SECONDS.toHours(seconds) - (daysLeft *24);
        long minutesLeft = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
        long secondsLeft = TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.SECONDS.toSeconds(TimeUnit.SECONDS.toMinutes(seconds) * 60);
        if(minutesLeft <= 0 && hoursLeft <= 0 && daysLeft <= 0){
            return secondsLeft + "s";
        }
        if(hoursLeft <= 0 && daysLeft <= 0){
            return minutesLeft + "m " + secondsLeft + "s";
        }
        if(daysLeft <= 0){
            return hoursLeft + "h " + minutesLeft + "m " + secondsLeft + "s";
        }
        else{
            return daysLeft + "d " + hoursLeft + "h " + minutesLeft + "m " + secondsLeft + "s";
        }
    }

    public static ItemStack itemBuilder(Material item, Short data, int amount, String name, String[] lore, boolean glowing){
        ArrayList<String> itemLore = new ArrayList<String>();
        ItemStack is = new ItemStack(item, amount, (short) data);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(color(name));
        for(String s : lore){
            itemLore.add(color(s));
        }
        im.setLore(itemLore);
        if(glowing){
            im.addEnchant(Enchantment.DURABILITY, 1, true);
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        is.setItemMeta(im);
        return is;
    }

    public static List<Player> getNearbyPlayers(Player player, int distance){
        List<Player> near = new ArrayList<>();
        int d2 = distance * distance;
        for (Player p : Bukkit.getServer().getOnlinePlayers()){
            if(p.getWorld() == player.getWorld() && p.getLocation().distanceSquared(player.getLocation()) <= d2){
                near.add(p);
                if(near.contains(player)){
                    near.remove(player);
                }
            }
        }
        return near;
    }


}
