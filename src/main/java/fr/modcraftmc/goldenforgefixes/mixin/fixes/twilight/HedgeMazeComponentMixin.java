package fr.modcraftmc.goldenforgefixes.mixin.fixes.twilight;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import twilightforest.world.components.structures.HedgeMazeComponent;
import twilightforest.world.components.structures.TFMaze;

@Mixin(value = HedgeMazeComponent.class)
public class HedgeMazeComponentMixin {

    @Inject(method = "postProcess", at = @At(value = "FIELD", target = "Ltwilightforest/world/components/structures/TFMaze;oddBias:I", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    public void redirect(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos, CallbackInfo ci, TFMaze maze) {
        ((TFMazeAccessor) maze).setRand(RandomSource.create());
    }
}
