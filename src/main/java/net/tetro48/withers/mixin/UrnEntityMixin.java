package net.tetro48.withers.mixin;

import btw.entity.UrnEntity;
import net.minecraft.src.*;
import net.tetro48.withers.DormantWither;
import net.tetro48.withers.WitherTweaksAddon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(UrnEntity.class)
public abstract class UrnEntityMixin {


	@Shadow
	private static boolean isWitherBodyBlock(World world, int i, int j, int k) {
		return false;
	}

	@Unique
	private static boolean isRunedSkullBlock(World world, int i, int j, int k) {
		int iBlockID = world.getBlockId(i, j, k);
		if (iBlockID == Block.skull.blockID) {
			TileEntity tileEntity = world.getBlockTileEntity(i, j, k);
			if (tileEntity instanceof TileEntitySkull) {
				return ((TileEntitySkull)tileEntity).getSkullType() == 1;
			}
		}

		return false;
	}

	@Inject(method = "attemptToCreateGolemOrWither", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", target = "Lbtw/entity/UrnEntity;isWitherHeadBlock(Lnet/minecraft/src/World;III)Z"), cancellable = true)
	private static void tryCreatingDormantWither(World world, int x, int y, int z, CallbackInfoReturnable<Boolean> cir, int j) {
		if (WitherTweaksAddon.isDormantWitherEnabled && isRunedSkullBlock(world, x, j, z)) {
			cir.setReturnValue(attemptToCreateDormantWither(world, x, j, z));
		}
	}

	@Unique
	private static boolean attemptToCreateDormantWither(World world, int i, int j, int k) {
		if (j >= 2) {
			for(int iTempKOffset = -2; iTempKOffset <= 0; ++iTempKOffset) {
				if (isWitherBodyBlock(world, i, j - 1, k + iTempKOffset) && isWitherBodyBlock(world, i, j - 1, k + iTempKOffset + 1) && isWitherBodyBlock(world, i, j - 2, k + iTempKOffset + 1) && isWitherBodyBlock(world, i, j - 1, k + iTempKOffset + 2) && isRunedSkullBlock(world, i, j, k + iTempKOffset) && isRunedSkullBlock(world, i, j, k + iTempKOffset + 1) && isRunedSkullBlock(world, i, j, k + iTempKOffset + 2)) {
					world.SetBlockMetadataWithNotify(i, j, k + iTempKOffset, 8, 2);
					world.SetBlockMetadataWithNotify(i, j, k + iTempKOffset + 1, 8, 2);
					world.SetBlockMetadataWithNotify(i, j, k + iTempKOffset + 2, 8, 2);
					world.setBlock(i, j, k + iTempKOffset, 0, 0, 2);
					world.setBlock(i, j, k + iTempKOffset + 1, 0, 0, 2);
					world.setBlock(i, j, k + iTempKOffset + 2, 0, 0, 2);
					world.setBlock(i, j - 1, k + iTempKOffset, 0, 0, 2);
					world.setBlock(i, j - 1, k + iTempKOffset + 1, 0, 0, 2);
					world.setBlock(i, j - 1, k + iTempKOffset + 2, 0, 0, 2);
					world.setBlock(i, j - 2, k + iTempKOffset + 1, 0, 0, 2);
					DormantWither.summonDormantWither(world, i, j, k + iTempKOffset + 1);
					world.notifyBlockChange(i, j, k + iTempKOffset, 0);
					world.notifyBlockChange(i, j, k + iTempKOffset + 1, 0);
					world.notifyBlockChange(i, j, k + iTempKOffset + 2, 0);
					world.notifyBlockChange(i, j - 1, k + iTempKOffset, 0);
					world.notifyBlockChange(i, j - 1, k + iTempKOffset + 1, 0);
					world.notifyBlockChange(i, j - 1, k + iTempKOffset + 2, 0);
					world.notifyBlockChange(i, j - 2, k + iTempKOffset + 1, 0);
					return true;
				}
			}

			for(int iTempIOffset = -2; iTempIOffset <= 0; ++iTempIOffset) {
				if (isWitherBodyBlock(world, i + iTempIOffset, j - 1, k) && isWitherBodyBlock(world, i + iTempIOffset + 1, j - 1, k) && isWitherBodyBlock(world, i + iTempIOffset + 1, j - 2, k) && isWitherBodyBlock(world, i + iTempIOffset + 2, j - 1, k) && isRunedSkullBlock(world, i + iTempIOffset, j, k) && isRunedSkullBlock(world, i + iTempIOffset + 1, j, k) && isRunedSkullBlock(world, i + iTempIOffset + 2, j, k)) {
					world.SetBlockMetadataWithNotify(i + iTempIOffset, j, k, 8, 2);
					world.SetBlockMetadataWithNotify(i + iTempIOffset + 1, j, k, 8, 2);
					world.SetBlockMetadataWithNotify(i + iTempIOffset + 2, j, k, 8, 2);
					world.setBlock(i + iTempIOffset, j, k, 0, 0, 2);
					world.setBlock(i + iTempIOffset + 1, j, k, 0, 0, 2);
					world.setBlock(i + iTempIOffset + 2, j, k, 0, 0, 2);
					world.setBlock(i + iTempIOffset, j - 1, k, 0, 0, 2);
					world.setBlock(i + iTempIOffset + 1, j - 1, k, 0, 0, 2);
					world.setBlock(i + iTempIOffset + 2, j - 1, k, 0, 0, 2);
					world.setBlock(i + iTempIOffset + 1, j - 2, k, 0, 0, 2);
					DormantWither.summonDormantWither(world, i + iTempIOffset + 1, j, k);
					world.notifyBlockChange(i + iTempIOffset, j, k, 0);
					world.notifyBlockChange(i + iTempIOffset + 1, j, k, 0);
					world.notifyBlockChange(i + iTempIOffset + 2, j, k, 0);
					world.notifyBlockChange(i + iTempIOffset, j - 1, k, 0);
					world.notifyBlockChange(i + iTempIOffset + 1, j - 1, k, 0);
					world.notifyBlockChange(i + iTempIOffset + 2, j - 1, k, 0);
					world.notifyBlockChange(i + iTempIOffset + 1, j - 2, k, 0);
					return true;
				}
			}
		}

		return false;
	}
}
