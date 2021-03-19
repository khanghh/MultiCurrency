package me.khanghoang.oregen.hooks;

/**
 * Stores all existing hooks for sky block plugins
 * 
 * @author MasterCake
 *
 */
public enum HookInfo {

	AcidIsland(HookAcidIsland.class),
    ASkyBlock(HookASkyBlock.class),
	IslandWorld(HookIslandWorld.class),
	SuperiorSkyblock2(HookSuperiorSkyblock.class),
    uSkyBlock(HookuSkyBlock.class),
	FabledSkyBlock(HookFabledSkyblock.class), 
    PlotSquared(HookPlotSquared.class), 
    PlotSquaredLegacy(HookPlotSquaredLegacy.class),
    Vanilla(HookVanilla.class);

	private Class<?> hookClass;

	HookInfo(Class<?> hookClass) {
		this.hookClass = hookClass;
	}

	public Class<?> getHookClass() {
		return this.hookClass;
	}
}
