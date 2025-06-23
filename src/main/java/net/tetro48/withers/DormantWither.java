package net.tetro48.withers;

import net.minecraft.src.*;

public class DormantWither extends EntityWither {

	public DormantWither(World par1World) {
		super(par1World);
		this.targetTasks.removeAllTasks();
		this.experienceValue = 1;
	}

	public int getChunkLoadSeconds() {
		return this.dataWatcher.getWatchableObjectInt(21);
	}

	@Override
	protected void updateAITasks() {
		if (this.func_82212_n() > 0) {
			int var1 = this.func_82212_n() - 1;
			if (var1 <= 0) {
				this.worldObj.func_82739_e(1013, (int) this.posX, (int) this.posY, (int) this.posZ, 0);
			}

			this.func_82215_s(var1);
			if (this.ticksExisted % 10 == 0) {
				this.heal(10.0F);
			}
		}
	}

	@Override
	protected void modSpecificOnLivingUpdate() {
		if (this.recentlyOnChoppingBlockCountdown > 0) {
			--this.recentlyOnChoppingBlockCountdown;
		}
	}
	@Override
	protected String getLivingSound() {
		return null;
	}

	@Override
	public boolean isArmored() {
		if (getChunkLoadSeconds() <= 0) return super.isArmored();
		else if (getChunkLoadSeconds() < 10) return ticksExisted % 20 < 10;
		else if (getChunkLoadSeconds() < 30) return ticksExisted % 120 < 110;
		else return true;
	}

	public static void summonDormantWither(World world, int i, int j, int k) {
		DormantWither wither = new DormantWither(world);
		wither.setLocationAndAngles(i + 0.5d, j - 1.45d, k + 0.5d, 0f, 0f);
		wither.func_82206_m();
		world.spawnEntityInWorld(wither);
		world.playAuxSFX(2279, i, j, k, 0);
	}

	//This won't drop a scroll on death.
	@Override
	public void checkForScrollDrop() {}
	//This won't drop a nether star on death, to avoid cheesing progression.
	@Override
	protected void dropFewItems(boolean par1, int par2) {}
}
