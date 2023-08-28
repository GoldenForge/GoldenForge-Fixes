package fr.modcraftmc.goldenforgefixes.mixin.fixes.chunky;

import net.minecraft.commands.CommandSourceStack;
import org.popcraft.chunky.platform.ForgeSender;
import org.popcraft.chunky.platform.ForgeWorld;
import org.popcraft.chunky.platform.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ForgeSender.class, remap = false)
public class ForgeSenderMixin {

    @Shadow @Final private CommandSourceStack source;

    /**
     * @author manugame_
     * @reason redirect to goldenforge world
     */
    @Overwrite
    public World getWorld() {
        return new ForgeWorld(source.getLevel());
    }
}