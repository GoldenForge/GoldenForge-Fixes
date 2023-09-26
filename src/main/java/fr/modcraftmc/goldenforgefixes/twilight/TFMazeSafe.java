package fr.modcraftmc.goldenforgefixes.twilight;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import org.spongepowered.asm.mixin.Mixin;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFConfiguredFeatures;
import twilightforest.world.components.structures.TFMaze;
import twilightforest.world.components.structures.TFStructureComponentOld;

/**
 * This is a maze of cells and walls.
 * <p>
 * The cells are at odd numbered x and y values, and the walls are at even numbered ones.  This does make the storage slightly inefficient, but oh wells.
 *
 * @author Ben
 */
public class TFMazeSafe extends TFMaze {

    public RandomSource safeRandom;

    public TFMazeSafe(int cellsWidth, int cellsDepth, RandomSource randomSource) {
        super(cellsWidth, cellsDepth);
        this.safeRandom = randomSource;
    }

    public void setSeed(long newSeed) {
        safeRandom.setSeed(newSeed);
    }
}
