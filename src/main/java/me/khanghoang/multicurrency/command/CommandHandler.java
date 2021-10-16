package me.khanghoang.multicurrency.command;

import org.bukkit.command.TabExecutor;

/**
 * @author khanghh on 2021/05/23
 */
public interface CommandHandler extends TabExecutor {
    
    String getPermission();
    
    String getHelpMessage();
}
