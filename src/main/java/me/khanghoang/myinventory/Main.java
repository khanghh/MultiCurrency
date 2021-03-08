package me.khanghoang.myinventory;

import me.khanghoang.myinventory.commands.AdminCommand;
import me.khanghoang.myinventory.commands.PlayerCommand;
import me.khanghoang.myinventory.config.Configuration;
import me.khanghoang.myinventory.listeners.InventoryEventListener;
import me.khanghoang.myinventory.listeners.PlayerEventListener;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;

public class Main extends JavaPlugin {

    
    private static Main instance;
    public static Main getInstance() {
        return instance;
    }
    
    private Configuration config;
    private MyInventoryManager invManager;

    @Override
    public Configuration getConfig() {
        return config;
    }

    public MyInventoryManager getInventoryManager() {
        return invManager;
    }

    public Configuration loadConfig(String fileName) {
        File configFile = new File(getDataFolder(), fileName);
        if (!configFile.exists()) {
            saveDefaultConfig();
            return Configuration.loadFromFile(configFile);
        } else {
            Configuration oldConfig = Configuration.loadFromFile(configFile);
            InputStream stream = getResource(fileName);
            if (stream != null) {
                Configuration newConfig = new Configuration(configFile);
                newConfig.loadFromStream(stream);
                newConfig.migrate(oldConfig);
                newConfig.save();
                return newConfig;
            }
            return oldConfig;
        }
    }

    @Override
    public void onEnable() {
        instance = this;
        this.config = loadConfig("config.yml");
        this.invManager = new MyInventoryManager(this);
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerEventListener(this), this);
        pluginManager.registerEvents(new InventoryEventListener(this), this);
        getCommand("myinv").setExecutor(new AdminCommand(this));
        getCommand("kd").setExecutor(new PlayerCommand(this));
    }

    @Override
    public void onDisable() {
    }

    public void logDebug(String format, Object... args) {
        if (this.config.isDebug()) {
            getLogger().info("[DEBUG] "+ String.format(format, args));
        }
    }
    
    public void logError(String format, Object... args) {
        getLogger().severe("[ERROR] "+ String.format(format, args));
    }
    
}
