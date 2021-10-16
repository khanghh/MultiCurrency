package me.khanghoang.multicurrency.command;

import me.khanghoang.multicurrency.MultiCurrency;
import me.khanghoang.multicurrency.locale.LocaleMessage;
import me.khanghoang.multicurrency.locale.LocaleTranslator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.*;

/**
 * Handles the commands for the root command.
 * 
 * @author Mitsugaru
 */
public class Commander implements TabExecutor {
    
    private final MultiCurrency plugin;
    
    public Map<String, CommandHandler> handlers = new HashMap<>();
    
    public Commander(MultiCurrency plugin) {
        this.plugin = plugin;
        // Register commands
        registerCommand("give", new GiveCommandHandler());
        registerCommand("take", new TakeCommandHandler());
        registerCommand("reload", new ReloadCommandHandler(plugin));
//        registerCommand("giveall", new GiveAllCommand());
//        registerCommand("take", new TakeCommand());
//        registerCommand("look", new LookCommand());
//        registerCommand("pay", new PayCommand());
//        registerCommand("set", new SetCommand());
//        registerCommand("broadcast", new BroadcastCommand());
//        registerCommand("reset", new ResetCommand());
//        registerCommand("me", new MeCommand());
//        registerCommand("transfer", new TransferCommand());
//        registerCommand("reload", new ReloadCommand());
//
//        // Register handlers
//        registerHandler(new LeadCommand(plugin));
    }
    
    public void registerCommand(String cmd, CommandHandler handler) {
        handlers.put(cmd,handler);
        if(handlers.containsKey(cmd)) {
            plugin.getLogger().warning("Replacing existing command: " + cmd);
        }
        handlers.put(cmd, handler);
    }
    
    public boolean sendUsage(CommandSender sender) {
        sender.sendMessage(LocaleTranslator.translate(LocaleMessage.HELP_HEADER));
        for (CommandHandler cmd : handlers.values()) {
            if (sender.hasPermission(cmd.getPermission())) {
                sender.sendMessage(cmd.getHelpMessage());
            }
        }
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0) {
            return sendUsage(sender);
        }
        final String subcmd = args[0].toLowerCase();
        final CommandHandler handler = handlers.get(subcmd);
        if(handler != null) {
            if (!sender.hasPermission(handler.getPermission())) {
                sender.sendMessage(LocaleTranslator.translate(LocaleMessage.PERMISSION_DENY));
                return false;
            }
            return handler.onCommand(sender, cmd, label, args);
        }
        sender.sendMessage(LocaleTranslator.translate(LocaleMessage.COMMAND_UNKNOWN));
        return false;
    }

    private List<String> getCmdSuggestions(String argument, List<String> availCmds) {
        argument = argument.toLowerCase();
        List<String> suggestions = new ArrayList<>();
        for (String suggestion : availCmds) {
            if (suggestion.toLowerCase().startsWith(argument)) {
                suggestions.add(suggestion);
            }
        }
        return suggestions;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> availCmds = new ArrayList<>();
            for (Map.Entry<String, CommandHandler> entry : handlers.entrySet()) {
                String cmd = entry.getKey();
                CommandHandler handler = entry.getValue();
                if (sender.hasPermission(handler.getPermission())) {
                    availCmds.add(cmd);
                }
            }
            return getCmdSuggestions(args[0], availCmds);
        } else if (args.length > 1) {
            final String subcmd = args[0].toLowerCase();
            final CommandHandler handler = handlers.get(subcmd);
            if(handler != null && sender.hasPermission(handler.getPermission())) {
                return handler.onTabComplete(sender, command, alias, args);
            }
        }
        return new ArrayList<>();
    }
}