package me.khanghoang.oregen;

import java.util.*;

/**
 * @author khanghh on 2021/03/17
 */

public class OreGenerator {
    public Set<OreBlock> blocks;
    public String name;
    public String item;
    public String label;
    public String symbol;
    public int islandLevel = 0;
    public boolean isDefault = false;
    public int rank = 0;
    public long lastUsed = 0;
    
    public OreGenerator() {
        this.blocks = new HashSet<>();
        this.label = null;
    }

    public OreGenerator(String name) {
        this.name = name;
        this.label = name;
        this.item = "COBBLESTONE";
        this.blocks = new HashSet<>();
    }

    public OreGenerator(String name, String item, String label, String symbol, int islandLevel, Set<OreBlock> blocks, boolean isDefault) {
        this.name = name;
        this.item = item;
        this.label = label;
        this.symbol = symbol;
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

    public boolean removeBlock(String blockName) {
        Iterator<OreBlock> it = blocks.iterator();
        while (it.hasNext()) {
            if (it.next().name.equals(blockName)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public boolean updateBlockChance(String blockName, double newChance) {
        for (OreBlock oreBlock : blocks) {
            if (oreBlock.name.equals(blockName)) {
                oreBlock.chance = newChance;
                return true;
            }
        }
        return false;
    }

    public OreGenerator clone() {
        Set<OreBlock> cloneBlocks = new HashSet<>();
        for (OreBlock item: blocks) {
            cloneBlocks.add(item.clone());
        }
        return new OreGenerator(name, item, label, symbol, islandLevel, cloneBlocks, isDefault);
    }
}
