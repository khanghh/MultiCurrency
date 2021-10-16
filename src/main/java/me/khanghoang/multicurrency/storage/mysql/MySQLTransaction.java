package me.khanghoang.multicurrency.storage.mysql;

import me.khanghoang.multicurrency.storage.BankTransaction;

import java.sql.Connection;

/**
 * @author khanghh on 2021/05/23
 */
public class MySQLTransaction implements BankTransaction {
    private Connection conn;

    public MySQLTransaction(Connection conn) {
        this.conn = conn;
    }
    
    @Override
    public void commit() throws Exception {
        conn.commit();
    }

    @Override
    public void rollback() throws Exception {
        conn.rollback();
    }

    @Override
    public void close() throws Exception {
        conn.close();
    }
    
    @Override
    public MySQLTransaction transferMoney(String uuid, int amount) throws Exception {
        return this;
    }

}
