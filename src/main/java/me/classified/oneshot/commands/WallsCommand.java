package me.classified.oneshot.commands;
/*
 * AUTHOR: Classified
 * DISCORD: Classified#0001
 * DATE: 1/7/2021
 * PROJECT: OneShotCore
 */

import com.intellectualcrafters.plot.object.Plot;
import me.classified.oneshot.OneShotCore;
import me.classified.oneshot.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import java.util.HashMap;
import java.util.UUID;

public class WallsCommand implements CommandExecutor {
    
    private final int WORLD_HEIGHT = 256;
    private final int MAX_WALLS = 100;
    private final int MAX_WALL_WIDTH = 50;
    
    public static HashMap<UUID, Long> cooldown = new HashMap<>();
    public static HashMap<UUID, Object> cache = new HashMap<>();
    public static HashMap<UUID, Object> metadata = new HashMap<>();
    public static HashMap<UUID, Object> startingPositions = new HashMap<>();
    public static HashMap<UUID, Object> wallData = new HashMap<>();
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player subject;
        if (command.getName().equalsIgnoreCase("walls") && sender instanceof Player) {
            subject = (Player)sender;
            if (subject.hasPermission("oneshot.walls")) {
                Plot plot = OneShotCore.getPlotAPI().getPlot(subject.getLocation());
                if (plot == null) {
                    subject.sendMessage(Utils.color("&c&l(!) &cYou must be in your plot to use this command."));
                    return false;
                }
                UUID uuid = subject.getUniqueId();
                if (!plot.isOwner(uuid)) {
                    subject.sendMessage(Utils.color("&c&l(!) &cYou must be the owner of the plot to use this command."));
                    return false;
                }
                if (!subject.hasPermission("oneshot.walls.bypass.cooldown")) {
                    if (cooldown.containsKey(subject.getUniqueId())) {
                        long secondsLeft = cooldown.get(subject.getUniqueId()) / 1000L + 60 - System.currentTimeMillis() / 1000L;
                        if (secondsLeft > 0L) {
                            subject.sendMessage(Utils.color("&c&l(!) &cThe /walls command is still on cooldown for another " + Utils.calculateCooldown(secondsLeft) + "s."));
                            return false;
                        } else {
                            getPlayerInput(subject, args);
                        }
                    } else {
                        getPlayerInput(subject, args);
                    }
                } else {
                    getPlayerInput(subject, args);
                }
            } else {
                subject.sendMessage(Utils.color("&c&l(!) &cYou do not have permission to use this command."));
            }
        } else if (command.getName().equalsIgnoreCase("undowalls") && sender instanceof Player) {
            subject = (Player)sender;
            if (subject.hasPermission("oneshot.walls.undowalls")) {
                World w = subject.getWorld();
                Material[][][] restoration = (Material[][][])cache.getOrDefault(subject.getUniqueId(), null);
                MaterialData[][][] states = (MaterialData[][][])metadata.getOrDefault(subject.getUniqueId(), null);
                Integer[] startingPos = (Integer[])startingPositions.getOrDefault(subject.getUniqueId(), null);
                for(int x = 0; x < restoration.length; ++x) {
                    for(int y = 0; y < restoration[x].length; ++y) {
                        for(int z = 0; z < restoration[x][y].length; ++z) {
                            Block target;
                            if (startingPos[3] == 1) {
                                target = w.getBlockAt(startingPos[0] + x, startingPos[1] + y, startingPos[2] + z);
                            } else if (startingPos[4] == 0) {
                                target = w.getBlockAt(startingPos[0] - x, startingPos[1] + y, startingPos[2] + z);
                            } else {
                                target = w.getBlockAt(startingPos[0] + x, startingPos[1] + y, startingPos[2] - z);
                            }
                            target.setType(restoration[x][y][z]);
                            target.getState().setData(states[x][y][z]);
                            target.getState().update();
                        }
                    }
                }
                subject.sendMessage(Utils.color("&6&l(!) &6Your most recent /walls command has been undone."));
            } else {
                subject.sendMessage(Utils.color("&c&l(!) &cYou do not have permission to use this command."));
            }
        }
        return true;
    }

    private void getPlayerInput(Player subject, String[] args) {
        if (args.length == 0) {
            subject.sendMessage(Utils.color("&c&l(!) &cUsage: &7/walls <amount> <width> <material>"));
        } else {
            int width = 1;
            Material type = Material.COBBLESTONE;
            int nWalls;
            try {
                nWalls = Integer.parseInt(args[0]);
            } catch (NumberFormatException var8) {
                nWalls = -1;
            }
            if (args.length >= 2) {
                try {
                    width = Integer.parseInt(args[1]);
                } catch (NumberFormatException var7) {
                    width = -1;
                }
            }
            if (args.length >= 3 ) {
                Material material = Material.getMaterial(args[2].toUpperCase());
                if(material == null){
                    if(StringUtils.isNumeric(args[2])){
                        int materialID = Integer.parseInt(args[2]);
                        material = Material.getMaterial(materialID);
                        if(material == null){
                            subject.sendMessage(Utils.color("&c&l(!) &cThe material type could not be found, please try again."));
                            return;
                        }
                        if(!material.isBlock()){
                            subject.sendMessage(Utils.color("&c&l(!) &cThe material is not a block, please try again."));
                            return;
                        }
                        if(material == Material.TNT){
                            subject.sendMessage(Utils.color("&c&l(!) &cThe material can not be used, please try again."));
                            return;
                        }
                    }
                    else {
                        subject.sendMessage(Utils.color("&c&l(!) &cThe material type could not be found, please try again."));
                        return;
                    }
                }
                if(!material.isBlock()){
                    subject.sendMessage(Utils.color("&c&l(!) &cThe material is not a block, please try again."));
                    return;
                }
                if(material == Material.TNT){
                    subject.sendMessage(Utils.color("&c&l(!) &cThe material can not be used, please try again."));
                    return;
                }
                type = material;
            }
            if (nWalls > 0 && width > 0) {
                if (nWalls > MAX_WALLS && !subject.hasPermission("oneshot.walls.bypass.limit")) {
                    subject.sendMessage(Utils.color("&c&l(!) &cYou can not generate more than 100 walls at a time."));
                } else if (width > MAX_WALL_WIDTH && !subject.hasPermission("oneshot.walls.bypass.width")) {
                    subject.sendMessage(Utils.color("&c&l(!) &cYou can not generate walls wider than 50 blocks."));
                } else if (generateWalls(nWalls, width, type, subject)) {
                    subject.sendMessage(Utils.color("&a&l(!) &aYou have successfully generated " + nWalls + "x " + type.toString().toUpperCase() + " walls."));
                    cooldown.put(subject.getUniqueId(), System.currentTimeMillis());
                } else {
                    subject.sendMessage(Utils.color("&c&l(!) &cYou are too close to the world border to generate walls."));
                }
            } else {
                subject.sendMessage(Utils.color("&c&l(!) &cUsage: &7/walls <amount> <width> <material>"));
            }
        }

    }

    private boolean generateWalls(int nWalls, int width, Material type, Player subject) {
        Integer[] info = (Integer[])wallData.getOrDefault(subject.getUniqueId(), null);
        int isCobble = Material.COBBLESTONE == type ? 0 : 1;
        int wallWidth = width + width % 2 - 1;
        if (info != null) {
            info[0] = nWalls;
            info[1] = wallWidth;
            info[2] = isCobble;
            wallData.put(subject.getUniqueId(), info);
        } else {
            wallData.put(subject.getUniqueId(), new Integer[]{nWalls, width, isCobble});
        }
        World w = subject.getWorld();
        Location l = subject.getLocation();
        int x = l.getBlockX();
        int z = l.getBlockZ();
        double dir = (double)l.getYaw();
        boolean isFacingZ = true;
        int wallGap = 2;
        if (dir >= 45.0D && dir <= 135.0D || dir >= -135.0D && dir <= -45.0D || dir >= 225.0D && dir <= 315.0D) {
            isFacingZ = false;
        }
        if (dir >= 45.0D && dir <= 225.0D || dir <= -135.0D && dir >= -225.0D) {
            wallGap = -2;
        }
        boolean foundBorder = false;
        int i;
        if (isFacingZ) {
            if (wallGap > 0) {
                for(i = 0; i < nWalls * 2 + 2; ++i) {
                    if (Material.BEDROCK == w.getBlockAt(x, 1, z + i).getType()) {
                        foundBorder = true;
                        break;
                    }
                }
                if (!foundBorder) {
                    for(i = -1 * wallWidth / 2 - 1; i < wallWidth / 2 + 1; ++i) {
                        if (Material.BEDROCK == w.getBlockAt(x + i, 1, z).getType()) {
                            foundBorder = true;
                            break;
                        }
                    }
                }
            } else {
                for(i = 0; i < nWalls * 2 + 2; ++i) {
                    if (Material.BEDROCK == w.getBlockAt(x, 1, z - i).getType()) {
                        foundBorder = true;
                        break;
                    }
                }
                if (!foundBorder) {
                    for(i = -1 * wallWidth / 2 - 1; i < wallWidth / 2 + 1; ++i) {
                        if (Material.BEDROCK == w.getBlockAt(x + i, 1, z).getType()) {
                            foundBorder = true;
                            break;
                        }
                    }
                }
            }
        } else if (wallGap > 0) {
            for(i = 0; i < nWalls * 2 + 2; ++i) {
                if (Material.BEDROCK == w.getBlockAt(x + i, 1, z).getType()) {
                    foundBorder = true;
                    break;
                }
            }
            if (!foundBorder) {
                for(i = -1 * wallWidth / 2 - 1; i < wallWidth / 2 + 1; ++i) {
                    if (Material.BEDROCK == w.getBlockAt(x, 1, z + i).getType()) {
                        foundBorder = true;
                        break;
                    }
                }
            }
        } else {
            for(i = 0; i < nWalls * 2 + 2; ++i) {
                if (Material.BEDROCK == w.getBlockAt(x - i, 1, z).getType()) {
                    foundBorder = true;
                    break;
                }
            }
            if (!foundBorder) {
                for(i = -1 * wallWidth / 2 - 1; i < wallWidth / 2 + 1; ++i) {
                    if (Material.BEDROCK == w.getBlockAt(x, 1, z + i).getType()) {
                        foundBorder = true;
                        break;
                    }
                }
            }
        }
        if (foundBorder) {
            return false;
        } else {
            Material[][][] area;
            MaterialData[][][] data;
            if (isFacingZ) {
                area = new Material[wallWidth][255][nWalls * 2];
                data = new MaterialData[wallWidth][255][nWalls * 2];
                z += wallGap;
                x -= wallWidth / 2;
                startingPositions.put(subject.getUniqueId(), new Integer[]{x, 1, z - wallGap / 2, wallGap / 2, 1});
            } else {
                area = new Material[nWalls * 2][255][wallWidth];
                data = new MaterialData[nWalls * 2][255][wallWidth];
                x += wallGap;
                z -= wallWidth / 2;
                startingPositions.put(subject.getUniqueId(), new Integer[]{x - wallGap / 2, 1, z, wallGap / 2, 0});
            }
            for(i = 0; i < nWalls; ++i) {
                for(int j = 0; j < wallWidth; ++j) {
                    for(int y = 1; y < 255; ++y) {
                        generateBlock(x, y, z, i, j, w, type, isFacingZ, wallGap, area, data);
                    }
                    generateBlock(x, 255, z, i, j, w, Material.WATER, isFacingZ, wallGap, area, data);
                    if (isFacingZ) {
                        ++x;
                    } else {
                        ++z;
                    }
                }
                if (isFacingZ) {
                    x -= wallWidth;
                    z += wallGap;
                } else {
                    z -= wallWidth;
                    x += wallGap;
                }
            }
            cache.put(subject.getUniqueId(), area);
            metadata.put(subject.getUniqueId(), data);
            return true;
        }
    }

    private void generateBlock(int x, int y, int z, int wallIndex, int widthIndex, World w, Material type, boolean isFacingZ, int wallGap, Material[][][] area, MaterialData[][][] data) {
        Block block = w.getBlockAt(x, y, z);
        Material blockType = block.getType();
        MaterialData blockData = block.getState().getData();
        block.setType(type);
        Block gap;
        Material gapType;
        MaterialData gapData;
        if (isFacingZ) {
            gap = w.getBlockAt(x, y, z - wallGap / 2);
            gapType = gap.getType();
            gapData = gap.getState().getData();
            area[widthIndex][y - 1][wallIndex * 2 + 1] = blockType;
            area[widthIndex][y - 1][wallIndex * 2] = gapType;
            data[widthIndex][y - 1][wallIndex * 2 + 1] = blockData;
            data[widthIndex][y - 1][wallIndex * 2] = gapData;
        } else {
            gap = w.getBlockAt(x - wallGap / 2, y, z);
            gapType = gap.getType();
            gapData = gap.getState().getData();
            area[wallIndex * 2 + 1][y - 1][widthIndex] = blockType;
            area[wallIndex * 2][y - 1][widthIndex] = gapType;
            data[wallIndex * 2 + 1][y - 1][widthIndex] = blockData;
            data[wallIndex * 2][y - 1][widthIndex] = gapData;
        }
        gap.setType(Material.AIR);
    }


}
