package me.classified.oneshot.pvp;
/*
 * AUTHOR: Classified
 * DISCORD: Classified#0001
 * DATE: 1/7/2021
 * PROJECT: OneShotCore
 */

import me.classified.oneshot.utils.ItemBuilder;
import me.classified.oneshot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.HashMap;

public class PvPCommand implements CommandExecutor {

    public static ArrayList<Player> pvpEnabled = new ArrayList<>();

    public static ArrayList<Player> getPvpEnabled() {
        return pvpEnabled;
    }

    public static HashMap<Player, ItemStack[]> inventoryContent = new HashMap<>();
    public static HashMap<Player, ItemStack[]> armorContent = new HashMap<>();

    public static HashMap<Player, ItemStack[]> getInventoryContent() {
        return inventoryContent;
    }

    public static HashMap<Player, ItemStack[]> getArmorContent() {
        return armorContent;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(command.getName().equalsIgnoreCase("pvp")) {
            if(!(sender instanceof Player)){
                return false;
            }
            Player p = (Player) sender;
            if(!p.getWorld().getName().equalsIgnoreCase("world")){
                p.sendMessage(Utils.color("&c&l(!) &cYou can only use /pvp while you are at spawn."));
                return false;
            }
            if (getPvpEnabled().contains(p)) {
                getPvpEnabled().remove(p);
                PvPListener.getPvpKillstreak().remove(p);
                p.getInventory().setContents(inventoryContent.get(p));
                p.getInventory().setArmorContents(armorContent.get(p));
                p.sendMessage(Utils.color("&8&l(&c&lPVP&8&l) &7PvP Mode has been &c&l&nDISABLED&7."));
                p.performCommand("spawn");
                p.setGameMode(GameMode.CREATIVE);
                p.playSound(p.getLocation(), Sound.BLAZE_DEATH, 1, 1);
            }
            else {
                inventoryContent.put(p, p.getInventory().getContents());
                armorContent.put(p, p.getInventory().getArmorContents());
                p.getInventory().clear();
                ItemStack ironHelmet = new ItemStack(Material.IRON_HELMET);
                ItemMeta ironHelmetMeta = ironHelmet.getItemMeta();
                ironHelmetMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
                ironHelmetMeta.addEnchant(Enchantment.DURABILITY, 15, true);
                ironHelmet.setItemMeta(ironHelmetMeta);

                ItemStack ironChestplate = new ItemStack(Material.IRON_CHESTPLATE);
                ItemMeta ironChestplateMeta = ironChestplate.getItemMeta();
                ironChestplateMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
                ironChestplateMeta.addEnchant(Enchantment.DURABILITY, 15, true);
                ironChestplate.setItemMeta(ironChestplateMeta);

                ItemStack ironLeggings = new ItemStack(Material.IRON_LEGGINGS);
                ItemMeta ironLeggingsMeta = ironLeggings.getItemMeta();
                ironLeggingsMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
                ironLeggingsMeta.addEnchant(Enchantment.DURABILITY, 15, true);
                ironLeggings.setItemMeta(ironLeggingsMeta);

                ItemStack ironBoots = new ItemStack(Material.IRON_BOOTS);
                ItemMeta ironBootsMeta = ironBoots.getItemMeta();
                ironBootsMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
                ironBootsMeta.addEnchant(Enchantment.DURABILITY, 15, true);
                ironBoots.setItemMeta(ironBootsMeta);

                ItemStack sword = new ItemBuilder(Material.DIAMOND_SWORD).setGlow().setName("&c&lPvP Sword").setLore("&8&l* &3&lIN ALPHA &8&l*").build();

                p.getInventory().setArmorContents(new ItemStack[]{ironBoots, ironLeggings, ironChestplate, ironHelmet});
                p.getInventory().setItem(0, sword);
                p.getInventory().setItem(1, new ItemStack(Material.COOKED_BEEF, 64));
                p.getInventory().setHeldItemSlot(0);
                p.setGameMode(GameMode.SURVIVAL);
                p.setHealth(20);
                p.setSaturation(40);
                p.setWalkSpeed(0.2f);
                p.performCommand("undisguise");
                p.performCommand("spawn");
                for(PotionEffect effect : p.getActivePotionEffects()){
                    p.removePotionEffect(effect.getType());
                }
                if(p.hasPermission("essentials.god")){
                    p.performCommand("god off");
                }
                if(p.hasPermission("essentials.vanish")){
                    p.performCommand("vanish off");
                }
                p.sendMessage(Utils.color("&8&l(&c&lPVP&8&l) &7PvP Mode has been &a&l&nENABLED&7. &7(&c&lNOTE: &7PvP is in beta so there may be bugs!)"));
                p.playSound(p.getLocation(), Sound.HORSE_ARMOR, 1, 1);
                getPvpEnabled().add(p);
            }
        }
        return false;
    }
}
