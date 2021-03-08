package me.khanghoang.myinventory.config;

import java.io.File;

/**
 * @author khanghh on 2021/02/15
 */
public class Configuration extends YamlConfig {

    public class MySQLConfig {
        public String hostname;
        public int port;
        public String user;
        public String password;
        public String databaseName;
        public int maxPoolSize;
        public String itemsTable;
        public MySQLConfig() {}
    }

    private boolean debug;
    private int refreshInterval;
    private MySQLConfig mysqlConfig;

    public static Configuration loadFromFile(File file) {
        Configuration config = new Configuration(file);
        config.load();
        config.applyConfig();
        return config;
    }

    public Configuration(File file) {
        super(file);
    }

    public void reload() {
        this.load();
        this.applyConfig();
    }

    private void applyConfig() {
        this.debug = this.getBoolean("debug");
        String tablePrefix = this.getString("mysql.tablePrefix");
        MySQLConfig mysqlConfig = new MySQLConfig();
        mysqlConfig.hostname = this.getString("mysql.hostname");
        mysqlConfig.port = this.getInt("mysql.port", 3306);
        mysqlConfig.user = this.getString("mysql.user");
        mysqlConfig.password = this.getString("mysql.password");
        mysqlConfig.maxPoolSize = this.getInt("mysql.maxPoolSize", 10);
        mysqlConfig.itemsTable = tablePrefix + "items";
        this.mysqlConfig = mysqlConfig;
    }

    public boolean isDebug() {
        return debug;
    }

    public MySQLConfig getMySQLConfig() {
        return this.mysqlConfig;
    }

    public void toggleDebug() {
        debug = !debug;
        this.set("debug", debug);
        this.save();
    }

    public int refreshInterval() {
        return this.refreshInterval;
    }

    public void migrate(Configuration oldConfig) {
        for (String key : oldConfig.getKeys(false)) {
            if (this.contains(key)) {
                this.set(key, oldConfig.get(key));
            }
        }
        applyConfig();
    }
}
