package me.classified.oneshot.commands;

import com.intellectualcrafters.plot.object.Location;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotPlayer;
import me.classified.oneshot.utils.Utils;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CornerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) { return false; }
        Player p = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("corner")) {
            try {
                PlotPlayer plotPlayer = PlotPlayer.wrap(p);
                Plot plot = Plot.getPlot(plotPlayer.getLocation());
                List<Location> corners = plot.getAllCorners();
                if (plot.isOwner(plotPlayer.getUUID())) {
                    if (args.length == 0) {
                        Location location = corners.get(0);
                        location.add(0, 10, 0);
                        plotPlayer.teleport(location);
                        p.sendMessage(Utils.color("&3&l(!)&3 You were teleported to a corner of your plot"));
                        p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
                    } else if (args.length == 1) {
                        try {
                            Location location = corners.get(Integer.parseInt(args[0]) - 1);
                            location.add(0, 10, 0);
                            plotPlayer.teleport(location);
                            p.sendMessage(Utils.color("&3&l(!)&3 You were teleported to a corner of your plot"));
                            p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
                        } catch (NullPointerException | IndexOutOfBoundsException | NumberFormatException ex) {
                            p.sendMessage(Utils.color("&c&l(!)&c Invalid corner ID"));
                        }
                    }

                } else {
                    p.sendMessage(Utils.color("&c&l(!)&c You can only teleport to your own plot corners"));
                }
            } catch (NullPointerException ex) { p.sendMessage(Utils.color("&c&l(!)&c You can only teleport to your own plot corners")); }

        }
        return false;
    }
}
