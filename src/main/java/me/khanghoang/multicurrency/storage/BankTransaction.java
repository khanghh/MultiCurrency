package me.khanghoang.multicurrency.storage;

/**
 * @author khanghh on 2021/05/23
 */
public interface BankTransaction {

    void commit() throws Exception;
    
    void rollback() throws Exception;

    void close() throws Exception;

    BankTransaction setMoney(String uuid, String currency, int amount) throws Exception;
    
    BankTransaction transferMoney(String uuid, String currency, int amount) throws Exception;

}
