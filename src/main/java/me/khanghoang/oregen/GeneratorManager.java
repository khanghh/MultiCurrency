package me.khanghoang.oregen;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.khanghoang.oregen.config.YamlConfig;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.io.File;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

/**
 * @author khanghh on 2021/03/17
 */
public class GeneratorManager {
    
    private Main plugin;
    private Map<String, OreGenerator> generators = new HashMap<>();
    private List<String> disabledWorlds;
    private HashMap<String, OreGenerator> cachedPlayers = new HashMap<>();
    private YamlConfig playersConfig;
    private OreGenerator defaulGenerator;
    private final long cacheDuration = 5000;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    
    public GeneratorManager(Main plugin) {
        this.plugin = plugin;
        this.reloadConfig();
        this.loadPlayerConfig();
    }
    
    public void reloadConfig() {
        try {
            readWriteLock.writeLock().lock();
            plugin.getConfig().reload();
            generators.clear();
            for (OreGenerator gen : plugin.getConfig().getGenerators()) {
                if (gen.isDefault || gen.name.equals("default")) {
                    defaulGenerator = gen;
                }
                generators.put(gen.name, gen);
            }
            disabledWorlds = plugin.getConfig().getDisabledWorlds();
            if (defaulGenerator == null) {
                defaulGenerator = new OreGenerator();
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    private void loadPlayerConfig() {
        String fileName = "players.yml";
        File configFile = new File(plugin.getDataFolder(), fileName);
        playersConfig = new YamlConfig(configFile);
        if (!configFile.exists()) {
            plugin.saveResource(fileName, false);
        }
        playersConfig.load();
        ConfigurationSection playersCfg = playersConfig.getConfigurationSection("players");
        if (playersCfg == null) return;
        for (String uuidStr : playersCfg.getKeys(false)) {
            String genName = playersCfg.getString(uuidStr + ".generator");
            OreGenerator generator = findGeneratorByName(genName);
            if (generator == null) {
                generator = defaulGenerator;
            }
            addCachedGenerator(UUID.fromString(uuidStr), generator);
        }
    }
    
    private void addCachedGenerator(UUID pUuid, OreGenerator generator) {
        OreGenerator gen = generator.clone();
        gen.lastUsed = System.currentTimeMillis();
        cachedPlayers.put(pUuid.toString(), gen);
    }

    private String getPlayerData(UUID pUuid) {
        return playersConfig.getString("players." + pUuid.toString() + ".generator");
    }

    private void savePlayerData(Player player, String genName) {
        try {
            readWriteLock.writeLock().lock();
            UUID pUuid = player.getUniqueId();
            plugin.logDebug("savePlayerData: %s -> %s", pUuid.toString(), genName);
            playersConfig.set("players." + pUuid.toString() + ".generator", genName);
            playersConfig.set("players." + pUuid.toString() + ".name", player.getName());
            playersConfig.save();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    private OreGenerator findPlayerGenerator(Player player) {
        return generators.values().stream().filter(gen -> {
            String genPerm = String.format("oregen.%s", gen.name);
            int islandLevel = plugin.getSkyBlockAPICached().getIslandLevel(player.getUniqueId());
            return player.hasPermission(genPerm) && islandLevel >= gen.islandLevel || gen.isDefault;
        }).sorted((item, other) -> Integer.compare(item.rank, other.rank)).findFirst().orElse(defaulGenerator);
    }

    public List<String> getDisabledWorlds() {
        return disabledWorlds;
    }

    public Map<String, OreGenerator> getGenerators() {
        return generators;
    }

    public boolean addUpdateOreGenerator(OreGenerator newGen) {
        generators.put(newGen.name, newGen);
        saveGenerators();
        return true;
    }

    public boolean removeOreGenerator(String genName) {
        if (generators.containsKey(genName)) {
            generators.remove(genName);
            saveGenerators();
            return true;
        }
        return false;
    }

    private void saveGenerators() {
        List<OreGenerator> genList = generators.values().stream()
            .sorted((gen, other) -> Integer.compare(gen.rank, other.rank))
            .collect(Collectors.toList());
        plugin.getConfig().setGenerators(genList);
    }

    public OreGenerator findGeneratorByName(String genName) {
        if (genName == null) return defaulGenerator;
        for (OreGenerator generator: generators.values()) {
            if (generator.name.equals(genName)) {
                return generator;
            }
        }
        return null;
    }

    public OreGenerator getPlayerGenerator(UUID pUuid) {
        // Get generator from cache
        long timeNow = System.currentTimeMillis();
        OreGenerator cachedGen = cachedPlayers.get(pUuid.toString());
        if (cachedGen != null && timeNow - cachedGen.lastUsed < cacheDuration) {
            return cachedGen;
        }
        // if cached generator is not found or expired, find the real generator
        // and add them to cache
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(pUuid);
        if (offlinePlayer.isOnline()) {
            Player player = offlinePlayer.getPlayer();
            OreGenerator generator = findPlayerGenerator(player);
            String currentGenName = getPlayerData(pUuid);
            if (!generator.name.equals(currentGenName) && (currentGenName != null || !generator.isDefault)) {
                savePlayerData(player, generator.name);
            }
            addCachedGenerator(pUuid, generator);
            return generator;
        }
        return cachedGen != null ? cachedGen : defaulGenerator;
    }
}
