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
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
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

public class JebbQuarterBlock extends Block implements SimpleWaterloggedBlock {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    public static final BooleanProperty BOTTOM_NEAR = BooleanProperty.create("bottom_near");
    public static final BooleanProperty BOTTOM_FAR = BooleanProperty.create("bottom_far");
    public static final BooleanProperty TOP_NEAR = BooleanProperty.create("top_near");
    public static final BooleanProperty TOP_FAR = BooleanProperty.create("top_far");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape Z_BN = Block.box(0.0, 0.0, 0.0, 8.0, 8.0, 16.0);
    private static final VoxelShape Z_BF = Block.box(8.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    private static final VoxelShape Z_TN = Block.box(0.0, 8.0, 0.0, 8.0, 16.0, 16.0);
    private static final VoxelShape Z_TF = Block.box(8.0, 8.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape X_BN = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 8.0);
    private static final VoxelShape X_BF = Block.box(0.0, 0.0, 8.0, 16.0, 8.0, 16.0);
    private static final VoxelShape X_TN = Block.box(0.0, 8.0, 0.0, 16.0, 16.0, 8.0);
    private static final VoxelShape X_TF = Block.box(0.0, 8.0, 8.0, 16.0, 16.0, 16.0);

    private final Block parent;

    public JebbQuarterBlock(Block parent, BlockBehaviour.Properties properties) {
        super(properties);
        this.parent = parent;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(AXIS, Direction.Axis.Z)
                .setValue(BOTTOM_NEAR, true)
                .setValue(BOTTOM_FAR, false)
                .setValue(TOP_NEAR, false)
                .setValue(TOP_FAR, false)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS, BOTTOM_NEAR, BOTTOM_FAR, TOP_NEAR, TOP_FAR, WATERLOGGED);
    }

    private static boolean isFull(BlockState state) {
        return state.getValue(BOTTOM_NEAR) && state.getValue(BOTTOM_FAR)
                && state.getValue(TOP_NEAR) && state.getValue(TOP_FAR);
    }

    private static int filledCount(BlockState state) {
        int n = 0;
        if (state.getValue(BOTTOM_NEAR)) n++;
        if (state.getValue(BOTTOM_FAR)) n++;
        if (state.getValue(TOP_NEAR)) n++;
        if (state.getValue(TOP_FAR)) n++;
        return n;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return !isFull(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        if (isFull(state)) return Shapes.block();
        boolean z = state.getValue(AXIS) == Direction.Axis.Z;
        VoxelShape shape = Shapes.empty();
        if (state.getValue(BOTTOM_NEAR)) shape = Shapes.or(shape, z ? Z_BN : X_BN);
        if (state.getValue(BOTTOM_FAR)) shape = Shapes.or(shape, z ? Z_BF : X_BF);
        if (state.getValue(TOP_NEAR)) shape = Shapes.or(shape, z ? Z_TN : X_TN);
        if (state.getValue(TOP_FAR)) shape = Shapes.or(shape, z ? Z_TF : X_TF);
        return shape;
    }

    private static BooleanProperty quadrantFor(Direction.Axis axis, double offX, double offY, double offZ) {
        boolean top = offY >= 0.5;
        boolean far = (axis == Direction.Axis.Z) ? (offX >= 0.5) : (offZ >= 0.5);
        if (top && far) return TOP_FAR;
        if (top) return TOP_NEAR;
        if (far) return BOTTOM_FAR;
        return BOTTOM_NEAR;
    }

    private static Direction.Axis pickAxis(BlockPlaceContext ctx) {
        Direction face = ctx.getClickedFace();
        Direction.Axis perp = face.getAxis().isHorizontal()
                ? face.getAxis()
                : ctx.getHorizontalDirection().getAxis();
        return perp == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
    }

    private static double placementOffsetX(Direction face, double hitX, BlockPos pos) {
        return switch (face) {
            case EAST -> 0.0;
            case WEST -> 1.0;
            default -> hitX - pos.getX();
        };
    }

    private static double placementOffsetZ(Direction face, double hitZ, BlockPos pos) {
        return switch (face) {
            case SOUTH -> 0.0;
            case NORTH -> 1.0;
            default -> hitZ - pos.getZ();
        };
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos pos = ctx.getClickedPos();
        BlockState here = ctx.getLevel().getBlockState(pos);
        Vec3 hit = ctx.getClickLocation();
        Direction face = ctx.getClickedFace();
        double offX = hit.x - pos.getX() + face.getStepX() * 0.5;
        double offY = hit.y - pos.getY() + face.getStepY() * 0.5;
        double offZ = hit.z - pos.getZ() + face.getStepZ() * 0.5;

        if (here.is(this)) {
            Direction.Axis axis = here.getValue(AXIS);
            BooleanProperty quad = quadrantFor(axis, offX, offY, offZ);
            return here.setValue(quad, true);
        }

        FluidState fluid = ctx.getLevel().getFluidState(pos);
        Direction.Axis axis = pickAxis(ctx);
        double placeX = placementOffsetX(face, hit.x, pos);
        double placeZ = placementOffsetZ(face, hit.z, pos);
        BooleanProperty quad = switch (face) {
            case UP -> quadrantFor(axis, placeX, 0.25, placeZ);
            case DOWN -> quadrantFor(axis, placeX, 0.75, placeZ);
            default -> quadrantFor(axis, placeX, offY, placeZ);
        };
        return this.defaultBlockState()
                .setValue(AXIS, axis)
                .setValue(BOTTOM_NEAR, false)
                .setValue(BOTTOM_FAR, false)
                .setValue(TOP_NEAR, false)
                .setValue(TOP_FAR, false)
                .setValue(quad, true)
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext ctx) {
        ItemStack stack = ctx.getItemInHand();
        if (!stack.is(this.asItem())) return false;
        if (isFull(state)) return false;
        if (!ctx.replacingClickedOnBlock()) return true;
        Vec3 hit = ctx.getClickLocation();
        BlockPos pos = ctx.getClickedPos();
        Direction face = ctx.getClickedFace();
        double offX = hit.x - pos.getX() + face.getStepX() * 0.5;
        double offY = hit.y - pos.getY() + face.getStepY() * 0.5;
        double offZ = hit.z - pos.getZ() + face.getStepZ() * 0.5;
        BooleanProperty quad = quadrantFor(state.getValue(AXIS), offX, offY, offZ);
        return !state.getValue(quad);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluid) {
        return !isFull(state) && SimpleWaterloggedBlock.super.placeLiquid(level, pos, state, fluid);
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
        return !isFull(state) && SimpleWaterloggedBlock.super.canPlaceLiquid(level, pos, state, fluid);
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

    public static int countFilledQuadrants(BlockState state) {
        return filledCount(state);
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
