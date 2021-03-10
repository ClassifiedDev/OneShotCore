package me.classified.oneshot;
/*
 * AUTHOR: Classified
 * DISCORD: Classified#0001
 * DATE: 8/23/2020
 * PROJECT: OneShotCore
 */

//import com.intellectualcrafters.plot.api.PlotAPI;
//import com.plotsquared.core.api.PlotAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.eventbus.Subscribe;
import me.classified.oneshot.chatfilter.ChatFilter;
import me.classified.oneshot.chatfilter.ChatFilterListener;
import me.classified.oneshot.commands.*;
import me.classified.oneshot.listeners.*;
import me.classified.oneshot.pvp.PvPCommand;
import me.classified.oneshot.pvp.PvPListener;
import me.classified.oneshot.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.defaults.ListCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Boat;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class OneShotCore extends JavaPlugin {

    private static File configFile;
    private static FileConfiguration config;
    private static Plugin instance;
    public static WorldEditPlugin wep;
    //private static PlotAPI plotAPI;

    public void onEnable(){
        PluginDescriptionFile pdfFile = getDescription();
        Logger logger = getLogger();
        instance = this;
        wep = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        generateConfigs();
        registerCommands();
        registerEvents();
        new ChatFilter();
        fixEntityEdits();
        logger.info(pdfFile.getName() + " has been ENABLED " + pdfFile.getVersion() + " (Developed by: " + pdfFile.getAuthors() + ")");
    }

    public void onDisable(){
        PluginDescriptionFile pdfFile = getDescription();
        Logger logger = getLogger();
        logger.info(pdfFile.getName() + " has been DISABLED " + pdfFile.getVersion() + " (Developed by: " + pdfFile.getAuthors() + ")");
    }

    public static Plugin getInstance(){
        return instance;
    }

    private void registerCommands(){
        getCommand("buy").setExecutor(new BuyCommand());
        getCommand("pvp").setExecutor(new PvPCommand());
        getCommand("walls").setExecutor(new WallsCommand());
        getCommand("xray").setExecutor(new XRayCommand(this));
        getCommand("poke").setExecutor(new PokeCommand());
        //getCommand("upload").setExecutor(new UploadCommand());
        getCommand("rename").setExecutor(new RenameCommand());
        getCommand("announce").setExecutor(new AnnounceCommand());
        getCommand("toggletnt").setExecutor(new ToggleTnTCommand());
        getCommand("checkplayers").setExecutor(new CheckPlayersCommand());
        getCommand("corner").setExecutor(new CornerCommand());
    }

    private void registerEvents(){
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new PvPListener(), this);
        //pm.registerEvents(new HelpCommand(), this);
        pm.registerEvents(new JoinListener(), this);
        pm.registerEvents(new AutoBroadcast(), this);
        pm.registerEvents(new WeatherHandler(), this);
        pm.registerEvents(new BlockedCommands(), this);
        pm.registerEvents(new ExplosionListener(), this);
        pm.registerEvents(new ChatFilterListener(), this);
        pm.registerEvents(new BoatExploitListener(), this);
        pm.registerEvents(new CheckPlayersListener(), this);
    }

    public static void fixEntityEdits() {
        OneShotCore.wep.getWorldEdit().getEventBus().register(new Object() {
            @Subscribe
            public void onEditSessionEvent(EditSessionEvent event) {
                if (event.getStage() == EditSession.Stage.BEFORE_CHANGE) {
                    try {
                        LocalSession s = OneShotCore.wep.getSession(Bukkit.getServer().getPlayer(event.getActor().getUniqueId()));
                        Clipboard c = s.getClipboard().getClipboard();
                        for (Entity en : c.getEntities()) { en.remove(); }
                        ClipboardHolder ch = (ClipboardHolder) c;
                        s.setClipboard(ch);
                    } catch (Exception ignored) {}
                }
            }
        });
    }

//    public static PlotAPI getPlotAPI() {
//        return plotAPI;
//    }

    private void generateConfigs(){
        File fileFile;
        FileConfiguration file;
        saveDefaultConfig();

        configFile = new File(getDataFolder(), "config.yml");
        config = new YamlConfiguration();

        fileFile = configFile;
        file = config;

        if (!fileFile.exists()){
            if(fileFile.getParentFile().mkdirs()) saveResource(fileFile.getName(), false);
        }
        try{
            file.load(fileFile);
        }
        catch (IOException | InvalidConfigurationException e){
            e.printStackTrace();
        }
    }

    public static FileConfiguration getConfigFile(){
        return config;
    }

    public static void saveConfigFile(){
        try{
            config.save(configFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void reloadConfigFile(){
        config = YamlConfiguration.loadConfiguration(configFile);
        saveConfigFile();
    }


}
