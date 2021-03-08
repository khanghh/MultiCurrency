package me.khanghoang.myinventory.storage.mysql;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.scheduler.BukkitRunnable;
import me.khanghoang.myinventory.Main;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseUpdateTask extends BukkitRunnable {

    public interface DatabaseUpdateCallback {
        void call(SQLException err);
    }
    private HikariDataSource hikari;
    private String itemsTable;
    private DatabaseUpdateCallback callbackFunc;

    private static final String ITEM_SCHEMA = "CREATE TABLE IF NOT EXISTS %s (" + 
    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4_general_ci";

    private static final int CURRENT_DATABASE_VERSION = 4;

    public DatabaseUpdateTask(HikariDataSource hikari, String itemsTable, DatabaseUpdateCallback callbackFunc) {
        this.itemsTable = itemsTable;
        this.hikari = hikari;
        this.callbackFunc = callbackFunc;
    }
    
    private void callback(SQLException err) {
        if (this.callbackFunc != null) {
            this.callbackFunc.call(err);
        }
    }

    @Override
    public void run() {
        try (Connection connection = hikari.getConnection()) {
            String query = String.format(ITEM_SCHEMA, this.itemsTable);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            callback(null);
        } catch (SQLException err) {
            callback(err);
        }
    }

}
