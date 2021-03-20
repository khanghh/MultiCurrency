package me.khanghoang.oregen.chestgui;

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
            .size(3, 9)
            .title(Utils.format("&c&lChoose a generator"))
            .build();
        return inv;
    }

    public SmartInventory getConfirmDeleteDialog(String genId) {
        plugin.logDebug("getConfirmDeleteDialog: %s", genId);
        SmartInventory inv = SmartInventory
            .builder()
            .manager(this)
            .provider(new ConfirmDeleteDialogProvider(plugin, genId))
            .size(3, 9)
            .title(Utils.format("&c&lConfirm delete generator &6&l%s", genId))
            .build();
        return inv;
    }

    public SmartInventory getEditGeneratorGUI(String genId) {
        plugin.logDebug("getEditGeneratorGUI");
        SmartInventory inv = SmartInventory
            .builder()
            .manager(this)
            .provider(new EditGeneratorProvider(plugin, genId))
            .size(6, 9)
            .title(Utils.format("&a&lEdit genearator &6&l%s", genId))
            .build();
        return inv;
    }
}
