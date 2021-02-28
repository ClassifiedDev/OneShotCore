package me.classified.oneshot.commands;
/*
 * AUTHOR: Classified
 * DISCORD: Classified#0001
 * DATE: 9/29/2020
 * PROJECT: OneShotCore
 */

import me.classified.oneshot.OneShotCore;
import me.classified.oneshot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XRayCommand implements CommandExecutor, Listener {

    public XRayCommand(OneShotCore plugin){
        loadIgnoredBlocks();
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private List<Material> ignoredBlocks = new ArrayList<>();
    private HashMap<Player, List<Block>> activeXray = new HashMap<>();

    public void loadIgnoredBlocks(){
        ignoredBlocks.add(Material.BEDROCK);
        ignoredBlocks.add(Material.REDSTONE);
        ignoredBlocks.add(Material.DIODE_BLOCK_ON);
        ignoredBlocks.add(Material.DIODE_BLOCK_OFF);
        ignoredBlocks.add(Material.DIODE);
        ignoredBlocks.add(Material.REDSTONE_COMPARATOR_ON);
        ignoredBlocks.add(Material.REDSTONE_COMPARATOR_OFF);
        ignoredBlocks.add(Material.REDSTONE_COMPARATOR);
        ignoredBlocks.add(Material.REDSTONE_TORCH_OFF);
        ignoredBlocks.add(Material.REDSTONE_TORCH_ON);
        ignoredBlocks.add(Material.REDSTONE_WIRE);
        ignoredBlocks.add(Material.REDSTONE_BLOCK);
        ignoredBlocks.add(Material.PISTON_BASE);
        ignoredBlocks.add(Material.PISTON_MOVING_PIECE);
        ignoredBlocks.add(Material.PISTON_STICKY_BASE);
        ignoredBlocks.add(Material.PISTON_EXTENSION);
        ignoredBlocks.add(Material.LEVER);
        ignoredBlocks.add(Material.STONE_BUTTON);
        ignoredBlocks.add(Material.EMERALD_BLOCK);
        ignoredBlocks.add(Material.DIAMOND_BLOCK);
        ignoredBlocks.add(Material.GOLD_BLOCK);
        ignoredBlocks.add(Material.GLOWSTONE);
        ignoredBlocks.add(Material.STONE_SLAB2);
        ignoredBlocks.add(Material.STEP);
        ignoredBlocks.add(Material.DOUBLE_STEP);
        ignoredBlocks.add(Material.CARPET);
        ignoredBlocks.add(Material.GRAVEL);
        ignoredBlocks.add(Material.SAND);
        ignoredBlocks.add(Material.AIR);
        ignoredBlocks.add(Material.TRAP_DOOR);
        ignoredBlocks.add(Material.IRON_TRAPDOOR);
        ignoredBlocks.add(Material.COBBLESTONE_STAIRS);
        ignoredBlocks.add(Material.QUARTZ_STAIRS);
        ignoredBlocks.add(Material.SMOOTH_STAIRS);
        ignoredBlocks.add(Material.LADDER);
    }

    public List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    if(!ignoredBlocks.contains(location.getWorld().getBlockAt(x, y, z).getType())) {
                        blocks.add(location.getWorld().getBlockAt(x, y, z));
                    }
                }
            }
        }
        return blocks;
    }

    @EventHandler
    public void onXRayQuit(PlayerQuitEvent e){
        if(activeXray.containsKey(e.getPlayer())){
            activeXray.remove(e.getPlayer());
        }
    }

    @EventHandler
    public void onXrayBlockPlace(BlockPlaceEvent e){
        if(activeXray.containsKey(e.getPlayer())){
            e.setCancelled(true);
            e.getPlayer().sendMessage(Utils.color("&c&l(!) &cYou can not place or break block while X-Ray mode is active!"));
        }
    }

    @EventHandler
    public void onXrayBlockBreak(BlockBreakEvent e){
        if(activeXray.containsKey(e.getPlayer())){
            e.setCancelled(true);
            e.getPlayer().sendMessage(Utils.color("&c&l(!) &cYou can not place or break block while X-Ray mode is active!"));
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(!(sender instanceof Player)){ return false; }
        Player p = (Player) sender;
        if(cmd.getName().equalsIgnoreCase("xray")){
            if(!activeXray.containsKey(p)){
                activeXray.put(p, getNearbyBlocks(p.getLocation(), 32));
                for(Block b : activeXray.get(p)) {
                    p.sendBlockChange(b.getLocation(), Material.BARRIER, (byte) 1);
                }
                p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 10, 2);
                p.sendMessage(Utils.color("&3&l(!) &3X-Ray mode has been &a&lACTIVATED&3!"));
            }
            else {
                for(Block b : activeXray.get(p)) {
                    p.sendBlockChange(b.getLocation(), b.getType(), b.getData());
                }
                activeXray.remove(p);
                p.playSound(p.getLocation(), Sound.BLAZE_DEATH, 10, 2);
                p.sendMessage(Utils.color("&3&l(!) &3X-Ray mode has been &c&lDEACTIVATED&3!"));
            }
        }
        return false;
    }

}
