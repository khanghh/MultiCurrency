package me.khanghoang.myinventory.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author khanghh on 2021/02/21
 */
public class LargeChestProvider implements InventoryProvider {

    public LargeChestProvider() {
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();

        ClickableItem[] items = new ClickableItem[22];

        for(int i = 0; i < items.length; i++)
            items[i] = ClickableItem.empty(new ItemStack(Material.CHORUS_FRUIT, i));

        pagination.setItems(items);
        pagination.setItemsPerPage(7);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));

        contents.set(5, 0, ClickableItem.of(new ItemStack(Material.ARROW),
                e -> contents.inventory().open(player, pagination.previous().getPage())));
        contents.set(5, 8, ClickableItem.of(new ItemStack(Material.ARROW),
                e -> contents.inventory().open(player, pagination.next().getPage())));
    }

    @Override
    public void update(Player player, InventoryContents contents) {}

}