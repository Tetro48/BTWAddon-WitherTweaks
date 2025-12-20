package net.tetro48.withers.mixin;

import api.world.data.DataEntry;
import net.minecraft.src.*;
import net.tetro48.withers.WitherChunkLoaderData;
import net.tetro48.withers.WitherTweaksAddon;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;

@Mixin(WorldServer.class)
public abstract class WorldServerMixin extends World {

	public WorldServerMixin(ISaveHandler par1ISaveHandler, String par2Str, WorldProvider par3WorldProvider, WorldSettings par4WorldSettings, Profiler par5Profiler, ILogAgent par6ILogAgent) {
		super(par1ISaveHandler, par2Str, par3WorldProvider, par4WorldSettings, par5Profiler, par6ILogAgent);
	}

	@Shadow public abstract <T> T getData(DataEntry.WorldDataEntry<T> entry);

	@Inject(method = "updateActiveChunkMap", at = @At("RETURN"))
	private void addChunkLoaders(CallbackInfo ci) {
		List<WitherChunkLoaderData> witherChunkLoaderList = getData(WitherTweaksAddon.CHUNK_LOADER_LIST).witherChunkLoaders;
		for (WitherChunkLoaderData witherChunkLoader : witherChunkLoaderList) {
			addAreaAroundChunkToActiveChunkMap(witherChunkLoader.chunkX, witherChunkLoader.chunkZ);
		}
	}
	@Inject(method = "modUpdateTick", at = @At("TAIL"))
	private void validateChunkLoaderList(CallbackInfo ci) {

		// periodically check the chunk loader list for dead entities

		long lWorldTime = getWorldTime();

		if ( ( lWorldTime & 15 ) != 0 ) return;

		Iterator<WitherChunkLoaderData> tempIterator = this.getData(WitherTweaksAddon.CHUNK_LOADER_LIST).witherChunkLoaders.iterator();
		while (tempIterator.hasNext()) {
			WitherChunkLoaderData chunkLoaderData = tempIterator.next();
			Entity currentEntity = null;
			for (Object entityObject : this.loadedEntityList) {
				Entity entity = (Entity) entityObject;
				if (chunkLoaderData.entityUUID.equals(entity.getUniqueID())) {
					currentEntity = entity;
					break;
				}
			}
			if (currentEntity != null && currentEntity.isEntityAlive()) {
				chunkLoaderData.chunkX = (int) (currentEntity.posX/16);
				chunkLoaderData.chunkZ = (int) (currentEntity.posZ/16);
			}
			else {
				// remove chunk loader if entity not found, or is dead
				tempIterator.remove();
			}
		}
	}
}
