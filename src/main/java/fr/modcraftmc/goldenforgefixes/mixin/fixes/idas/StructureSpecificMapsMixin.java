package fr.modcraftmc.goldenforgefixes.mixin.fixes.idas;

import com.craisinlord.integrated_api.misc.maptrades.StructureSpecificMaps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = StructureSpecificMaps.TreasureMapForEmeralds.class, remap = false)
public class StructureSpecificMapsMixin {


    @Inject(method = "getOffer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/item/trading/MerchantOffer;",
    at = @At("HEAD")
    )
    public void cancel(ServerLevel level, Entity entity, CallbackInfoReturnable<MerchantOffer> cir) {
        cir.setReturnValue(null);
    }
}
