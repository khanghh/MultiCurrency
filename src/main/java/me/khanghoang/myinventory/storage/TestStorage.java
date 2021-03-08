package me.khanghoang.myinventory.storage;

import me.khanghoang.myinventory.Main;

/**
 * @author khanghh on 2021/03/07
 */
public class TestStorage implements IDataStorage {
    
    private Main plugin;
    
    public TestStorage(Main plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void shutdown() {
        
    }
}
