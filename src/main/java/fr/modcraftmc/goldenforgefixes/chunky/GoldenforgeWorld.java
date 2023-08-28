package fr.modcraftmc.goldenforgefixes.chunky;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.ImposterProtoChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import org.goldenforge.bukkit.Bukkit;
import org.popcraft.chunky.platform.ForgeWorld;

import java.util.concurrent.CompletableFuture;

public class GoldenforgeWorld extends ForgeWorld {

    private final ServerLevel world;

    public GoldenforgeWorld(ServerLevel world) {
        super(world);
        this.world = world;
    }

    @Override
    public boolean isChunkGenerated(final int x, final int z) {
        // Paper start - Fix this method
        if (!Bukkit.isPrimaryThread()) {
            return java.util.concurrent.CompletableFuture.supplyAsync(() -> {
                return isChunkGenerated(x, z);
            }, world.getChunkSource().mainThreadProcessor).join();
        }
        ChunkAccess chunk = world.getChunkSource().getChunkAtImmediately(x, z);
        if (chunk != null) {
            return chunk instanceof ImposterProtoChunk || chunk instanceof net.minecraft.world.level.chunk.LevelChunk;
        }
        try {
            return world.getChunkSource().chunkMap.getChunkStatusOnDisk(new ChunkPos(x, z)) == ChunkStatus.FULL;
            // Paper end
        } catch (java.io.IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public CompletableFuture<Void> getChunkAtAsync(final int x, final int z) {
        return CompletableFuture.allOf(getChunk(x, z));
    }

    public CompletableFuture<LevelChunk> getChunk(int x, int z) {
        if (Bukkit.isPrimaryThread()) {
            net.minecraft.world.level.chunk.LevelChunk immediate = this.world.getChunkSource().getChunkAtIfLoadedImmediately(x, z);
            if (immediate != null) {
                return java.util.concurrent.CompletableFuture.completedFuture(immediate);
            }
        }

        ca.spottedleaf.concurrentutil.executor.standard.PrioritisedExecutor.Priority priority = ca.spottedleaf.concurrentutil.executor.standard.PrioritisedExecutor.Priority.NORMAL;
        java.util.concurrent.CompletableFuture<LevelChunk> ret = new java.util.concurrent.CompletableFuture<>();

        io.papermc.paper.chunk.system.ChunkSystem.scheduleChunkLoad(this.world, x, z, true, ChunkStatus.FULL, true, priority, (c) -> {
            net.minecraft.server.MinecraftServer.getServer().scheduleOnMain(() -> {
                net.minecraft.world.level.chunk.LevelChunk chunk = (net.minecraft.world.level.chunk.LevelChunk)c;
                //if (chunk != null) addTicket(x, z); // Paper
                ret.complete(chunk == null ? null : chunk);
            });
        });
        return ret;
    }
}