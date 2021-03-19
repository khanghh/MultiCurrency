package me.khanghoang.oregen.chestgui;

import me.khanghoang.smartinv.ClickableItem;
import me.khanghoang.smartinv.content.InventoryContents;
import me.khanghoang.smartinv.content.InventoryProvider;
import me.khanghoang.smartinv.content.Pagination;
import me.khanghoang.smartinv.content.SlotIterator;

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

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));

        contents.set(5, 0, ClickableItem.from(new ItemStack(Material.ARROW),
                e -> contents.inventory().open(player, pagination.previous().getPage())));
        contents.set(5, 8, ClickableItem.from(new ItemStack(Material.ARROW),
                e -> contents.inventory().open(player, pagination.next().getPage())));
    }

    @Override
    public void update(Player player, InventoryContents contents) {}

}