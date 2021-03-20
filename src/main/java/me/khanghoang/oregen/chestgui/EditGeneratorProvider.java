package me.khanghoang.oregen.chestgui;

import me.khanghoang.oregen.Main;
import me.khanghoang.oregen.OreBlock;
import me.khanghoang.oregen.OreGenerator;
import me.khanghoang.oregen.Utils;
import me.khanghoang.smartinv.ClickableItem;
import me.khanghoang.smartinv.content.InventoryContents;
import me.khanghoang.smartinv.content.InventoryProvider;
import me.khanghoang.smartinv.content.SlotPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author khanghh on 2021/02/21
 */
public class EditGeneratorProvider implements InventoryProvider {

    private Main plugin;
    private String genId;

    public EditGeneratorProvider(Main plugin, String genId) {
        this.plugin = plugin;
        this.genId = genId;
    }

    private ItemStack getOreBlockItem(OreBlock oreBlock) {
        Material itemType = Material.getMaterial(oreBlock.name);
        if (itemType == null) itemType = Material.BARRIER;
        ItemStack item = new ItemStack(itemType, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(oreBlock.name);
        List<String> lore = new ArrayList<>();
        lore.add(Utils.format("&a&oleft &7&oclick to edit"));
        lore.add(Utils.format("&c&oright &7&oclick to remove"));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    private void drawOreBlock(InventoryContents contents, OreBlock oreBlock) {
        ItemStack item = getOreBlockItem(oreBlock);
        Optional<SlotPos> slot = contents.firstEmpty();
        if (slot.isPresent()) {
            contents.set(slot.get(), ClickableItem.empty(item));
        }
    }

    private void drawGlass(InventoryContents contents, int row, int column) {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(" ");
        item.setItemMeta(itemMeta);
        contents.set(row, column, ClickableItem.empty(item));
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        OreGenerator generator = plugin.getManager().findGeneratorById(genId);
        for (OreBlock oreBlock: generator.blocks) {
            drawOreBlock(contents, oreBlock);
        }

        drawGlass(contents, 5, 0);
        drawGlass(contents, 5, 1);
        drawGlass(contents, 5, 2);
        drawGlass(contents, 5, 3);
        drawGlass(contents, 5, 4);
        drawGlass(contents, 5, 5);
        drawGlass(contents, 5, 6);
        drawGlass(contents, 5, 7);
        drawGlass(contents, 5, 8);
    }

    @Override
    public void update(Player player, InventoryContents contents) {}

}