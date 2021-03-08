package me.khanghoang.myinventory.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public enum MyInventoryMessage {
    DEBUG_TOGGLED("debug has been %s"),
    NO_PERMISSION("You do not have permission to use this.");

    public static final String PREFIX = "&7[&aMyInventory&7] » &a";
    private final String text;

    MyInventoryMessage(String text2) {
        this.text = text2;
    }

    public String toString() {
        return ChatColor.translateAlternateColorCodes('&', PREFIX + this.text);
    }

    public void send(CommandSender sender) {
        sender.sendMessage(toString());
    }

    public void send(CommandSender sender, Object... object) {
        sender.sendMessage(String.format(toString(), object));
    }
}