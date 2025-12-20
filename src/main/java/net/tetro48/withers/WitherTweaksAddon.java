package net.tetro48.withers;

import api.AddonHandler;
import api.BTWAddon;
import api.config.AddonConfig;
import api.world.data.DataEntry;
import api.world.data.DataProvider;
import net.minecraft.src.NBTTagList;

import java.util.Map;

public class WitherTweaksAddon extends BTWAddon {
	private static WitherTweaksAddon instance;

	public static boolean isDormantWitherEnabled = true;
	public static boolean canDormantWitherGrief = false;

	private static final String CHUNK_LOADER_NAME = "WitherChunkLoaders";
	public static final DataEntry.WorldDataEntry<WitherChunkLoaderList> CHUNK_LOADER_LIST = DataProvider
			.getBuilder(WitherChunkLoaderList.class)
			.name(CHUNK_LOADER_NAME)
			.defaultSupplier(WitherChunkLoaderList::new)
			.readNBT((tag) -> {
		NBTTagList listTag = tag.getTagList(CHUNK_LOADER_NAME);
		return new WitherChunkLoaderList(listTag);
	})
			.writeNBT((tag, pointList) -> tag.setTag(CHUNK_LOADER_NAME, pointList.saveToNBT()))
			.world()
			.filename("FCWorldData")
			.build();
	public WitherTweaksAddon() {
		super();
	}

	@Override
	public void registerConfigProperties(AddonConfig config) {
		config.registerBoolean("dormant-wither-toggle", true, "This toggles the summoning of a wither that's exclusively for chunk loading. Turn this off if you're running on a server where this addon is not expected for the client.");
		config.updatePath("DormantWitherToggle", "dormant-wither-toggle");
		config.registerBoolean("dormant-wither-grief-toggle", false, "This toggles the block damage from exploding Dormant Withers");
		config.updatePath("DormantWitherGriefToggle", "dormant-wither-grief-toggle");
	}

	@Override
	public void handleConfigProperties(AddonConfig config) {
		isDormantWitherEnabled = config.getBoolean("dormant-wither-toggle");
		canDormantWitherGrief = config.getBoolean("dormant-wither-grief-toggle");
	}

	@Override
	public void initialize() {
		AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
		CHUNK_LOADER_LIST.register();
	}
}