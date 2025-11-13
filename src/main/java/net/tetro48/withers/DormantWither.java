package net.tetro48.withers;

import net.minecraft.src.*;

public class DormantWither extends EntityWither {

	public static final int MAX_CHUNK_LOADER_TIME = 28800;

	public DormantWither(World par1World) {
		super(par1World);
		this.targetTasks.removeAllTasks();
		this.experienceValue = 1;
	}

	public int getChunkLoadSeconds() {
		return this.dataWatcher.getWatchableObjectInt(21);
	}

	public void addChunkLoadSeconds(int seconds) {
		this.dataWatcher.updateObject(21, getChunkLoadSeconds() + seconds);
	}
	@Override
	public void entityInit() {
		super.entityInit();
		this.dataWatcher.updateObject(20, 20);
		this.dataWatcher.updateObject(21, 600);
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
		int invulnTicks = func_82212_n();
		//invuln bypass logic
		func_82215_s(0);
		boolean canHit = super.attackEntityFrom(par1DamageSource, par2);
		func_82215_s(invulnTicks);

		if (canHit) {
			float fraction = par2 / this.getMaxHealth();
			this.addChunkLoadSeconds((int) (-MAX_CHUNK_LOADER_TIME * fraction));
		}
		return canHit;
	}

	@Override
	protected void updateAITasks() {}

	@Override
	protected void modSpecificOnLivingUpdate() {
		if (this.recentlyOnChoppingBlockCountdown > 0) {
			--this.recentlyOnChoppingBlockCountdown;
		}
		this.setHealth(this.getMaxHealth() * (getChunkLoadSeconds() / (float) MAX_CHUNK_LOADER_TIME));
		if (getChunkLoadSeconds() > MAX_CHUNK_LOADER_TIME && this.isEntityAlive()) {
			this.attackEntityFrom(new EntityDamageSource("overflow_explosion", null), 9999);
			this.worldObj.newExplosion(this, this.posX, this.posY + (double)this.getEyeHeight(), this.posZ, 7.0F, false, this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing") && WitherTweaksAddon.canDormantWitherGrief);
		}
	}

	@Override
	public String getEntityName() {
		if (getChunkLoadSeconds() > MAX_CHUNK_LOADER_TIME * 0.9) {
			return String.format("§4<!> §r%s §4<!>", super.getEntityName());
		}
		return super.getEntityName();
	}

	@Override
	protected String getLivingSound() {
		return null;
	}

	@Override
	public boolean isArmored() {
		if (getChunkLoadSeconds() <= 0) return false;
		else if (getChunkLoadSeconds() > (MAX_CHUNK_LOADER_TIME * 0.9)) return ticksExisted % 2 < 1;
		else return false;
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
