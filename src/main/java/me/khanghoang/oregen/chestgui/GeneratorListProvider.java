package me.khanghoang.oregen.chestgui;

import me.khanghoang.oregen.Main;
import me.khanghoang.smartinv.ClickableItem;
import me.khanghoang.smartinv.content.InventoryContents;
import me.khanghoang.smartinv.content.InventoryProvider;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author khanghh on 2021/02/21
 */
public class GeneratorListProvider implements InventoryProvider {

    private ItemStack menuBg = null;
    Main plugin;

    public GeneratorListProvider(Main plugin) {
        this.plugin = plugin;
        menuBg = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemMeta meta = menuBg.getItemMeta();
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        menuBg.setItemMeta(meta);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(5, 0, ClickableItem.empty(menuBg));
        contents.set(5, 8, ClickableItem.empty(menuBg));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
    }

}