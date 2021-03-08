package me.khanghoang.myinventory.storage.mysql;

import com.zaxxer.hikari.HikariDataSource;
import me.khanghoang.myinventory.Main;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseUpdateTask2 {

    public interface DatabaseUpdateCallback {
        void call(SQLException err);
    }
    private HikariDataSource hikari;
    private Main plugin;
    private DatabaseUpdateCallback callbackFunc;

    private static final String ITEM_SCHEMA = "CREATE TABLE IF NOT EXISTS %s (" + 
    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4_general_ci";

    private static final int CURRENT_DATABASE_VERSION = 4;

    public DatabaseUpdateTask2(Main plugin, HikariDataSource hikari) {
        this.plugin = plugin;
        this.hikari = hikari;
    }
    
    public void runAsync(String itemsTable, DatabaseUpdateCallback callbackFunc) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try (Connection connection = hikari.getConnection()) {
                    String query = String.format(ITEM_SCHEMA, itemsTable);
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.execute();
                    callbackFunc.call(null);
                } catch (SQLException err) {
                    callbackFunc.call(err);
                }
            }
        }.runTaskAsynchronously(this.plugin);
    }
}
