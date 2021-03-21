package me.khanghoang.oregen.gui;

import me.khanghoang.oregen.Main;
import me.khanghoang.oregen.OreGenerator;
import me.khanghoang.oregen.Utils;
import me.khanghoang.smartinv.ClickableItem;

import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import me.khanghoang.smartinv.content.InventoryContents;
import me.khanghoang.smartinv.content.InventoryProvider;
import me.khanghoang.smartinv.content.SlotPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author khanghh on 2021/02/21
 */
public class GeneratorListProvider implements InventoryProvider {

    private Main plugin;

    public GeneratorListProvider(Main plugin) {
        this.plugin = plugin;
    }

    private ItemStack getOreGenItem(OreGenerator generator) {
        Material itemType = Material.getMaterial(generator.item);
        if (itemType == null) itemType = Material.COBBLESTONE;
        ItemStack item = new ItemStack(itemType, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.format(generator.label));
        List<String> lore = new ArrayList<>();
        lore.add(Utils.format("&2Total ores: &a&l%s", generator.blocks.size()));
        lore.add(Utils.format("&2Label: &f%s", generator.label));
        lore.add(Utils.format("&2Name: &5%s", generator.name));
        lore.add(Utils.format("&2Symbol: %s", generator.symbol));
        lore.add(Utils.format("&2Rank: &f%s", generator.rank));
        if (generator.rank < 1) {
            lore.add(Utils.format("&4&oDISABLED"));
        }
        lore.add(Utils.format("&oLeft click &7&oto edit"));
        lore.add(Utils.format("&oRight click &7&oto remove"));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    private ItemStack getOregenAddItem() {
        ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.format("&aAdd new generator"));
        List<String> lores = new ArrayList<>();
        lores.add("Drop a block here to create new generator.");
        itemMeta.setLore(lores);
        item.setItemMeta(itemMeta);
        return item;
    }

    private void drawOreGeneratorItem(InventoryContents contents, OreGenerator generator) {
        Consumer<InventoryClickEvent> generatorConsumer = event -> {
            Player player = (Player)event.getWhoClicked();
            if (event.isLeftClick()) {
                plugin.getGUIManager().openEditGeneratorGUI(player, generator.name);
            } else if (event.isRightClick()) {
                plugin.getGUIManager().openDeleteGeneratorGUI(player, generator.name);
            }
        };
        ItemStack item = getOreGenItem(generator);
        Optional<SlotPos> slot = contents.firstEmpty();
        if (slot.isPresent()) {
            contents.set(slot.get(), ClickableItem.of(item, generatorConsumer));
        }
    }

    private OreGenerator getNewGenerator() {
        int id = 0;
        for (OreGenerator generator : plugin.getManager().getGenerators()) {
            if (generator.name.startsWith("generator")) {
                String idStr = generator.name.replaceAll("generator", "");
                int newId = Utils.parseInt(idStr, 0);
                if (newId > id) id = newId;
            }
        }
        id += 1;
        String name = "generator" + id;
        return new OreGenerator(name);
    }

    private void drawAddGeneratorItem(InventoryContents contents, int row, int column) {
        ItemStack itemAdd = getOregenAddItem();

        Consumer<InventoryClickEvent> addGenerator = event -> {
            if (event.isLeftClick()) {
                if (event.getAction() != InventoryAction.SWAP_WITH_CURSOR) return;
                ItemStack cursorItem = event.getCursor();
                if (!cursorItem.getType().isBlock()) return;
                cursorItem.setAmount(1);
                Optional<SlotPos> slot = contents.firstEmpty();
                if (!slot.isPresent()) return;
                event.getWhoClicked().setItemOnCursor(null);
                OreGenerator newGen = getNewGenerator();
                newGen.item = cursorItem.getType().name();
                plugin.getManager().addOreGenerator(newGen);
                drawOreGeneratorItem(contents, newGen);
            }
        };
        contents.set(row, column, ClickableItem.of(itemAdd, addGenerator));
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
        for (OreGenerator generator : plugin.getManager().getGenerators()) {
            drawOreGeneratorItem(contents, generator);
        }
        drawGlass(contents, 2, 0);
        drawGlass(contents, 2, 1);
        drawGlass(contents, 2, 2);
        drawGlass(contents, 2, 3);
        drawAddGeneratorItem(contents, 2, 4);
        drawGlass(contents, 2, 5);
        drawGlass(contents, 2, 6);
        drawGlass(contents, 2, 7);
        drawGlass(contents, 2, 8);
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        // plugin.logDebug("GeneratorListProvider");
    }

}