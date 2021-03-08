package me.khanghoang.myinventory.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import me.khanghoang.myinventory.Main;
import me.khanghoang.myinventory.Utils;

import java.util.ArrayList;
import java.util.List;

public class AdminCommand implements CommandExecutor, TabExecutor {

    private Main plugin;

    public AdminCommand(Main plugin) {
        this.plugin = plugin;
    }

    private List<String> getSuggestions(String argument, String... array) {
        argument = argument.toLowerCase();
        List<String> suggestions = new ArrayList<>();
        for (String suggestion : array) {
            if (suggestion.toLowerCase().startsWith(argument)) {
                suggestions.add(suggestion);
            }
        }
        return suggestions;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return getSuggestions(args[0], "debug");
        } else if (args.length == 2) {
            List<String> suggestions = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                    suggestions.add(player.getName());
                }
            }
            return suggestions;
        }
        return new ArrayList<>();
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage(Utils.format("\n&7» &a&l MyInventory Admin Help &7«"));
        sender.sendMessage(Utils.format("     edit by &a&lKhangHoang"));
        sender.sendMessage(Utils.format("\n\n&fType a command to get started:"));
        sender.sendMessage(Utils.format("&7» &a/myinv <player>"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("myinventory.admin")) {
            MyInventoryMessage.NO_PERMISSION.send(sender);
            return false;
        }
        if (args.length < 1) {
            sendUsage(sender);
            return false;
        }
        switch (args[0].toLowerCase()) {
            case "debug":
                plugin.getConfig().toggleDebug();
                MyInventoryMessage.DEBUG_TOGGLED.send(sender, plugin.getConfig().isDebug() ? "&aENABLED" : "&cDISABLED");
                return true;
            default:
                sendUsage(sender);
                return false;
        }
    }

}
