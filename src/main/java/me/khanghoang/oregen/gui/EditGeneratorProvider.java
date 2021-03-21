package me.khanghoang.oregen.gui;

import me.khanghoang.oregen.Main;
import me.khanghoang.oregen.OreBlock;
import me.khanghoang.oregen.OreGenerator;
import me.khanghoang.oregen.Utils;
import me.khanghoang.oregen.gui.SignGUIManager.SignGUI;
import me.khanghoang.smartinv.ClickableItem;
import me.khanghoang.smartinv.content.InventoryContents;
import me.khanghoang.smartinv.content.InventoryProvider;
import me.khanghoang.smartinv.content.SlotPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author khanghh on 2021/02/21
 */
public class EditGeneratorProvider implements InventoryProvider {

    private Main plugin;
    private OreGenerator generator;
    private boolean editing;

    public EditGeneratorProvider(Main plugin, OreGenerator generator) {
        this.plugin = plugin;
        this.generator = generator;
        this.editing = false;
    }

    private double getTotalChance() {
        double sum = 0;
        for (OreBlock block: generator.blocks) {
            sum += block.chance;
        }
        return sum;
    }

    private SignGUI getEditChanceGUI(InventoryContents contents, OreBlock oreBlock) {
        List<String> text = new ArrayList<>();
        text.add(String.format("%,.2f", oreBlock.getChance()));
        text.add("&b&lEdit chance");
        text.add("&b&labove");
        text.add("");
        return plugin.getGUIManager().getSignGUIManager()
            .create(text)
            .reopenIfFail()
            .onResponse((player, lines) -> {
                double chance = Utils.parseDouble(lines[0], 0);
                if (chance > 0) {
                    if (oreBlock.chance != chance) {
                        plugin.logDebug("setChance: %s => %,.2f", oreBlock.name, chance);
                        oreBlock.chance = chance;
                        editing = true;
                    }
                    Bukkit.getScheduler().runTaskLater(plugin, () -> contents.inventory().open(player), 2L);
                    return true;
                }
                return false;
            });
    }

    private ItemStack getSaveItem() {
        ItemStack item = new ItemStack(Material.EMERALD, 1);
        ItemMeta itemMeta = item.getItemMeta();
        
        itemMeta.setDisplayName(Utils.format("&e&lTotal chance: &a&l%,.2f", getTotalChance()));
        if (editing) {
            itemMeta.addEnchant(Enchantment.DURABILITY, 1, false);
            List<String> lore = new ArrayList<>();
            if (getTotalChance() != 1.00) {
                lore.add(Utils.format("&4&oTotal chance must be equals &a&o1.00."));
            } else {
                lore.add(Utils.format("&7&oClick to save"));
            }
            itemMeta.setLore(lore);
        }
        item.setItemMeta(itemMeta);
        return item;
    }

    private ItemStack getOreBlockItem(OreBlock oreBlock) {
        Material itemType = Material.getMaterial(oreBlock.name);
        if (itemType == null) itemType = Material.BARRIER;
        ItemStack item = new ItemStack(itemType, 1);
        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(Utils.format("&2&lChance: &a&l%,.2f", oreBlock.chance));
        lore.add(Utils.format("&oLeft click &7&oto edit chance"));
        lore.add(Utils.format("&oRight click &7&oto remove block"));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    private void drawOreBlock(InventoryContents contents, OreBlock oreBlock) {
        Consumer<InventoryClickEvent> oreBlockEdit = event -> {
            if (event.isLeftClick()) {
                Player player = (Player)event.getWhoClicked();
                contents.inventory().close(player);
                getEditChanceGUI(contents, oreBlock).open(player);
            } else if (event.isRightClick()) {
                generator.removeBlock(oreBlock.name);
                event.setCurrentItem(null);
                editing = true;
                drawSave(contents);
            }
        };

        ItemStack item = getOreBlockItem(oreBlock);
        Optional<SlotPos> slot = contents.firstEmpty();
        if (slot.isPresent()) {
            contents.set(slot.get(), ClickableItem.of(item, oreBlockEdit));
        }
    }

    private void drawGlass(InventoryContents contents, int row, int column) {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(" ");
        item.setItemMeta(itemMeta);
        contents.set(row, column, ClickableItem.empty(item));
    }

    private void drawSave(InventoryContents contents) {
        ItemStack item = getSaveItem();
        Consumer<InventoryClickEvent> saveConsumer = event -> {
            if (getTotalChance() == 1.0) {
                plugin.getManager().updateOreGenerator(generator.name, generator);
                editing = false;
                drawSave(contents);
            }
        };
        contents.set(5, 4, ClickableItem.of(item, saveConsumer));
    }

    private void drawExit(InventoryContents contents) {
        ItemStack item = new ItemStack(Material.OAK_DOOR, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.format("&c&lBack"));
        item.setItemMeta(itemMeta);
        contents.set(5, 8, ClickableItem.of(item, event -> {
            Player player = (Player)event.getWhoClicked();
            plugin.getGUIManager().openGeneratorListGUI(player);
        }));
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        plugin.logDebug("EditGeneratorProvider.init: %s", generator.name);
        for (OreBlock oreBlock: generator.blocks) {
            drawOreBlock(contents, oreBlock);
        }

        for (int col = 0; col < 9; col++) {
            drawGlass(contents, 5, col);
        }
        drawSave(contents);
        drawExit(contents);
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        // plugin.logDebug("EditGeneratorProvider");
    }

}