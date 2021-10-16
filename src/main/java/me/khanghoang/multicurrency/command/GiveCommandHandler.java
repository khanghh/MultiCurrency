package me.khanghoang.multicurrency.command;

import me.khanghoang.multicurrency.BankManager;
import me.khanghoang.multicurrency.locale.LocaleMessage;
import me.khanghoang.multicurrency.locale.LocaleTranslator;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * @author khanghh on 2021/05/23
 */
public class GiveCommandHandler implements CommandHandler {

    @Override
    public String getPermission() {
        return Permission.GIVE;
    }

    @Override
    public String getHelpMessage() {
        return LocaleTranslator.translate(LocaleMessage.HELP_GIVE);
    }
    
    public GiveCommandHandler() {
        BankManager bank = Bukkit.getServicesManager().load(BankManager.class);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(getHelpMessage());
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 2) {
            
        }
        return null;
    }

}
