package mingli29.jebb.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class JebbVerticalSlabBlock extends Block implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty DOUBLED = BooleanProperty.create("doubled");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape NORTH_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 8.0);
    private static final VoxelShape SOUTH_SHAPE = Block.box(0.0, 0.0, 8.0, 16.0, 16.0, 16.0);
    private static final VoxelShape WEST_SHAPE = Block.box(0.0, 0.0, 0.0, 8.0, 16.0, 16.0);
    private static final VoxelShape EAST_SHAPE = Block.box(8.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    private final Block parent;

    public JebbVerticalSlabBlock(Block parent, BlockBehaviour.Properties properties) {
        super(properties);
        this.parent = parent;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(DOUBLED, false)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, DOUBLED, WATERLOGGED);
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return !state.getValue(DOUBLED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        if (state.getValue(DOUBLED)) return Shapes.block();
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            case EAST -> EAST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos pos = ctx.getClickedPos();
        BlockState here = ctx.getLevel().getBlockState(pos);
        if (here.is(this) && !here.getValue(DOUBLED)) {
            return here.setValue(DOUBLED, true).setValue(WATERLOGGED, false);
        }
        FluidState fluid = ctx.getLevel().getFluidState(pos);
        Direction facing = pickFacing(ctx);
        return this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(DOUBLED, false)
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
    }

    private static Direction pickFacing(BlockPlaceContext ctx) {
        Direction face = ctx.getClickedFace();
        if (face.getAxis().isHorizontal()) {
            return face.getOpposite();
        }
        Vec3 hit = ctx.getClickLocation();
        BlockPos pos = ctx.getClickedPos();
        double dx = hit.x - pos.getX() - 0.5;
        double dz = hit.z - pos.getZ() - 0.5;
        if (Math.abs(dx) > Math.abs(dz)) {
            return dx > 0 ? Direction.EAST : Direction.WEST;
        }
        return dz > 0 ? Direction.SOUTH : Direction.NORTH;
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext ctx) {
        ItemStack stack = ctx.getItemInHand();
        if (state.getValue(DOUBLED) || !stack.is(this.asItem())) return false;
        if (!ctx.replacingClickedOnBlock()) return true;
        Direction facing = state.getValue(FACING);
        Direction clickedFace = ctx.getClickedFace();
        if (clickedFace == facing.getOpposite()) return true;
        if (clickedFace == facing) return false;
        Vec3 hit = ctx.getClickLocation();
        BlockPos pos = ctx.getClickedPos();
        double off = (facing.getAxis() == Direction.Axis.X)
                ? hit.x - pos.getX() - 0.5 + clickedFace.getStepX() * 0.5
                : hit.z - pos.getZ() - 0.5 + clickedFace.getStepZ() * 0.5;
        return facing.getAxisDirection() == Direction.AxisDirection.POSITIVE
                ? off < 0
                : off > 0;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluid) {
        return !state.getValue(DOUBLED) && SimpleWaterloggedBlock.super.placeLiquid(level, pos, state, fluid);
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
        return !state.getValue(DOUBLED) && SimpleWaterloggedBlock.super.canPlaceLiquid(level, pos, state, fluid);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbor,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, direction, neighbor, level, pos, neighborPos);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return switch (type) {
            case WATER -> level.getFluidState(pos).is(FluidTags.WATER);
            default -> false;
        };
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
