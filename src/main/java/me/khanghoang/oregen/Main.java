package me.khanghoang.oregen;

import me.khanghoang.oregen.chestgui.OreGenGUIManager;
import me.khanghoang.oregen.commands.OreGenCommand;
import me.khanghoang.oregen.config.Configuration;
import me.khanghoang.oregen.hooks.HookInfo;
import me.khanghoang.oregen.hooks.HookVanilla;
import me.khanghoang.oregen.hooks.SkyBlockAPICached;
import me.khanghoang.oregen.hooks.SkyblockAPIHook;
import me.khanghoang.oregen.listeners.OreGenListener;

import me.khanghoang.oregen.misc.NamePlaceholder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;

public class Main extends JavaPlugin {

    private SkyBlockAPICached skyBlockAPICached;
    private Configuration config;
    private GeneratorManager manager;
    private OreGenGUIManager guiManager;

    private SkyblockAPIHook getSkyBlockAPI() {
        SkyblockAPIHook skyblockAPI = null;
        for(HookInfo hookInfo : HookInfo.values()) {
            String pluginName = hookInfo.name().replace("Legacy", "");
            if(Bukkit.getServer().getPluginManager().isPluginEnabled(pluginName)) {
                try {
                    logInfo("&aFound SkyBlock plguin: %s. Hook in!", pluginName);
                    skyblockAPI = (SkyblockAPIHook) hookInfo.getHookClass().newInstance();
                    logInfo("&aHooked into %s. Hook class: %s", pluginName, hookInfo.getHookClass().getName());
                    break;
                } catch (NoClassDefFoundError err) {
                    logError("Could not hook into SkyBlock plugin %s.", pluginName);
                    err.printStackTrace();
                } catch (InstantiationException | IllegalAccessException err) {
                    err.printStackTrace();
                }
            }
        }
        if (skyblockAPI == null) {
            logInfo("&aNo SkyBlock plguin found. Hook vanilla instead!");
            skyblockAPI = new HookVanilla();
        }
        return skyblockAPI;
    }
    
    @Override
    public Configuration getConfig() {
        return config;
    }

    public Configuration loadConfig() {
        String fileName = "config.yml";
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

    public SkyBlockAPICached getSkyBlockAPICached() {
        return skyBlockAPICached;
    }

    public GeneratorManager getManager() {
        return manager;
    }

    public OreGenGUIManager getGUIManager() {
        return this.guiManager;
    }

    @Override
    public void onEnable() {
        this.config = loadConfig();
        this.manager = new GeneratorManager(this);
        this.guiManager = new OreGenGUIManager(this);
        this.skyBlockAPICached = new SkyBlockAPICached(getSkyBlockAPI());
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new OreGenListener(this), this);
        getCommand("oregen").setExecutor(new OreGenCommand(this));
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new NamePlaceholder(this).register();
        }
    }

    @Override
    public void onDisable() {
    }
    
    public void logInfo(String format, Object... args) {
        String message = String.format(format, args);
        getLogger().info(Utils.format(message));
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
