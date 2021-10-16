package me.khanghoang.multicurrency.storage;

import java.util.Collection;
import java.util.Map;

/**
 * Represents a storage model object.
 * 
 * @author Mitsugaru
 */
public interface Datasource {

    boolean beginChange();

    boolean commitChange();

    Map<String, Integer> getPlayerPointAll(String playerId) throws Exception;;

    /**
     * Get the amount of points the given player has.
     * 
     * @param playerId
     *            - Id of player.
     * @return Points the player has.
     */
    int getPlayerPoint(String playerId, String serverName) throws Exception;

    /**
     * Set the amount of points for the given player.
     * 
     * @param playerId
     *            - Player id
     * @param points
     *            - Amount of points to set.
     * @param serverName
     *            - Server to set points.
     * @return True if we were successful in editing, else false.
     */
    boolean setPlayerPoint(String playerId, int points, String serverName) throws Exception;

    /**
     * Check whether the player already exists in the storage medium.
     * 
     * @param playerId
     *            - Player id.
     * @return True if player is in storage, else false.
     */
    boolean playerEntryExists(String playerId, String serverName) throws Exception;

    boolean transferPointServer(String playerId, int amount, String fromServer, String toServer) throws Exception;

    /**
     * Remove player entry.
     * 
     * @param id
     *            - Player to remove.
     * @return True if entry was removed, else false.
     */
    boolean removePlayer(String id, String serverName) throws Exception;

    /**
     * Completely destroy all records.
     * 
     * @return True if successful, else false.
     */
    boolean destroy() throws Exception;

    /**
     * Build storage.
     * 
     * @return True if successful, else false.
     */
    boolean build() throws Exception;

    /**
     * Get the existing players in storage.
     * 
     * @return Collection of player IDs.
     */
    Collection<String> getPlayers() throws Exception;

}
