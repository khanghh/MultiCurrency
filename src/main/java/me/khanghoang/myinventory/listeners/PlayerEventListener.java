package me.khanghoang.myinventory.listeners;

import me.khanghoang.myinventory.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author khanghh on 2021/02/20
 */
public class PlayerEventListener implements Listener {
    private Main plugin;

    public PlayerEventListener(Main plugin) {
        this.plugin = plugin;
        ItemStack stack;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
    }
}
