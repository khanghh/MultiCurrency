package me.khanghoang.multicurrency.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Called when a player's points is to be changed.
 */
public class PlayerPointChangedEvent extends Event {

    /**
     * Handler list.
     */
    private static final HandlerList handlers = new HandlerList();

    /**
     * old point balance value
     */
    private int oldValue;

    /**
     * new point balance value
     */
    private int newValue;
    
    /**
     * Player whose point is changing.
     */
    private UUID playerId;
    
    /**
     * Constructor.
     * 
     * @param playerId
     *              Player UUID
     * @param oldvalue
     *              old balance of player.
     * @param oldvalue
     *              new balance of player.
     *              new balance of player.
     */
    public PlayerPointChangedEvent(UUID playerId, int oldvalue, int newValue) {
        this.playerId = playerId;
        this.oldValue = oldvalue;
        this.newValue = newValue;
    }

    /**
     * get new balance
     * @return
     */
    public int getNewValue() {
        return newValue;
    }

    /**
     * get old balance
     * @return
     */
    public int getOldValue() {
        return oldValue;
    }

    /**
     * Get the amount of points that the player's balance will change by.
     *
     * @return Amount of change.
     */
    public int getChange() {
        return newValue - oldValue;
    }


    /**
     * Get the player id.
     *
     * @return Player UUID.
     */
    public UUID getPlayerId() {
        return playerId;
    }
    
    /**
     * Static method to get HandlerList.
     *
     * @return HandlerList.
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }



    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}