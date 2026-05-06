package mingli29.jebb.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class JebbParentSlabBlock extends SlabBlock {
    private final Block parent;

    public JebbParentSlabBlock(Block parent, BlockBehaviour.Properties properties) {
        super(properties);
        this.parent = parent;
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        parent.fallOn(level, parent.defaultBlockState(), pos, entity, fallDistance);
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter level, Entity entity) {
        parent.updateEntityAfterFallOn(level, entity);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        parent.stepOn(level, pos, parent.defaultBlockState(), entity);
    }

    // 1.21.x made these parent callbacks protected; keep only public behavior delegation.
}

