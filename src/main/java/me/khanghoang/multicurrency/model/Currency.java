package me.khanghoang.multicurrency.model;

import java.util.List;
import java.util.Map;

/**
 * @author khanghh on 2021/10/10
 */
public class Currency {
    private String name;
    private String symbol;
    private String item;
    private List<String> lores;
    private Map<Integer, String> denominations;
    private boolean isDefault;

    public Currency(String name, String symbol, String item, List<String> lores, boolean isDefault) {
       this.name = name;
        this.symbol = symbol;
        this.item = item;
        this.lores = lores;
        this.isDefault = isDefault;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getItem() {
        return item;
    }

    public List<String> getLores() {
        return lores;
    }

    public boolean isDefault() {
        return isDefault;
    }
}
