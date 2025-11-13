package net.tetro48.withers;

import btw.AddonHandler;
import btw.BTWAddon;
import btw.world.util.data.DataEntry;
import btw.world.util.data.DataProvider;
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
	public void preInitialize() {
		registerProperty("DormantWitherToggle", "True", "This toggles the summoning of a wither that's exclusively for chunk loading. Turn this off if you're running on a server where this addon is not expected for the client.");
		registerProperty("DormantWitherGriefToggle", "False", "This toggles the block damage from exploding Dormant Withers");
	}

	@Override
	public void handleConfigProperties(Map<String, String> propertyValues) {
		isDormantWitherEnabled = Boolean.parseBoolean(propertyValues.get("DormantWitherToggle"));
		canDormantWitherGrief = Boolean.parseBoolean(propertyValues.get("DormantWitherGriefToggle"));
	}

	@Override
	public void initialize() {
		AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
		CHUNK_LOADER_LIST.register();
	}
}