package fr.modcraftmc.goldenforgefixes.mixin.fixes.twilight;

import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import twilightforest.world.TFTeleporter;

@Mixin(TFTeleporter.class)
public class TFTeleporterMixin {

    @Redirect(method = "placeInExistingPortal", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ChunkMap;isExistingChunkFull(Lnet/minecraft/world/level/ChunkPos;)Z"))
    private static boolean redirect(ChunkMap instance, ChunkPos p_140426_) {
        return instance.level.hasChunk(p_140426_.x, p_140426_.z);
    }
}
