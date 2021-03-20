package me.khanghoang.oregen.misc;

import me.khanghoang.oregen.Main;
import me.khanghoang.oregen.OreGenerator;
import org.bukkit.OfflinePlayer;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class NamePlaceholder extends PlaceholderExpansion {

	Main plugin;

	public NamePlaceholder(Main plugin) {
		this.plugin = plugin;
	}

	// This tells PlaceholderAPI to not unregister your expansion on reloads since it is provided by the dependency
	// Introduced in PlaceholderAPI 2.8.5
	@Override
	public boolean persist() {
		return true;
	}

	// Our placeholders will be %oregen_<params>%
	@Override
	public String getIdentifier() {
		return "oregen";
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
		if(!label.startsWith("generator.")) {
			return null;
		}
		
		OreGenerator generator = plugin.getManager().getPlayerGenerator(player.getUniqueId());
		switch(label.split("\\.")[1]) {
			case "name":
				return generator.name;
			case "label":
				return generator.label;
            case "item":
                return generator.item; 
			case "permission":
				return String.format("oregen.%s", generator.genId);
		}
		return null;
	}
}
