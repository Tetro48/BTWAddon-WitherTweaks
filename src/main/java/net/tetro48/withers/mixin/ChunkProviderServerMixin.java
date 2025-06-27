package net.tetro48.withers.mixin;

import net.minecraft.src.ChunkProviderServer;
import net.minecraft.src.WorldServer;
import net.tetro48.withers.WitherChunkLoaderData;
import net.tetro48.withers.WitherTweaksAddon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ChunkProviderServer.class)
public abstract class ChunkProviderServerMixin {
	@Shadow private WorldServer worldObj;

	@Inject(method = "unloadChunksIfNotNearSpawn", at = @At("HEAD"), cancellable = true)
	private void forceChunkLoad(int par1, int par2, CallbackInfo ci) {
		List<WitherChunkLoaderData> witherChunkLoaderList = this.worldObj.getData(WitherTweaksAddon.CHUNK_LOADER_LIST).witherChunkLoaders;
		int iChunkViewDistance = this.worldObj.getMinecraftServer().getConfigurationManager().getViewDistance();
		for (WitherChunkLoaderData witherChunkLoader : witherChunkLoaderList) {
			int witherChunkX = witherChunkLoader.chunkX;
			int witherChunkZ = witherChunkLoader.chunkZ;
			if (par1 >= witherChunkX - iChunkViewDistance && par1 <= witherChunkX + iChunkViewDistance && par2 >= witherChunkZ - iChunkViewDistance && par2 <= witherChunkZ + iChunkViewDistance) {
				ci.cancel();
			}
		}
	}
}
