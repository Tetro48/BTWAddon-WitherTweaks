package net.tetro48.withers;

import net.minecraft.src.NBTTagCompound;

import java.util.UUID;

public class WitherChunkLoaderData {
	public UUID entityUUID;
	public int chunkX, chunkZ;
	public int dimension;

	public WitherChunkLoaderData(UUID _entityUUID, int _chunkX, int _chunkZ, int _dimension) {
		this.entityUUID = _entityUUID;
		this.chunkX = _chunkX;
		this.chunkZ = _chunkZ;
		this.dimension = _dimension;
	}

	public WitherChunkLoaderData(NBTTagCompound tagCompound) {
		this.loadFromNBT(tagCompound);
	}

	public void loadFromNBT(NBTTagCompound tagCompound) {
		this.entityUUID = new UUID(tagCompound.getLong("UUIDMost"), tagCompound.getLong("UUIDLeast"));
		this.chunkX = tagCompound.getInteger("chunkX");
		this.chunkZ = tagCompound.getInteger("chunkZ");
		this.dimension = tagCompound.getInteger("dimension");
	}

	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		tagCompound.setLong("UUIDMost", this.entityUUID.getMostSignificantBits());
		tagCompound.setLong("UUIDLeast", this.entityUUID.getLeastSignificantBits());
		tagCompound.setInteger("chunkX", this.chunkX);
		tagCompound.setInteger("chunkZ", this.chunkZ);
		tagCompound.setInteger("dimension", this.dimension);
		return tagCompound;
	}
}
