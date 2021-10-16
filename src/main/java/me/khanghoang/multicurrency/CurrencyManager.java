package me.khanghoang.multicurrency;

import me.khanghoang.multicurrency.config.YamlConfig;
import me.khanghoang.multicurrency.storage.BankStorage;

/**
 * @author khanghh on 2021/10/10
 */
public class CurrencyManager {
    
    private final BankStorage bankStorage;
    
    private final YamlConfig curConfig;
    
    public CurrencyManager(BankStorage bankStorage, YamlConfig curConfig) {
        this.bankStorage = bankStorage;
        this.curConfig = curConfig;
    }
}
