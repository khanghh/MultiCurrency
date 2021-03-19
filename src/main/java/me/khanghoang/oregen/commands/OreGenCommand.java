package me.khanghoang.oregen.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import me.khanghoang.oregen.Main;
import me.khanghoang.oregen.Utils;

import java.util.ArrayList;
import java.util.List;

public class OreGenCommand implements CommandExecutor, TabExecutor {

    private Main plugin;

    public OreGenCommand(Main plugin) {
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
            return getSuggestions(args[0], "debug", "reload", "edit");
        }
        // } else if (args.length == 2) {
            // List<String> suggestions = new ArrayList<>();
            // for (Player player : Bukkit.getOnlinePlayers()) {
            //     if (player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
            //         suggestions.add(player.getName());
            //     }
            // }
            // return suggestions;
        // }
        return new ArrayList<>();
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage(Utils.format("\n&7AdvancedOreGen &c1.0.1"));
        sender.sendMessage(Utils.format("\n\n&fType a command to get started:"));
        sender.sendMessage(Utils.format("&a/oregen debug &7- Toggle debug mode"));
        sender.sendMessage(Utils.format("&a/oregen reload &7- Reload config"));
        sender.sendMessage(Utils.format("&a/oregen edit &7- Open edit generators GUI"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("oregen.admin")) {
            OreGenMessage.NO_PERMISSION.send(sender);
            return false;
        }
        if (args.length < 1) {
            sendUsage(sender);
            return false;
        }
        switch (args[0].toLowerCase()) {
            case "debug":
                plugin.getConfig().toggleDebug();
                OreGenMessage.DEBUG_TOGGLED.send(sender, plugin.getConfig().isDebug() ? "&aENABLED" : "&cDISABLED");
                return true;
            case "reload":
                plugin.loadConfig();
                plugin.getManager().reloadConfig();
                OreGenMessage.CONFIG_RELOADED.send(sender);
                return true;
            case "edit": 
                if (!(sender instanceof Player)) {
                    OreGenMessage.NOT_PLAYER.send(sender);
                }
                plugin.getGUIManager().getGeneratorListGUI().open((Player)sender);
                return true;
            default:
                sendUsage(sender);
                return false;
        }
    }

}
