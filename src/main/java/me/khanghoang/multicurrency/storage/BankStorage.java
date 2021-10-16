package me.khanghoang.multicurrency.storage;

/**
 * @author khanghh on 2021/05/23
 */
public interface BankStorage {
    BankTransaction newTransaction() throws Exception;
}
