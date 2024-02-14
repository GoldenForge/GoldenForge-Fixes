package fr.modcraftmc.goldenforgefixes.mixin.fixes.ultimineaddition;

import net.ixdarklord.ultimine_addition.common.data.chunk.ChunkData;
import net.ixdarklord.ultimine_addition.common.data.chunk.ChunkManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ConcurrentModificationException;
import java.util.Map;

@Mixin(value = ChunkManager.class, remap = false)
public class ChunkManagerMixin {

    @Shadow @Final private Map<ChunkAccess, ChunkData> chunks;

    /**
     * @author manugame_
     * @reason do not load chunk
     */
    @Overwrite
    public ChunkManager validateChunkData(ServerLevel level) {
        try {
            chunks.forEach((chunk, data) -> data.getPlacedBlocks().forEach((uuid, blockInfoList) ->
                    blockInfoList.removeIf(blockInfo -> chunk.getBlockState(blockInfo.pos()).getBlock() != blockInfo.blockState().getBlock())));
        } catch (ConcurrentModificationException ignored) {}
        return (ChunkManager) (Object) this;
    }
}
