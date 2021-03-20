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
import java.util.UUID;
import java.io.File;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author khanghh on 2021/03/17
 */
public class GeneratorManager {
    
    private Main plugin;
    private List<OreGenerator> generators;
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
            generators = plugin.getConfig().getGenerators();
            disabledWorlds = plugin.getConfig().getDisabledWorlds();
            if (generators.size() > 0) {
                defaulGenerator = generators.get(0);
            } else {
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
            String genKey = playersCfg.getString(uuidStr + ".generator");
            OreGenerator generator = findGeneratorById(genKey);
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

    private void savePlayerData(Player player, String genId) {
        try {
            readWriteLock.writeLock().lock();
            UUID pUuid = player.getUniqueId();
            plugin.logDebug("savePlayerData: %s -> %s", pUuid.toString(), genId);
            playersConfig.set("players." + pUuid.toString() + ".generator", genId);
            playersConfig.set("players." + pUuid.toString() + ".name", player.getName());
            playersConfig.save();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    private OreGenerator findPlayerGenerator(Player player) {
        ListIterator<OreGenerator> it = generators.listIterator(generators.size());
        while(it.hasPrevious()) {
            OreGenerator generator = it.previous();
            String genPerm = String.format("oregen.%s", generator.genId);
            int islandLevel = plugin.getSkyBlockAPICached().getIslandLevel(player.getUniqueId());
            if (player.hasPermission(genPerm) &&
                islandLevel >= generator.islandLevel || generator.isDefault) {
                return generator;
            }
        }
        return defaulGenerator;
    }

    public List<String> getDisabledWorlds() {
        return disabledWorlds;
    }

    public List<OreGenerator> getGenerators() {
        return generators;
    }

    public boolean addOreGenerator(OreGenerator newGen) {
        boolean exists = generators.stream().filter(gen -> gen.genId.equals(newGen.genId)).findFirst().isPresent();
        if (!exists) {
            generators.add(newGen);
            plugin.getConfig().setGenerators(generators);
        }
        return false;
    }

    public boolean removeOreGenerator(String genId) {
        Iterator<OreGenerator> it = generators.iterator();
        while(it.hasNext()) {
            if (it.next().genId == genId) {
                it.remove();
                plugin.logDebug("deleted: %s", genId);
                plugin.getConfig().setGenerators(generators);
                return true;
            }
        }
        return false;
    }

    public OreGenerator findGeneratorById(String genId) {
        if (genId == null) return defaulGenerator;
        for (OreGenerator generator: generators) {
            if (generator.genId == genId) {
                return generator;
            }
        }
        return defaulGenerator;
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
            String currentGenId = getPlayerData(pUuid);
            if (!generator.genId.equals(currentGenId) && (currentGenId != null || !generator.isDefault)) {
                savePlayerData(player, generator.genId);
            }
            addCachedGenerator(pUuid, generator);
            return generator;
        }
        return cachedGen != null ? cachedGen : defaulGenerator;
    }
}
