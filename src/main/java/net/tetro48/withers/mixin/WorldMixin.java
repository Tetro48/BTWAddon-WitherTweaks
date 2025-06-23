package net.tetro48.withers.mixin;

import btw.world.util.data.DataEntry;
import net.minecraft.src.*;
import net.tetro48.withers.WitherChunkLoaderData;
import net.tetro48.withers.WitherTweaksAddon;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(World.class)
public abstract class WorldMixin {

	@Shadow public abstract <T> T getData(DataEntry<T> entry);

	@Shadow @Final public WorldProvider provider;

	@Shadow protected abstract void addAreaAroundChunkToActiveChunkMap(int iChunkX, int iChunkZ);

	@Inject(method = "updateActiveChunkMap", at = @At("RETURN"))
	private void addChunkLoaders(CallbackInfo ci) {
		List<WitherChunkLoaderData> witherChunkLoaderList = getData(WitherTweaksAddon.CHUNK_LOADER_LIST).witherChunkLoaders;
		for (WitherChunkLoaderData witherChunkLoader : witherChunkLoaderList) {
			if (witherChunkLoader.dimension == this.provider.dimensionId) {
				addAreaAroundChunkToActiveChunkMap(witherChunkLoader.chunkX, witherChunkLoader.chunkZ);
			}
		}
	}
}
