package fr.modcraftmc.goldenforgefixes.mixin.fixes.ic2;

import ic2.core.block.generators.ReactorChamberBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ReactorChamberBlock.class, remap = false)
public class ReactorChamberBlockMixin {

    /**
     * @author manugame_
     * @reason do not load chunk
     */
    @Inject(method = "isReactorAt", at = @At("HEAD"), cancellable = true)
    public void checkLoaded(Level world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (!world.isLoaded(pos))
            cir.setReturnValue(false);
    }
}
