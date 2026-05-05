package mingli29.jebb.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

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

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        parent.entityInside(parent.defaultBlockState(), level, pos, entity);
    }

    @Override
    public void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        parent.onProjectileHit(level, parent.defaultBlockState(), hit, projectile);
    }
}

