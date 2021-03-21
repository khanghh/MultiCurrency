package me.khanghoang.oregen;

/**
 * @author khanghh on 2021/03/17
 */
public class OreBlock {
    public String name;
    public double chance;
    public OreBlock(String name, double chance) {
        this.name = name;
        this.chance = chance;
    }

    public String getName() {
        return name;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }

    public int hashCode() {
        return name.hashCode();
    }

    public boolean equals(Object obj) {
        return (obj instanceof OreBlock && ((OreBlock) obj).name == this.name);
    }

    public OreBlock clone() {
        return new OreBlock(name, chance);
    }
}
