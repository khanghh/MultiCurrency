package me.khanghoang.multicurrency.listener;

import me.khanghoang.multicurrency.MultiCurrency;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author khanghh on 2021/03/28
 */
public class PlayerEventListener implements Listener {

    private final MultiCurrency plugin;

    public PlayerEventListener(MultiCurrency plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
//        plugin.getAPI().removeCachedPoints(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
//        plugin.getAPI().removeCachedPoints(player.getUniqueId());
    }
}
