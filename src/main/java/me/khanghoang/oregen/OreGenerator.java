package me.khanghoang.oregen;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.*;

/**
 * @author khanghh on 2021/03/17
 */

public class OreGenerator {
    public Set<OreBlock> blocks;
    public String name;
    public String label;
    public int islandLevel = 0;
    public boolean isDefault = false;
    public int rank = 0;
    public long lastUsed = 0;
    
    public OreGenerator() {
        this.blocks = new HashSet<>();
        this.name = null;
    }

    public OreGenerator(String name, String label, int islandLevel, Set<OreBlock> blocks, boolean isDefault) {
        this.name = name;
        this.label = label;
        this.islandLevel = islandLevel;
        this.blocks = blocks;
        this.isDefault = isDefault;
    }
    
    public OreBlock generate() {
        Random random = new Random();
        double val = random.nextDouble();
        for (OreBlock block: blocks) {
            if ((val -= block.getChance()) < 0) {
                return block;
            }
        }
        return new OreBlock("COBBLESTONE", 0);
    }

    public OreGenerator clone() {
        return new OreGenerator(name, label, islandLevel, blocks, isDefault);
    }
}
