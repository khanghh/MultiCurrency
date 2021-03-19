package me.khanghoang.oregen.hooks;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;

import com.wasteofplastic.askyblock.ASkyBlockAPI;

public class HookASkyBlock implements SkyblockAPIHook {

	private ASkyBlockAPI api;

	public HookASkyBlock() {
		api = ASkyBlockAPI.getInstance();
	}

	@Override
	public int getIslandLevel(UUID uuid) {
		return api.getIslandLevel(uuid);
	}

	@Override
	public Optional<UUID> getIslandOwner(Location loc) {
		Optional<UUID> optional = Optional.empty();
		if(api.getIslandAt(loc) != null) {
			optional = Optional.of(api.getIslandAt(loc).getOwner());
		}
		return optional;
	}

	@Override
	public String[] getSkyBlockWorldNames() {
		return new String[] { api.getIslandWorld().getName() };
	}

	@Override
	public void sendBlockAcknowledge(Block block) {
		// TODO Auto-generated method stub
		
	}
	

}
