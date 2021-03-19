package me.khanghoang.oregen.chestgui;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import me.khanghoang.oregen.Main;
import me.khanghoang.oregen.Utils;
import me.khanghoang.smartinv.InventoryManager;
import me.khanghoang.smartinv.SmartInventory;

public class OreGenGUIManager extends InventoryManager {

    private Main plugin;

    public OreGenGUIManager(Main plugin) {
        super(plugin);
        this.plugin = plugin;
        this.init();
    }

    public SmartInventory getGeneratorListGUI() {
        plugin.logDebug("getGeneratorListGUI");
        SmartInventory inv = SmartInventory
            .builder()
            .manager(this)
            .provider(new GeneratorListProvider(plugin))
            .size(2, 9)
            .title(Utils.format("&c&lChoose a generator"))
            .build();
        return inv;
    }
}
