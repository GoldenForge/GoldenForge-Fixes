package fr.modcraftmc.goldenforgefixes.mixin.fixes.create;

import com.simibubi.create.content.contraptions.actors.seat.SeatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(SeatBlock.class)
public class SeatBlockMixin {

    VoxelShape SEAT_COLLISION = cuboid(0.0D, 3.0D, 0.0D, 16.0D, 9.0D, 16.0D), SEAT_COLLISION_PLAYERS = cuboid(0.0D, 3.0D, 0.0D, 16.0D, 9.0D, 16.0D);

    private static VoxelShape cuboid(double p_49797_, double p_49798_, double p_49799_, double p_49800_, double p_49801_, double p_49802_) {
        return Shapes.box(p_49797_ / 16.0D, p_49798_ / 16.0D, p_49799_ / 16.0D, p_49800_ / 16.0D, p_49801_ / 16.0D, p_49802_ / 16.0D);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public VoxelShape getCollisionShape(BlockState p_220071_1_, BlockGetter p_220071_2_, BlockPos p_220071_3_,
                                        CollisionContext ctx) {
        return Shapes.empty();
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_,
                               CollisionContext p_220053_4_) {
        return SEAT_COLLISION;
    }
}
