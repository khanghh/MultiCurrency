package me.khanghoang.oregen.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import me.khanghoang.oregen.Utils;

public enum OreGenMessage {
    DEBUG_TOGGLED("debug has been %s"),
    NO_PERMISSION("You do not have permission to use this."),
    NOT_PLAYER("You must be a player to execute this command."),
    GENERATOR_NOT_FOUND("Ore generator &f%s &anot found."),
    GENERATOR_REMOVED("Ore generator &f%s &aremoved."),
    PLAYER_OFFLINE("Player is offline."),
    CONFIG_RELOADED("Config reloaded!");

    public static final String PREFIX = "&f[&cAdvancedOreGen&f] &7» &a";
    private final String text;

    OreGenMessage(String text2) {
        this.text = text2;
    }

    public String toString() {
        return ChatColor.translateAlternateColorCodes('&', PREFIX + this.text);
    }

    public void send(CommandSender sender) {
        sender.sendMessage(toString());
    }

    public void send(CommandSender sender, Object... object) {
        sender.sendMessage(Utils.format(toString(), object));
    }
}