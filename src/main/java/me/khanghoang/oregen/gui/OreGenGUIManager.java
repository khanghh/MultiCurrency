package me.khanghoang.oregen.gui;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import me.khanghoang.oregen.Main;
import me.khanghoang.oregen.OreGenerator;
import me.khanghoang.oregen.Utils;
import me.khanghoang.smartinv.InventoryManager;
import me.khanghoang.smartinv.SmartInventory;

public class OreGenGUIManager extends InventoryManager {

    private Main plugin;
    private SignGUIManager signGUIManager;

    public OreGenGUIManager(Main plugin) {
        super(plugin);
        this.plugin = plugin;
        this.init();
        this.signGUIManager = new SignGUIManager(plugin);
    }

    public SmartInventory openGeneratorListGUI(Player player) {
        plugin.logDebug("openGeneratorListGUI");
        SmartInventory inv = SmartInventory
            .builder()
            .manager(this)
            .provider(new GeneratorListProvider(plugin))
            .size(3, 9)
            .title(Utils.format("&c&lChoose a generator"))
            .build();
        inv.open(player);
        return inv;
    }

    public SmartInventory openDeleteGeneratorGUI(Player player, String genName) {
        plugin.logDebug("openDeleteGeneratorGUI: %s", genName);
        SmartInventory inv = SmartInventory
            .builder()
            .manager(this)
            .provider(new ConfirmDeleteDialogProvider(plugin, genName))
            .size(3, 9)
            .title(Utils.format("&c&lConfirm delete generator &6&l%s", genName))
            .build();
        inv.open(player);
        return inv;
    }

    public SmartInventory openEditGeneratorGUI(Player player, String genName) {
        plugin.logDebug("openEditGeneratorGUI: %s", genName);
        OreGenerator generator = plugin.getManager().findGeneratorByName(genName);
        if (generator != null) {
            generator = generator.clone();
        }
        SmartInventory inv = SmartInventory
            .builder()
            .manager(this)
            .provider(new EditGeneratorProvider(plugin, generator))
            .size(6, 9)
            .title(Utils.format("&c&lEdit genearator &6&l%s", genName))
            .build();
        inv.open(player);
        return inv;
    }

    public SignGUIManager getSignGUIManager() {
        return signGUIManager;
    }
}
