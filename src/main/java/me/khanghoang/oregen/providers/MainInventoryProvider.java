package me.khanghoang.oregen.providers;

import me.khanghoang.smartinv.ClickableItem;

import me.khanghoang.smartinv.content.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author khanghh on 2021/02/21
 */
public class MainInventoryProvider implements InventoryProvider {

    private Player owner;
    private ClickableItem[] items;

    public MainInventoryProvider() {
        items = new ClickableItem[156];

        for(int i = 0; i < items.length; i++) {
            int val = ThreadLocalRandom.current().nextInt(1, 65);
            if (i % 2 == 0) {
                items[i] = ClickableItem.empty(new ItemStack(Material.AIR, val));
            } else {
                items[i] = ClickableItem.empty(new ItemStack(Material.CARROT, val));
            }
        }
    }

    @Override
    public void init(Player viewer, InventoryContents contents) {
        Pagination pagination = contents.pagination();

        pagination.setItems(items);

        SlotIterator it = contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0);
        it.blacklist(5,0);
        it.blacklist(5,8);
        pagination.addToIterator(it);
        contents.setEditable(new SlotPos(0,0), true);

        contents.set(5, 0, ClickableItem.from(new ItemStack(Material.ARROW),
                e -> contents.inventory().open(viewer, pagination.previous().getPage())));
        contents.set(5, 8, ClickableItem.from(new ItemStack(Material.ARROW),
                e -> contents.inventory().open(viewer, pagination.next().getPage())));
    }

    @Override
    public void update(Player viewer, InventoryContents contents) {}

}