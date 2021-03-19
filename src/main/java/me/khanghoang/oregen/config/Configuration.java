package me.khanghoang.oregen.config;

import me.khanghoang.oregen.OreBlock;
import me.khanghoang.oregen.OreGenerator;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.*;

/**
 * @author khanghh on 2021/02/15
 */
public class Configuration extends YamlConfig {

    private boolean debug;

    public static Configuration loadFromFile(File file) {
        Configuration config = new Configuration(file);
        config.load();
        config.applyConfig();
        return config;
    }

    public Configuration(File file) {
        super(file);
    }

    public void reload() {
        this.load();
        this.applyConfig();
    }

    private void applyConfig() {
        this.debug = this.getBoolean("debug");
    }

    public boolean isDebug() {
        return debug;
    }

    public void toggleDebug() {
        debug = !debug;
        this.set("debug", debug);
        this.save();
    }

    public void migrate(Configuration oldConfig) {
        for (String key : oldConfig.getKeys(false)) {
            if (this.contains(key)) {
                this.set(key, oldConfig.get(key));
            }
        }
        applyConfig();
    }
    
    public List<OreGenerator> getGenerators() {
        List<OreGenerator> generators = new ArrayList<>();
        ConfigurationSection genCfg = this.getConfigurationSection("generators");
        if (genCfg == null) return generators;
        int rank = 1;
        for (String name : genCfg.getKeys(false)) {
            String label = genCfg.getString(name + ".label");
            int unlock_islandLevel = genCfg.getInt(name + ".islandLevel", 0);
            Set<OreBlock> blocks = new HashSet<>();
            ConfigurationSection blocksCfg = genCfg.getConfigurationSection(name + ".blocks");
            if (blocksCfg == null) continue;
            for (String blockName : blocksCfg.getKeys(false)) {
                double chance = blocksCfg.getDouble(blockName, 0.0);
                blocks.add(new OreBlock(blockName, chance));
            }
            boolean isDefault = genCfg.getBoolean(name + ".default", false);
            OreGenerator generator = new OreGenerator(name, label, unlock_islandLevel, blocks, isDefault);
            generator.rank = rank++;
            generators.add(generator);
        }
        return generators;
    }

    public void setGenerators(List<OreGenerator> generators) {
        this.set("generators", null);
        for (OreGenerator generator: generators) {
            String name = generator.name;
            this.set("generators." + name + ".label", generator.label);
            this.set("generators." + name + ".islandLevel", generator.islandLevel);
            this.set("generators." + name + ".default", false);
            for (OreBlock block : generator.blocks) {
                this.set("generators." + name + ".blocks." + block.name, block.chance);
            }
        }
        this.save();
    }

    public List<String> getDisabledWorlds() {
        return this.getStringList("disabledWorlds");
    }

    public void setDisabledWorlds(List<String> disabledWorlds) {
        this.set("disabledWorlds", disabledWorlds);
        this.save();
    }
}
