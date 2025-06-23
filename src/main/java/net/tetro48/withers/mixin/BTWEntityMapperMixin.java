package net.tetro48.withers.mixin;

import btw.entity.util.BTWEntityMapper;
import net.minecraft.src.EntityList;
import net.tetro48.withers.DormantWither;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BTWEntityMapper.class)
public abstract class BTWEntityMapperMixin {
	@Inject(method = "createModEntityMappings", at = @At("TAIL"), remap = false)
	private static void addNewMappings(CallbackInfo ci) {
		EntityList.addMapping(DormantWither.class, "DormantWither", 3900, 3551805, 6113391);
	}
}
