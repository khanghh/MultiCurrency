package me.khanghoang.oregen.listeners;

import me.khanghoang.oregen.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author khanghh on 2021/02/20
 */
public class InventoryEventListener implements Listener {
    private Main plugin;

    public InventoryEventListener(Main plugin) {
        this.plugin = plugin;
        ItemStack stack;
    }

    @EventHandler
    public void onOpenInventory(InventoryOpenEvent event) {
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
    }
}
