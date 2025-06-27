package net.tetro48.withers.mixin;

import net.minecraft.src.*;
import net.tetro48.withers.WitherChunkLoaderList;
import net.tetro48.withers.WitherTweaksAddon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;

@Mixin(EntityWither.class)
public abstract class EntityWitherMixin extends EntityMob {
	@Unique
	public boolean isPowered = false;

	public EntityWitherMixin(World par1World) {
		super(par1World);
	}
	@Inject(method = "entityInit", at = @At("HEAD"))
	private void entityInit(CallbackInfo ci) {
		this.dataWatcher.addObject(21, Integer.MAX_VALUE);
	}
	@Inject(method = "writeEntityToNBT", at = @At("RETURN"))
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
		par1NBTTagCompound.setInteger("WTChunkLoadTicks", this.dataWatcher.getWatchableObjectInt(21));
	}

	@Inject(method = "readEntityFromNBT", at = @At("RETURN"))
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
		this.dataWatcher.updateObject(21, par1NBTTagCompound.getInteger("WTChunkLoadTicks"));
		isPowered = getChunkLoadSeconds() > 0;
	}
	@Unique
	public int getChunkLoadSeconds() {
		return this.dataWatcher.getWatchableObjectInt(21);
	}
	@Unique
	public void setChunkLoadSeconds(int seconds) {
		this.dataWatcher.updateObject(21, seconds);
	}
	@Unique
	public void addChunkLoadSeconds(int seconds) {
		this.dataWatcher.updateObject(21, getChunkLoadSeconds() + seconds);
	}

	@Inject(method = "onLivingUpdate", at = @At("TAIL"))
	public void injectLivingUpdate(CallbackInfo ci) {
		if (ticksExisted % 20 == 0) {
			if (getChunkLoadSeconds() > 0) {
				if (getChunkLoadSeconds() < Integer.MAX_VALUE) {
					addChunkLoadSeconds(-1);
				}
				if (!isPowered) {
					isPowered = true;
					loadChunks();
				}
				WitherChunkLoaderList chunkLoaderList = worldObj.getData(WitherTweaksAddon.CHUNK_LOADER_LIST);
				chunkLoaderList.updateChunkLoaderPosition((EntityWither)(Object)this);
			}
			if (getChunkLoadSeconds() <= 0){
				if (isPowered) {
					isPowered = false;
					this.playSound("mob.wither.death", 1F, 1.75F + this.rand.nextFloat() * 0.25F);
				}
				unloadChunks();
			}
		}
		if (this.isLivingDead) return;
		List entities = this.worldObj.getEntitiesWithinAABB(EntityXPOrb.class, this.boundingBox.expand(2.0d, 3.0d, 2.0d));
		Iterator orbIterator = entities.iterator();
		EntityXPOrb closestOrb = null;
		double dClosestOrbDistSq = 257.0F;

		while(orbIterator.hasNext()) {
			EntityXPOrb xpOrb = (EntityXPOrb) orbIterator.next();
			double dDeltaX = this.posX - xpOrb.posX;
			double dDeltaY = this.posY - xpOrb.posY;
			double dDeltaZ = this.posZ - xpOrb.posZ;
			double dDistSq = dDeltaX * dDeltaX + dDeltaY * dDeltaY + dDeltaZ * dDeltaZ;
			if (dDistSq < dClosestOrbDistSq) {
				closestOrb = xpOrb;
				dClosestOrbDistSq = dDistSq;
			}
		}
		if (closestOrb != null && !closestOrb.isDead && getChunkLoadSeconds() < Integer.MAX_VALUE) {
			this.playSound("random.orb", 0.1F, 0.5F * ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.8F));
			addChunkLoadSeconds(closestOrb.xpValue * 15);
			closestOrb.setDead();
		}
	}
	public void onDeath(DamageSource source) {
		super.onDeath(source);
		if (isPowered) {
			isPowered = false;
			unloadChunks();
		}
	}

	@Unique
	private void loadChunks() {
		WitherChunkLoaderList chunkLoaderList = worldObj.getData(WitherTweaksAddon.CHUNK_LOADER_LIST);
		chunkLoaderList.addChunkLoader((EntityWither)(Object)this);
		worldObj.setData(WitherTweaksAddon.CHUNK_LOADER_LIST, chunkLoaderList);
	}
	@Unique
	private void unloadChunks() {
		WitherChunkLoaderList chunkLoaderList = worldObj.getData(WitherTweaksAddon.CHUNK_LOADER_LIST);
		chunkLoaderList.removeChunkLoadersAt(this.getUniqueID());
		worldObj.setData(WitherTweaksAddon.CHUNK_LOADER_LIST, chunkLoaderList);
	}
}
