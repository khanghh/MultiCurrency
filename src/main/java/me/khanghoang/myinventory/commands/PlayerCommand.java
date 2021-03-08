package me.khanghoang.myinventory.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import me.khanghoang.myinventory.Main;

public class PlayerCommand implements CommandExecutor, TabExecutor {

    private Main plugin;

    public PlayerCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("myinventory.use")) {
            MyInventoryMessage.NO_PERMISSION.send(sender);
            return false;
        }
        if (args.length == 0) {
            Player player = (Player) sender;
            plugin.getInventoryManager().getMainInventory(player).open(player);
            return true;
        } else {
        }
        return false;
    }

}
