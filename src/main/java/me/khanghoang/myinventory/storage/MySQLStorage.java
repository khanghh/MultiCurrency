package me.khanghoang.myinventory.storage;

import com.zaxxer.hikari.HikariDataSource;
import me.khanghoang.myinventory.Main;
import me.khanghoang.myinventory.config.Configuration.MySQLConfig;
import me.khanghoang.myinventory.storage.mysql.DatabaseUpdateTask;
import me.khanghoang.myinventory.storage.mysql.DatabaseUpdateTask2;

import java.sql.SQLException;

public class MySQLStorage implements IDataStorage {

    private Main plugin;
    private HikariDataSource hikari;

    public MySQLStorage(Main plugin) {
        this.plugin = plugin;
        init();
    }

    private void init() {
        shutdown();
        MySQLConfig mysqlConfig = plugin.getConfig().getMySQLConfig();
        hikari = new HikariDataSource();
        hikari.setMaximumPoolSize(mysqlConfig.maxPoolSize);
        hikari.setPoolName("MyInventory");
        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("useSSL", false);
        hikari.addDataSourceProperty("requireSSL", false);
        hikari.addDataSourceProperty("verifyServerCertificate", false);
        hikari.addDataSourceProperty("serverName", mysqlConfig.hostname);
        hikari.addDataSourceProperty("port", mysqlConfig.port);
        hikari.addDataSourceProperty("databaseName", mysqlConfig.databaseName);
        hikari.addDataSourceProperty("user", mysqlConfig.user);
        hikari.addDataSourceProperty("password", mysqlConfig.password);

        new DatabaseUpdateTask2(plugin, hikari).runAsync("aaa", (SQLException err) -> {
            if (err != null) {
                handleSQLException(err);
                MySQLStorage.this.shutdown();
            }
        });
    }

    private void handleSQLException(SQLException error) {
        if (plugin.getConfig().isDebug()) {
            error.printStackTrace();
        } else {
            plugin.logError("MySQL Query Failed - Reason: " + error.getMessage());
        }
    }

    @Override
    public void shutdown() {
        if (hikari != null) {
            hikari.close();
        }
    }
}
