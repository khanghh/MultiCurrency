package me.khanghoang.multicurrency.misc;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.khanghoang.multicurrency.MultiCurrency;
import org.bukkit.OfflinePlayer;

/**
 * @author khanghh on 2021/05/23
 */

public class PlayerPointPlaceholder extends PlaceholderExpansion {

    MultiCurrency plugin;

    public PlayerPointPlaceholder(MultiCurrency plugin) {
        this.plugin = plugin;
    }

    // This tells PlaceholderAPI to not unregister your expansion on reloads since
    // it is provided by the dependency
    // Introduced in PlaceholderAPI 2.8.5
    @Override
    public boolean persist() {
        return true;
    }

    // Our placeholders will be %points_<params>%
    @Override
    public String getIdentifier() {
        return "points";
    }

    // the author
    @Override
    public String getAuthor() {
        return "khanghoang";
    }

    // This is the version
    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, String label) {
//        OreGenerator generator = plugin.getManager().getPlayerGenerator(player.getUniqueId());
//        switch (label) {
//            case "points":
//                return generator.name;
//            case "label":
//                return generator.label;
//            case "symbol":
//                return generator.symbol;
//            case "item":
//                return generator.item;
//            case "permission":
//                return String.format("oregen.%s", generator.name);
//        }
        return null;
    }
}
