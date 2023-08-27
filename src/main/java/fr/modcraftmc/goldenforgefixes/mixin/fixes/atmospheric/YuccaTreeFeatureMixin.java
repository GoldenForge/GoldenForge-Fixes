package fr.modcraftmc.goldenforgefixes.mixin.fixes.atmospheric;

import com.google.common.collect.Sets;
import com.teamabnormals.atmospheric.common.levelgen.feature.YuccaTreeFeature;
import com.teamabnormals.atmospheric.common.levelgen.feature.configurations.YuccaTreeConfiguration;
import com.teamabnormals.atmospheric.core.other.tags.AtmosphericBlockTags;
import com.teamabnormals.blueprint.core.util.TreeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Set;
import java.util.function.BiConsumer;

@Mixin(YuccaTreeFeature.class)
public abstract class YuccaTreeFeatureMixin {

    /**
     * @author
     * @reason make this thread-safe
     */
    @Overwrite
    public boolean place(FeaturePlaceContext<YuccaTreeConfiguration> context) {
        YuccaTreeConfiguration config = context.config();
        WorldGenLevel worldIn = context.level();
        RandomSource rand = context.random();
        BlockPos position = context.origin();

        Set<BlockPos> logPosSet = Sets.newHashSet();
        if (config.baby) {
            int height = 2 + rand.nextInt(2) + rand.nextInt(2);
            boolean flag = true;

            if (position.getY() > worldIn.getMinBuildHeight() && position.getY() + height + 1 <= worldIn.getMaxBuildHeight()) {
                for (int j = position.getY(); j <= position.getY() + 1 + height; ++j) {
                    int k = 1;
                    if (j == position.getY())
                        k = 0;
                    if (j >= position.getY() + 1 + height - 2)
                        k = 2;
                    BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
                    for (int l = position.getX() - k; l <= position.getX() + k && flag; ++l) {
                        for (int i1 = position.getZ() - k; i1 <= position.getZ() + k && flag; ++i1) {
                            if (j >= worldIn.getMinBuildHeight() && j < worldIn.getMaxBuildHeight()) {
                                if (!TreeUtil.isAirOrLeaves(worldIn, blockpos$mutableblockpos.set(l, j, i1)))
                                    flag = false;
                            } else
                                flag = false;
                        }
                    }
                }

                if (!flag) {
                    return false;
                } else if (TreeUtil.isInTag(worldIn, position.below(), AtmosphericBlockTags.YUCCA_PLACEABLE) && position.getY() < worldIn.getMaxBuildHeight()) {
                    if (!TreeUtil.isInTag(worldIn, position.below(), BlockTags.SAND))
                        TreeUtil.setDirtAt(worldIn, position.below());

                    int logX = position.getX();
                    int logZ = position.getZ();
                    int logY = position.getY();

                    for (int k1 = 0; k1 < height; ++k1) {
                        logY = position.getY() + k1;
                        BlockPos blockpos = new BlockPos(logX, logY, logZ);
                        if (TreeUtil.isAirOrLeaves(worldIn, blockpos)) {
                            this.placeLogAt(worldIn, blockpos, Direction.UP, false, rand, config, logPosSet);
                        }
                    }

                    logY = position.getY() + height - 1;

                    position = new BlockPos(logX, logY, logZ);

                    this.createBabyYuccaLeaves(worldIn, position.above(), rand, config, false);
                    this.createBabyYuccaLeaves(worldIn, position, rand, config, true);
                    this.createBabyYuccaLeaves(worldIn, position.below(), rand, config, false);

                    if (config.patch) {
                        for (int j = 0; j < 64; ++j) {
                            BlockPos blockpos = position.offset(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
                            if (worldIn.isStateAtPosition(blockpos, BlockBehaviour.BlockStateBase::isAir) && blockpos.getY() < worldIn.getMaxBuildHeight() && config.flowerProvider.getState(rand, blockpos).canSurvive(worldIn, blockpos)) {
                                placeFlowerAt(worldIn, blockpos, rand, config);
                            }
                        }
                    }

                    TreeUtil.updateLeaves(worldIn, logPosSet);
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            int height = 4 + rand.nextInt(2) + rand.nextInt(2);
            int reduction = 2 + rand.nextInt(3);
            if (config.petrified)
                height -= reduction;
            boolean flag = true;

            if (position.getY() >= worldIn.getMinBuildHeight() && position.getY() + height + 1 <= worldIn.getMaxBuildHeight()) {
                for (int j = position.getY(); j <= position.getY() + 1 + height; ++j) {
                    int k = 1;
                    if (j == position.getY())
                        k = 0;
                    if (j >= position.getY() + 1 + height - 2)
                        k = 2;
                    BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
                    for (int l = position.getX() - k; l <= position.getX() + k && flag; ++l) {
                        for (int i1 = position.getZ() - k; i1 <= position.getZ() + k && flag; ++i1) {
                            if (j >= 0 && j < worldIn.getMaxBuildHeight()) {
                                if (!TreeUtil.isAirOrLeaves(worldIn, blockpos$mutableblockpos.set(l, j, i1)))
                                    flag = false;
                            } else
                                flag = false;
                        }
                    }
                }

                if (!flag) {
                    return false;
                } else if (TreeUtil.isInTag(worldIn, position.below(), AtmosphericBlockTags.YUCCA_PLACEABLE) && position.getY() < worldIn.getMaxBuildHeight()) {
                    // base log
                    if (!TreeUtil.isInTag(worldIn, position.below(), BlockTags.SAND))
                        TreeUtil.setDirtAt(worldIn, position.below());
                    for (int ja = 0; ja < reduction; ++ja) {
                        if (config.petrified) {
                            this.placeLogAt(worldIn, position.below(ja), Direction.UP, false, rand, config, logPosSet);
                        }
                    }

                    int logX = position.getX();
                    int logZ = position.getZ();
                    int logY = position.getY();

                    for (int k1 = 0; k1 < height; ++k1) {
                        logY = position.getY() + k1;
                        BlockPos blockpos = new BlockPos(logX, logY, logZ);
                        if (TreeUtil.isAirOrLeaves(worldIn, blockpos)) {
                            this.placeLogAt(worldIn, blockpos, Direction.UP, false, rand, config, logPosSet);
                        }
                    }

                    logY = position.getY() + height - 1;

                    BlockPos newPos = this.createYuccaBranch(height, worldIn, position, Direction.NORTH, rand, config, logPosSet);
                    this.createYuccaLeaves(worldIn, newPos.above(), rand, false, config);
                    this.createYuccaLeaves(worldIn, newPos, rand, true, config);
                    this.createYuccaLeaves(worldIn, newPos.below(), rand, false, config);

                    newPos = this.createYuccaBranch(height, worldIn, position, Direction.EAST, rand, config, logPosSet);
                    this.createYuccaLeaves(worldIn, newPos.above(), rand, false, config);
                    this.createYuccaLeaves(worldIn, newPos, rand, true, config);
                    this.createYuccaLeaves(worldIn, newPos.below(), rand, false, config);

                    newPos = this.createYuccaBranch(height, worldIn, position, Direction.SOUTH, rand, config, logPosSet);
                    this.createYuccaLeaves(worldIn, newPos.above(), rand, false, config);
                    this.createYuccaLeaves(worldIn, newPos, rand, true, config);
                    this.createYuccaLeaves(worldIn, newPos.below(), rand, false, config);

                    newPos = this.createYuccaBranch(height, worldIn, position, Direction.WEST, rand, config, logPosSet);
                    this.createYuccaLeaves(worldIn, newPos.above(), rand, false, config);
                    this.createYuccaLeaves(worldIn, newPos, rand, true, config);
                    this.createYuccaLeaves(worldIn, newPos.below(), rand, false, config);

                    if (config.patch) {
                        for (int j = 0; j < 64; ++j) {
                            BlockPos blockpos = position.offset(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
                            if (worldIn.isStateAtPosition(blockpos, BlockBehaviour.BlockStateBase::isAir) && blockpos.getY() < worldIn.getMaxBuildHeight() && config.flowerProvider.getState(rand, blockpos).canSurvive(worldIn, blockpos)) {
                                placeFlowerAt(worldIn, blockpos, rand, config);
                            }
                        }
                    }

                    if (config.petrified && rand.nextInt(12) == 0) {
                        for (int j = 0; j < 12; ++j) {
                            BlockPos blockpos = position.offset(rand.nextInt(6) - rand.nextInt(6), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(6) - rand.nextInt(6));
                            if (worldIn.isStateAtPosition(blockpos, BlockBehaviour.BlockStateBase::isAir) && blockpos.getY() < worldIn.getMaxBuildHeight() && TreeUtil.isInTag(worldIn, blockpos.below(), BlockTags.SAND)) {
                                TreeUtil.setForcedState(worldIn, blockpos, config.bundleProvider.getState(rand, blockpos));
                            }
                        }
                    }

                    TreeUtil.updateLeaves(worldIn, logPosSet);

                    Set<BlockPos> set3 = Sets.newHashSet();
                    BiConsumer<BlockPos, BlockState> biconsumer3 = (p_225290_, p_225291_) -> {
                        set3.add(p_225290_.immutable());
                        worldIn.setBlock(p_225290_, p_225291_, 19);
                    };

                    if (!config.decorators.isEmpty()) {
                        TreeDecorator.Context decoratorContext = new TreeDecorator.Context(worldIn, biconsumer3, rand, logPosSet, Sets.newHashSet(), Sets.newHashSet());
                        config.decorators.forEach((decorator) -> decorator.place(decoratorContext));
                    }

                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    private void createYuccaLeaves(LevelSimulatedRW worldIn, BlockPos newPos, RandomSource rand, boolean square, YuccaTreeConfiguration config) {
        int leafSize = 1;
        for (int k3 = -leafSize; k3 <= leafSize; ++k3) {
            for (int j4 = -leafSize; j4 <= leafSize; ++j4) {
                if (square) {
                    this.placeLeafAt(worldIn, newPos.offset(k3, 0, j4), rand, config);
                } else {
                    if ((Math.abs(k3) != leafSize || Math.abs(j4) != leafSize)) {
                        this.placeLeafAt(worldIn, newPos.offset(k3, 0, j4), rand, config);
                    } else if (rand.nextInt(4) == 0) {
                        this.placeLeafAt(worldIn, newPos.offset(k3, 0, j4), rand, config);
                    }
                }
            }
        }
    }

    private BlockPos createYuccaBranch(int height, LevelSimulatedRW worldIn, BlockPos pos, Direction direction, RandomSource rand, YuccaTreeConfiguration config, Set<BlockPos> logPosSet) {
        int logX = pos.getX();
        int logZ = pos.getZ();
        int logY = pos.getY() + height - 1;
        int length = 4 + rand.nextInt(2);
        BlockPos blockpos = new BlockPos(logX, logY, logZ);
        boolean bundle = false;
        boolean anyBundle = false;

        for (int i = 0; i < length; i++) {
            blockpos = new BlockPos(logX, logY, logZ);
            if (!anyBundle && rand.nextInt(16) == 0) {
                bundle = true;
                anyBundle = true;
            } else {
                bundle = false;
            }
            this.createHorizontalLog(1, worldIn, blockpos, direction, bundle, rand, config, logPosSet);
            if (i != length) {
                if (direction == Direction.EAST || direction == Direction.WEST) {
                    if (direction == Direction.EAST) {
                        logX += rand.nextInt(2);
                    } else {
                        logX -= rand.nextInt(2);
                    }
                } else {
                    if (direction == Direction.SOUTH) {
                        logZ += rand.nextInt(2);
                    } else {
                        logZ -= rand.nextInt(2);
                    }
                }
                logY += 1;
            }
        }
        return blockpos.relative(direction);
    }

    private void createHorizontalLog(int branchLength, LevelSimulatedRW worldIn, BlockPos pos, Direction direction, boolean bundle, RandomSource rand, YuccaTreeConfiguration config, Set<BlockPos> logPosSet) {
        int logX = pos.getX();
        int logY = pos.getY();
        int logZ = pos.getZ();

        for (int k3 = 0; k3 < branchLength; ++k3) {
            logX += direction.getStepX();
            logZ += direction.getStepZ();
            BlockPos blockpos1 = new BlockPos(logX, logY, logZ);
            if (TreeUtil.isAirOrLeaves(worldIn, blockpos1)) {
                if (!worldIn.isStateAtPosition(blockpos1.below(), BlockBehaviour.BlockStateBase::isAir))
                    bundle = false;
                this.placeLogAt(worldIn, blockpos1, Direction.UP, bundle, rand, config, logPosSet);
            }
        }
    }

    private void placeLogAt(LevelWriter worldIn, BlockPos pos, Direction direction, boolean bundle, RandomSource rand, YuccaTreeConfiguration config, Set<BlockPos> logPosSet) {
        BlockState logState = config.petrified ? config.trunkProvider.getState(rand, pos) : config.trunkProvider.getState(rand, pos).setValue(RotatedPillarBlock.AXIS, direction.getAxis());
        TreeUtil.setForcedState(worldIn, pos, logState);
        logPosSet.add(pos.immutable());
        if (bundle && !config.petrified) {
            TreeUtil.setForcedState(worldIn, pos.below(), config.branchProvider.getState(rand, pos.below()));
        }
    }

    private void placeLeafAt(LevelSimulatedRW world, BlockPos pos, RandomSource rand, YuccaTreeConfiguration config) {
        if (TreeUtil.isAirOrLeaves(world, pos) && !config.petrified) {
            TreeUtil.setForcedState(world, pos, config.leavesProvider.getState(rand, pos));
        }
        if (rand.nextInt(8) == 0 && !config.petrified) {
            placeFlowerAt(world, pos.above(), rand, config);
        }
    }

    private void createBabyYuccaLeaves(LevelSimulatedRW worldIn, BlockPos newPos, RandomSource rand, YuccaTreeConfiguration config, boolean square) {
        int leafSize = 1;
        for (int k3 = -leafSize; k3 <= leafSize; ++k3) {
            for (int j4 = -leafSize; j4 <= leafSize; ++j4) {
                if (square) {
                    this.placeLeafAt(worldIn, newPos.offset(k3, 0, j4), rand, config);
                } else {
                    if ((Math.abs(k3) != leafSize || Math.abs(j4) != leafSize)) {
                        this.placeLeafAt(worldIn, newPos.offset(k3, 0, j4), rand, config);
                    } else if (rand.nextInt(4) == 0) {
                        this.placeLeafAt(worldIn, newPos.offset(k3, 0, j4), rand, config);
                    }
                }
            }
        }
    }

    private void placeFlowerAt(LevelSimulatedRW world, BlockPos pos, RandomSource rand, YuccaTreeConfiguration config) {
        if (world.isStateAtPosition(pos, BlockBehaviour.BlockStateBase::isAir)) {
            if (!world.isStateAtPosition(pos.above(), BlockBehaviour.BlockStateBase::isAir)) {
                TreeUtil.setForcedState(world, pos, config.flowerProvider.getState(rand, pos));
            } else if (rand.nextInt(4) == 0) {
                TreeUtil.setForcedState(world, pos, config.tallFlowerBottomProvider.getState(rand, pos));
                TreeUtil.setForcedState(world, pos.above(), config.tallFlowerTopProvider.getState(rand, pos.above()));
            } else {
                TreeUtil.setForcedState(world, pos, config.flowerProvider.getState(rand, pos));
            }
        }
    }
}
