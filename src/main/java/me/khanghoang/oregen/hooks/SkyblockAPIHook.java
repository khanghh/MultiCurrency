package me.khanghoang.oregen.hooks;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;

public interface SkyblockAPIHook {
	/**
	 * Returns the island level for a defined player uuid
	 * 
	 * @param uuid UUID of the island owner
	 * @return island level
	 */
	public int getIslandLevel(UUID uuid);
	
	/**
	 * Gets the owner of an island on a certain location
	 * 
	 * @param loc location to check for island
	 * @return island owner UUID
	 */
	public Optional<UUID> getIslandOwner(Location loc);
	
	/**
	 * Obtains the names of the skyblock worlds
	 * 
	 * @return the names of the skyblock worlds
	 */
	public String[] getSkyBlockWorldNames();
	
	/*
	 * Calls the specific SkyBlock-API to make it aware of a block placement
	 * 
	 */
	public void sendBlockAcknowledge(Block block);
}
