package me.khanghoang.oregen.listeners;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import me.khanghoang.oregen.Main;
import me.khanghoang.oregen.OreBlock;
import me.khanghoang.oregen.OreGenerator;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class OreGenListener implements Listener {

    /*
     * CustomOreGen main class
     */
    private Main plugin;

    private boolean useLegacyBlockPlaceMethod;
    private boolean useLevelledClass;
    private Method legacyBlockPlaceMethod;

    public OreGenListener(Main plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        this.useLegacyBlockPlaceMethod = Arrays.stream(Block.class.getMethods())
            .anyMatch(method -> method.getName() == "setTypeIdAndData");
        if (this.useLegacyBlockPlaceMethod) {
            try {
                legacyBlockPlaceMethod = Block.class.getMethod("setTypeIdAndData", int.class, byte.class,
                    boolean.class);
            } catch (NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
        }

        try {
            Class.forName("org.bukkit.block.data.Levelled");
            useLevelledClass = true;
        } catch (ClassNotFoundException e) {
            useLevelledClass = false;
        }
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        Block fromBlock = event.getBlock();
        Block toBlock = event.getToBlock();
        World world = fromBlock.getLocation().getWorld();
        if (world == null) return;
        if (toBlock.getType() != Material.AIR) return;
        if (plugin.getManager().getDisabledWorlds().contains(world.getName())) return;

        List<World> islandWorlds = plugin.getSkyBlockAPICached().getActiveWorlds();
        if (!islandWorlds.contains(world)) return;

        BlockType fromType = this.getType(fromBlock);
        if (fromType != BlockType.WATER && fromType != BlockType.WATER_STAT) return;

        // fix for (lava -> water)
        // if (fromType == BlockType.LAVA || fromType == BlockType.LAVA_STAT) {
        //     if (!isSurroundedByWater(toBlock)) return;
        // }

        BlockType contactType = getContactType(toBlock);
        if (contactType == null) return;
        
        OfflinePlayer offlinePlayer = plugin.getSkyBlockAPICached().getIslandOwner(toBlock.getLocation());
        if (offlinePlayer == null) return;
        OreGenerator generator = plugin.getManager().getPlayerGenerator(offlinePlayer.getUniqueId());
        OreBlock winning = generator.generate();
        Material winningBlock = Material.getMaterial(winning.getName());
        if (winningBlock == null) return;

        event.setCancelled(true);

        // b.setType(Material.getMaterial(winning.getName()));
        // <Block>.setData(...) is deprecated, but there is no
        // alternative to it. #spigot
        if (useLegacyBlockPlaceMethod) {
            try {
                legacyBlockPlaceMethod.invoke(toBlock, winningBlock.getId(), 1, true);
                plugin.getSkyBlockAPICached().sendBlockAcknowledge(toBlock);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else {
            toBlock.setType(winningBlock);
            toBlock.getState().update(true);
            if (contactType == BlockType.LAVA || contactType == BlockType.LAVA_STAT) {
                toBlock.getWorld().playSound(toBlock.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1f, 10f);
            }
            plugin.getSkyBlockAPICached().sendBlockAcknowledge(toBlock);
        }
        // b.setData(winning.getDamage(), true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getManager().getPlayerGenerator(event.getPlayer().getUniqueId());
    }

    private enum BlockType {
        WATER, WATER_STAT, LAVA, LAVA_STAT, WOOD_FENCE
    }

    public BlockType getType(Block block) {
        if (block.getType() == Material.OAK_FENCE || block.getType() == Material.SPRUCE_FENCE
            || block.getType() == Material.BIRCH_FENCE || block.getType() == Material.JUNGLE_FENCE
            || block.getType() == Material.DARK_OAK_FENCE) {
            return BlockType.WOOD_FENCE;
        }
        if (useLevelledClass) {
            if (block.getBlockData() instanceof org.bukkit.block.data.Levelled) {
                org.bukkit.block.data.Levelled level = (org.bukkit.block.data.Levelled) block.getBlockData();
                if (level.getLevel() == 0) {
                    if (level.getMaterial() == Material.WATER) {
                        return BlockType.WATER_STAT;
                    } else if (level.getMaterial() == Material.LAVA) {
                        return BlockType.LAVA_STAT;
                    }
                } else {
                    if (level.getMaterial() == Material.WATER) {
                        return BlockType.WATER;
                    } else if (level.getMaterial() == Material.LAVA) {
                        return BlockType.LAVA;
                    }
                }
            }
        } else {
            switch (block.getType().name()) {
                case "WATER":
                    return BlockType.WATER;
                case "STATIONARY_WATER":
                    return BlockType.WATER_STAT;
                case "LAVA":
                    return BlockType.LAVA;
                case "STATIONARY_LAVA":
                    return BlockType.LAVA_STAT;
                case "OAK_FENCE":
                case "SPRUCE_FENCE":
                case "BIRCH_FENCE":
                case "JUNGLE_FENCE":
                case "DARK_OAK_FENCE":
                    return BlockType.WOOD_FENCE;
            }
        }
        return null;
    }

    private final BlockFace[] faces = {BlockFace.SELF, BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST,
        BlockFace.SOUTH, BlockFace.WEST};

    public BlockType getContactType(Block block) {
        for (BlockFace face : this.faces) {
            Block rblock = block.getRelative(face, 1);
            BlockType rblockType = this.getType(rblock);
            if (face != BlockFace.DOWN && rblockType == BlockType.LAVA || rblockType == BlockType.LAVA_STAT) {
                return rblockType;
            }
            if (face != BlockFace.UP && rblockType == BlockType.WOOD_FENCE) {
                return rblockType;
            } 
        }
        return null;
    }
}