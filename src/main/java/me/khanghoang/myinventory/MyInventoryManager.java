package me.khanghoang.myinventory;

import me.khanghoang.myinventory.providers.MainInventoryProvider;
import me.khanghoang.myinventory.storage.TestStorage;
import org.bukkit.entity.Player;

import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import me.khanghoang.myinventory.storage.IDataStorage;
import me.khanghoang.myinventory.storage.MySQLStorage;

public class MyInventoryManager extends InventoryManager {

    private Main plugin;
    private IDataStorage dataStorage;

    public MyInventoryManager(Main plugin) {
        super(plugin);
        this.plugin = plugin;
        this.init();
        dataStorage = new TestStorage(plugin);
    }

    public SmartInventory getMainInventory(Player player) {
        return SmartInventory.builder()
                .manager(this)
                .provider(new MainInventoryProvider(player, dataStorage))
                .size(6, 9)
                .title("Inventory of " + player.getName())
                .build();
    }
}
