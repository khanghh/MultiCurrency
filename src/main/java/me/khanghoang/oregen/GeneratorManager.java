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
import java.util.stream.Collectors;

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
            defaulGenerator = generators.stream()
                .filter(gen -> gen.isDefault)
                .findFirst()
                .orElse(new OreGenerator());
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
        ListIterator<OreGenerator> it = generators.listIterator(generators.size());
        while(it.hasPrevious()) {
            OreGenerator generator = it.previous();
            String genPerm = String.format("oregen.%s", generator.name);
            int islandLevel = plugin.getSkyBlockAPICached().getIslandLevel(player.getUniqueId());
            if (player.hasPermission(genPerm) && generator.rank > 0 &&
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
        return generators.stream()
            .sorted((gen, other) -> Integer.compare(gen.rank, other.rank))
            .collect(Collectors.toList());
    }

    public boolean addOreGenerator(OreGenerator newGen) {
        boolean exists = generators.stream().filter(gen -> gen.name.equals(newGen.name)).findFirst().isPresent();
        if (!exists) {
            generators.add(newGen);
            plugin.getConfig().setGenerators(generators);
        }
        return false;
    }

    public boolean removeOreGenerator(String genName) {
        Iterator<OreGenerator> it = generators.iterator();
        while(it.hasNext()) {
            if (it.next().name == genName) {
                it.remove();
                plugin.logDebug("deleted: %s", genName);
                plugin.getConfig().setGenerators(generators);
                return true;
            }
        }
        return false;
    }

    public boolean updateOreGenerator(String genName, OreGenerator newGen) {
        for (int i = 0; i < generators.size(); i++) {
            OreGenerator currentGen = generators.get(i);
            if (currentGen.name.equals(genName)) {
                generators.set(i, newGen);
                return true;
            }
        }
        return false;
    }

    public void saveOreGenerators() {
    }

    public OreGenerator findGeneratorByName(String genName) {
        if (genName == null) return defaulGenerator;
        for (OreGenerator generator : generators) {
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
