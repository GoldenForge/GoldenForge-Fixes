package fr.modcraftmc.goldenforgefixes.mixin.fixes.chunky;

import fr.modcraftmc.goldenforgefixes.chunky.GoldenforgeWorld;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import org.popcraft.chunky.platform.ForgeServer;
import org.popcraft.chunky.platform.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(value = ForgeServer.class, remap = false)
public class ForgeServerMixin {

    @Shadow @Final private MinecraftServer server;

    /**
     * @author manugame
     * @reason redirect to goldenforge world
     */
    @Overwrite
    public Optional<World> getWorld(final String name) {
        return Optional.ofNullable(ResourceLocation.tryParse(name))
                .map(resourceLocation -> server.getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, resourceLocation)))
                .map(GoldenforgeWorld::new);
    }

    /**
     * @author manugame
     * @reason redirect to goldenforge world
     */
    @Overwrite
    public List<World> getWorlds() {
        final List<World> worlds = new ArrayList<>();
        server.getAllLevels().forEach(world -> worlds.add(new GoldenforgeWorld(world)));
        return worlds;
    }

}