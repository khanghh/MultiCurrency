package me.khanghoang.myinventory.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import me.khanghoang.myinventory.storage.IDataStorage;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author khanghh on 2021/02/21
 */
public class MainInventoryProvider implements InventoryProvider {

    private IDataStorage dataStorage;
    private Player owner;

    public MainInventoryProvider(Player owner, IDataStorage dataStorage) {
        this.owner = owner;
        this.dataStorage = dataStorage;
    }

    @Override
    public void init(Player viewer, InventoryContents contents) {
        Pagination pagination = contents.pagination();

        ClickableItem[] items = new ClickableItem[156];

        for(int i = 0; i < items.length; i++)
            items[i] = ClickableItem.empty(new ItemStack(Material.CHORUS_FRUIT, 1));

        pagination.setItems(items);
        pagination.setItemsPerPage(52);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

        contents.set(5, 0, ClickableItem.of(new ItemStack(Material.ARROW),
                e -> contents.inventory().open(viewer, pagination.previous().getPage())));
        contents.set(5, 8, ClickableItem.of(new ItemStack(Material.ARROW),
                e -> contents.inventory().open(viewer, pagination.next().getPage())));
    }

    @Override
    public void update(Player viewer, InventoryContents contents) {}

}