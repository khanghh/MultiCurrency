package me.khanghoang.multicurrency;

import me.khanghoang.multicurrency.log.MyLogger;
import me.khanghoang.multicurrency.storage.BankStorage;
import me.khanghoang.multicurrency.storage.BankTransaction;

/**
 * @author khanghh on 2021/10/09
 */
public class BankManager {

    private final MyLogger logger = MyLogger.getLogger(BankManager.class.getName());
    
    private final BankStorage bankStorage;
    
    public BankManager(BankStorage bankStorage) {
        this.bankStorage = bankStorage;
    }
    
    public void giveMoney(String uuid, String currency, int amount) throws Exception {
        BankTransaction transaction = this.bankStorage.newTransaction();
        try {
            transaction
                .transferMoney(uuid, amount)
                .transferMoney(uuid, amount)
                .commit();
        }
        catch (Exception ex) {
            logger.error("Transaction rolled back.", ex);
            transaction.rollback();
            throw ex;
        }
        finally {
            transaction.close();
        }
    }
    
}
