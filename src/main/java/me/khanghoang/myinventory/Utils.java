package me.khanghoang.myinventory;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

public class Utils {

    public static String joinParam(String[] text, int to, int from) {
        return StringUtils.join(text, ' ', to, from).replace("'", "");
    }

    public static String format(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static int parseInt(String strValue, int defaultValue) {
        try {
            return Integer.parseInt(strValue);
        } catch(NumberFormatException ex) {
            return defaultValue; //Use default value if parsing failed
        }
    }
    
}
