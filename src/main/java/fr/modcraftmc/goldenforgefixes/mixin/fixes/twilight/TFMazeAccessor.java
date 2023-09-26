package fr.modcraftmc.goldenforgefixes.mixin.fixes.twilight;

import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import twilightforest.world.components.structures.TFMaze;

@Mixin(TFMaze.class)
public interface TFMazeAccessor {

    @Accessor("rand")
    public void setRand(RandomSource rand);
}
