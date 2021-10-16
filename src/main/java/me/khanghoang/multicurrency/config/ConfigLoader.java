package me.khanghoang.multicurrency.config;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author khanghh on 2021/10/10
 */
public class ConfigLoader {
   
    public static MySQLConfig loadMySQLConfig(YamlConfiguration config) {
        MySQLConfig ret = new MySQLConfig();
        ret.tablePrefix = config.getString("mysql.tablePrefix", "bank_");
        ret.host = config.getString("mysql.host", "localhost");
        ret.port = config.getString("mysql.port", "3306");
        ret.database = config.getString("mysql.database");
        ret.username = config.getString("mysql.username");
        ret.password = config.getString("mysql.password");
        ret.maxPoolSize = config.getInt("mysql.maxPoolSize");
        return ret;
    }
}
