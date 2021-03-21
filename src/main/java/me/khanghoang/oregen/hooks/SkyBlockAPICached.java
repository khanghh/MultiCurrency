package me.khanghoang.oregen.hooks;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class SkyBlockAPICached {
	private SkyblockAPIHook hook;
	
	public SkyBlockAPICached(SkyblockAPIHook hook) {
        this.hook = hook;
	}
	
	LoadingCache<UUID, Integer> cachedIslandLevel = CacheBuilder.newBuilder()
			   .maximumSize(1000)
			   .expireAfterWrite(10, TimeUnit.SECONDS)
			   .build(
			       new CacheLoader<UUID, Integer>() {
			         public Integer load(UUID key) {
			           return hook.getIslandLevel(key);
			         }
			       });
	
	LoadingCache<Location, Optional<UUID>> cachedIslandOwner = CacheBuilder.newBuilder()
			   .maximumSize(1000)
			   .expireAfterWrite(100, TimeUnit.SECONDS)
			   .build(
			       new CacheLoader<Location, Optional<UUID>>() {
			         public Optional<UUID> load(Location key) {
			           return hook.getIslandOwner(key);
			         }
			       });
	
	
	public int getIslandLevel(UUID owner) {
		try {
			return cachedIslandLevel.get(owner);
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	private Optional<UUID> getIslandOwnerUUID(Location loc){
		try {
			return cachedIslandOwner.get(loc);
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Optional.empty();
	}

    public String getHookName() {
        return this.hook.getClass().getName();
    }
	
	public OfflinePlayer getIslandOwner(Location loc) {
        Optional<UUID> uuid = getIslandOwnerUUID(loc);
        return uuid.map(Bukkit::getOfflinePlayer).orElse(null);
    }

    public List<World> getActiveWorlds() {
        return Arrays.stream(hook.getSkyBlockWorldNames())
            .map(v -> Bukkit.getWorld(v))
            .collect(Collectors.toList());
    }
	
	public void sendBlockAcknowledge(Block block) {
		hook.sendBlockAcknowledge(block);
	}
}
