package me.khanghoang.multicurrency.locale;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author khanghh on 2021/05/23
 */
public class LocaleTranslator {

    private static String TAG_VALUE = "";
    private static LocaleConfig config;
    
    public static void initialize(LocaleConfig config) {
        LocaleTranslator.config = config;
    }

    public static String translate(LocaleMessage node) {
        return translate(node, new HashMap<>());
    }
    
    public static String translate(LocaleMessage node, Map<String, String> replace) {
        String message = config.getString(node.getKey(), node.getValue());
        if (message != null) {
            LocaleMessage tagNode = LocaleMessage.TAG;
            String tagText = config.getString(tagNode.getKey(), tagNode.getValue());
            message = message.replaceAll(Placeholders.TAG, tagText);
            if(replace != null) {
                for(Map.Entry<String, String> entry : replace.entrySet()) {
                    message = message.replaceAll(entry.getKey(), entry.getValue());
                }
            }
            message = ChatColor.translateAlternateColorCodes('&', message);
        }
        return message;
    }
}
