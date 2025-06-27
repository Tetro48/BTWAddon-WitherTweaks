package net.tetro48.withers;

import net.minecraft.src.EntityWither;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WitherChunkLoaderList {
	public List<WitherChunkLoaderData> witherChunkLoaders;

	public WitherChunkLoaderList() {
		this.witherChunkLoaders = new ArrayList<>();
	}

	public WitherChunkLoaderList(NBTTagList tagList) {
		this();
		this.loadFromNBT(tagList);
	}

	public void loadFromNBT(NBTTagList tagList) {
		this.witherChunkLoaders.clear();

		for(int iTempCount = 0; iTempCount < tagList.tagCount(); ++iTempCount) {
			NBTTagCompound tempCompound = (NBTTagCompound)tagList.tagAt(iTempCount);
			WitherChunkLoaderData newChunkLoader = new WitherChunkLoaderData(tempCompound);
			this.witherChunkLoaders.add(newChunkLoader);
		}

	}

	public NBTTagList saveToNBT() {
		NBTTagList tagList = new NBTTagList("WTWitherChunkLoaders");

		for (WitherChunkLoaderData witherChunkLoader : this.witherChunkLoaders) {
			NBTTagCompound tempTagCompound = new NBTTagCompound();
			witherChunkLoader.writeToNBT(tempTagCompound);
			tagList.appendTag(tempTagCompound);
		}

		return tagList;
	}

	public void removeChunkLoadersAt(UUID uuid) {
		this.witherChunkLoaders.removeIf(tempChunkLoader -> tempChunkLoader.entityUUID == uuid);
	}

	public void addChunkLoader(EntityWither wither) {
		for (WitherChunkLoaderData tempChunkLoader : this.witherChunkLoaders) {
			if (tempChunkLoader.entityUUID == wither.getUniqueID()) {
				return;
			}
		}
		WitherChunkLoaderData newChunkLoader = new WitherChunkLoaderData(wither.getUniqueID(), (int) (wither.posX/16), (int) (wither.posZ/16));
		this.witherChunkLoaders.add(newChunkLoader);
	}

	public void updateChunkLoaderPosition(EntityWither wither) {
		WitherChunkLoaderData chunkLoaderData = getWitherChunkLoaderWithUUID(wither.getUniqueID());
		if (chunkLoaderData != null) {
			chunkLoaderData.chunkX = (int) (wither.posX / 16);
			chunkLoaderData.chunkZ = (int) (wither.posZ / 16);
		}
	}

	public WitherChunkLoaderData getWitherChunkLoaderWithUUID(UUID uuid) {
		for(WitherChunkLoaderData tempChunkLoader : this.witherChunkLoaders) {
			if (tempChunkLoader.entityUUID == uuid) {
				return tempChunkLoader;
			}
		}

		return null;
	}

}
