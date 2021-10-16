package me.khanghoang.multicurrency.locale;

import java.io.File;

import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * locale message config
 *
 * @author khanghoang
 */
public class LocaleConfig extends YamlConfiguration {

    public static LocaleConfig loadFromFile(File file) {
        LocaleConfig config = new LocaleConfig(file);
        config.load();
        return config;
    }

    public static void saveDefault(File file, boolean replace) {
        // Set config
        if (file.exists() && !replace) {
            return;
        }
        YamlConfiguration config = new YamlConfiguration();
        try {
            for(LocaleMessage message: LocaleMessage.values()) {
                config.set(message.getKey(), message.getValue());
            }
            config.save(file);
        } catch(IOException ex) {
            Bukkit.getLogger().log(Level.WARNING, "failed to reload file", ex);
        }
    }

    /**
     * File reference.
     */
    private File file;

    public LocaleConfig(File file) {
        this.file = file;
    }
    
    /**
     * load locale config.
     *
     */
    public void load() {
        try {
            super.load(file);
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.WARNING, "failed to reload file", e);
        }
    }

}
