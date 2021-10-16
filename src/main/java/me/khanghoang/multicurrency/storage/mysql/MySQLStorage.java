package me.khanghoang.multicurrency.storage.mysql;

import com.zaxxer.hikari.HikariDataSource;
import me.khanghoang.multicurrency.MultiCurrency;
import me.khanghoang.multicurrency.config.MySQLConfig;
import me.khanghoang.multicurrency.log.MyLogger;
import me.khanghoang.multicurrency.storage.BankStorage;
import me.khanghoang.multicurrency.storage.BankTransaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents a database type of storage.
 */
public class MySQLStorage implements BankStorage {

    private MyLogger logger = MyLogger.getLogger(this.getClass().getName());
    
    private HikariDataSource hikari;
    protected String SQL_LOOKUP_MONEY = "SELECT * FROM %s WHERE playername=? and currency=?";
    protected String GET_ALL_POINTS = "SELECT point, server FROM %s WHERE playername=?";
    protected String GET_PLAYERS = "SELECT playername FROM %s WHERE server=?;";
    protected String INSERT_RECORD = "INSERT INTO %s (point,playername,server) VALUES(?,?,?);";
    protected String REMOVE_RECORD = "DELETE %s WHERE playername=? and server=?";
    protected String UPDATE_PLAYER_POINT = "UPDATE %s SET point=? WHERE playername=? and server=?";
    protected String CREATE_POINT_TABLE = "CREATE TABLE IF NOT EXISTS %s (" +
            "id INT UNSIGNED NOT NULL AUTO_INCREMENT, " +
            "playername varchar(36) NOT NULL, " +
            "money INT NOT NULL, " +
            "point INT NOT NULL, " +
            "server varchar(36) NOT NULL," +
            "PRIMARY KEY(id), " +
            "UNIQUE(playername, server));";
    
    public MySQLStorage(MySQLConfig config) {
        setupQueries(config.tablePrefix);
        setupDataSource(config);
        initialize();
    }

    private void setupQueries(String tablePrefix) {
        String pointTable = tablePrefix + "point";
        String logTable = tablePrefix + "log";
        GET_ALL_POINTS = String.format(GET_ALL_POINTS, pointTable);
        SQL_LOOKUP_MONEY = String.format(SQL_LOOKUP_MONEY, pointTable);
        GET_PLAYERS = String.format(GET_PLAYERS, pointTable);
        INSERT_RECORD = String.format(INSERT_RECORD, pointTable);
        REMOVE_RECORD = String.format(REMOVE_RECORD, pointTable);
        UPDATE_PLAYER_POINT = String.format(UPDATE_PLAYER_POINT, pointTable);
        CREATE_POINT_TABLE = String.format(CREATE_POINT_TABLE, pointTable);
    }

    private void setupDataSource(MySQLConfig config) {
        hikari = new HikariDataSource();
        hikari.setPoolName("PointBank Pool");
        hikari.setMaximumPoolSize(config.maxPoolSize);
        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", config.host);
        if (!config.port.isEmpty()) {
            hikari.addDataSourceProperty("port", config.port);
        }
        hikari.addDataSourceProperty("databaseName", config.database);
        hikari.addDataSourceProperty("user", config.username);
        hikari.addDataSourceProperty("password", config.password);
    }

    private void initialize() {
        logger.debug("Initialize database");
        try (Connection conn = hikari.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(CREATE_POINT_TABLE);
            stmt.execute();
        } catch (SQLException ex) {
            throw new RuntimeException("Could not create MySQL table.", ex);
        }
    }

    @Override
    public BankTransaction newTransaction() throws SQLException {
        Connection conn = hikari.getConnection();
        return new MySQLTransaction(conn);
    }
    
    public int lookup(String pUuid, String currency) {
        try (Connection conn = hikari.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SQL_LOOKUP_MONEY);
            stmt.setString(1, pUuid);
            stmt.setString(2, currency);
            ResultSet result = stmt.executeQuery();
            result.
        } catch (Exception ex) {
            throw new RuntimeException("Could not lookup player balance.", ex);
        }
    }
    
}