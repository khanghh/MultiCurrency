package me.khanghoang.oregen.chestgui;

import me.khanghoang.oregen.Main;
import me.khanghoang.oregen.OreGenerator;
import me.khanghoang.oregen.Utils;
import me.khanghoang.smartinv.ClickableItem;

import me.khanghoang.smartinv.content.InventoryContents;
import me.khanghoang.smartinv.content.InventoryProvider;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author khanghh on 2021/02/21
 */
public class ConfirmDeleteDialogProvider implements InventoryProvider {

    private Main plugin;
    private String genId;

    private void drawOreGenItem(InventoryContents contents, int row, int column) {
        OreGenerator generator =  plugin.getManager().findGeneratorById(genId);
        Material itemType = Material.getMaterial(generator.item);
        if (itemType == null) itemType = Material.COBBLESTONE;
        ItemStack item = new ItemStack(itemType, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.format(generator.name));
        item.setItemMeta(itemMeta);
        contents.set(row, column, ClickableItem.empty(item));
    }


    private void drawYesItem(InventoryContents contents, int row, int column) {
        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.format("&c&lDelete"));
        item.setItemMeta(itemMeta);
        contents.set(row, column, ClickableItem.of(item, event -> {
            plugin.getManager().removeOreGenerator(genId);
            Player player = (Player)event.getWhoClicked();
            plugin.getGUIManager().getGeneratorListGUI().open(player);
        }));
    }

    private void drawCancelItem(InventoryContents contents, int row, int column) {
        ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.format("&a&lCancel"));
        item.setItemMeta(itemMeta);
        contents.set(row, column, ClickableItem.of(item, event -> {
            Player player = (Player)event.getWhoClicked();
            plugin.getGUIManager().getGeneratorListGUI().open(player);
        }));
    }

    public ConfirmDeleteDialogProvider(Main plugin, String genId) {
        this.plugin = plugin;
        this.genId = genId;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        drawOreGenItem(contents, 0, 4);
        drawYesItem(contents, 1, 2);
        drawCancelItem(contents, 1, 6);
    }

    @Override
    public void update(Player player, InventoryContents contents) {
    }

}