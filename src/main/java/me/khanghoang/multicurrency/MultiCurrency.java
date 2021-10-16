package me.khanghoang.multicurrency;

import kr.entree.spigradle.annotations.SpigotPlugin;
import me.khanghoang.multicurrency.command.Commander;
import me.khanghoang.multicurrency.config.ConfigLoader;
import me.khanghoang.multicurrency.config.YamlConfig;
import me.khanghoang.multicurrency.listener.PlayerEventListener;
import me.khanghoang.multicurrency.locale.LocaleConfig;
import me.khanghoang.multicurrency.locale.LocaleTranslator;
import me.khanghoang.multicurrency.log.LogLevel;
import me.khanghoang.multicurrency.log.MyLogger;
import me.khanghoang.multicurrency.misc.PlayerPointPlaceholder;
import me.khanghoang.multicurrency.storage.DatasourceType;
import me.khanghoang.multicurrency.storage.BankStorage;
import me.khanghoang.multicurrency.storage.mysql.MySQLStorage;
import me.khanghoang.multicurrency.storage.yaml.YAMLStorage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * @author khanghh on 2021/05/22
 */
@SpigotPlugin
public class MultiCurrency extends JavaPlugin {
    
    private static final String CONFIG_FILENAME = "config.yml";
    
    private static final String LOCALE_FILENAME = "localization.yml";
    
    private final File configFile = new File(getDataFolder(), CONFIG_FILENAME);

    private YamlConfig config;
    
    @Override
    public final YamlConfig getConfig() {
        return this.config;
    }
    
    @Override
    public final void reloadConfig() {
        try {
            this.loadConfig();
        } catch (Exception ex) {
            getLogger().log(Level.SEVERE, "Could not load config file " + CONFIG_FILENAME, ex);
        }
    }

    @Override
    public final void saveConfig() {
        try {
            this.config.save(configFile);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Could not save config to " + CONFIG_FILENAME, ex);
        }
    }
    
    private void updateConfig() {
        try {
            YamlConfig newConfig = new YamlConfig();
            newConfig.loadFromStream(getResource(CONFIG_FILENAME));
            YamlConfig oldConfig = this.config;
            for (String key : oldConfig.getKeys(false)) {
                newConfig.set(key, oldConfig.get(key));
            }
            config = newConfig;
        } catch (Exception ex) {
            getLogger().log(Level.SEVERE, "Could not update config file " + CONFIG_FILENAME, ex);
        }
    }

    private void loadConfig() {
        try {
            this.config = new YamlConfig();
            this.config.load(configFile);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } 
    }
    
    private BankStorage getBankStorage() {
        String datasource = config.getString("datasource");
        BankStorage bankStorage;
        if (datasource.equals(DatasourceType.MYSQL)) {
            bankStorage = new MySQLStorage(ConfigLoader.loadMySQLConfig(config));
        } else {
            bankStorage = new YAMLStorage();
        }
        return bankStorage;
    }
    
    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfig();
        updateConfig();

        File localeFile = new File(getDataFolder(), LOCALE_FILENAME);
        LocaleConfig.saveDefault(localeFile, false);
        LocaleConfig localeConfig = LocaleConfig.loadFromFile(localeFile);
        LocaleTranslator.initialize(localeConfig);

        MyLogger.setWriter(getLogger());
        if (config.getBoolean("debug")) {
            MyLogger.setLogLevel(LogLevel.DEBUG);
        }

        BankStorage bankStorage = getBankStorage();
        BankManager bankManager = new BankManager(bankStorage);
        CurrencyManager currencyManager = new CurrencyManager(bankStorage, config);
        Bukkit.getServicesManager().register(BankManager.class, bankManager, this, ServicePriority.Normal);
        Bukkit.getServicesManager().register(CurrencyManager.class, currencyManager, this, ServicePriority.Normal);
        
        Bukkit.getPluginManager().registerEvents(new PlayerEventListener(this), this);
        
        getCommand("bank").setExecutor(new Commander(this));
        
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlayerPointPlaceholder(this).register();
        }
    }

    @Override
    public void onDisable() {
    }


}
