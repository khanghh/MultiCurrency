package me.khanghoang.oregen.hooks;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;

public class HookSuperiorSkyblock implements SkyblockAPIHook {


	public HookSuperiorSkyblock() {

	}

	@Override
	public int getIslandLevel(UUID uuid) {
		if(SuperiorSkyblockAPI.getPlayer(uuid) == null || SuperiorSkyblockAPI.getPlayer(uuid).getIsland() == null) {
			return 0;
		}else {
			return 	SuperiorSkyblockAPI.getPlayer(uuid).getIsland().getIslandLevel().intValue();
		}
	}

	@Override
	public Optional<UUID> getIslandOwner(Location loc) {
		if(SuperiorSkyblockAPI.getIslandAt(loc) != null && SuperiorSkyblockAPI.getIslandAt(loc).getOwner() != null) {
			return Optional.of(SuperiorSkyblockAPI.getIslandAt(loc).getOwner().getUniqueId());
		}else {
			return Optional.empty();
		}
	}

	@Override
	public String[] getSkyBlockWorldNames() {
		return new String[] {SuperiorSkyblockAPI.getIslandsWorld().getName()};
	}
	
	@Override
	public void sendBlockAcknowledge(Block block) {
		if(SuperiorSkyblockAPI.getIslandAt(block.getLocation()) != null) {
			SuperiorSkyblockAPI.getIslandAt(block.getLocation()).handleBlockPlace(block);	
		}
	}

}
