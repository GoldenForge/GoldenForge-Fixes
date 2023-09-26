package fr.modcraftmc.goldenforgefixes.mixin.fixes.chunky;

import fr.modcraftmc.goldenforgefixes.chunky.GoldenforgeWorld;
import net.minecraft.server.level.ServerPlayer;
import org.popcraft.chunky.platform.ForgePlayer;
import org.popcraft.chunky.platform.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ForgePlayer.class, remap = false)
public class ForgePlayerMixin {

    @Shadow @Final private ServerPlayer player;

    /**
     * @author manugame_
     * @reason redirect to goldenforge world
     */
    @Overwrite
    public World getWorld() {
        return new GoldenforgeWorld(player.serverLevel());
    }
}