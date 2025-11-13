package net.tetro48.withers;

import net.minecraft.src.Entity;
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
		NBTTagList tagList = new NBTTagList("WitherChunkLoaders");

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

	public WitherChunkLoaderData addOrGetChunkLoader(Entity entity) {
		for (WitherChunkLoaderData tempChunkLoader : this.witherChunkLoaders) {
			if (tempChunkLoader.entityUUID.equals(entity.getUniqueID())) {
				return tempChunkLoader;
			}
		}
		WitherChunkLoaderData newChunkLoader = new WitherChunkLoaderData(entity.getUniqueID(), (int) (entity.posX/16), (int) (entity.posZ/16));
		this.witherChunkLoaders.add(newChunkLoader);
		return newChunkLoader;
	}

	public void updateChunkLoaderPosition(EntityWither wither) {
		WitherChunkLoaderData chunkLoaderData = addOrGetChunkLoader(wither);
		if (chunkLoaderData != null) {
			chunkLoaderData.chunkX = (int) (wither.posX / 16);
			chunkLoaderData.chunkZ = (int) (wither.posZ / 16);
		}
	}
}
