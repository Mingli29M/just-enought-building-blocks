package mingli29.jebb.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class JebbCornerPillarBlock extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty NW = BooleanProperty.create("nw");
    public static final BooleanProperty NE = BooleanProperty.create("ne");
    public static final BooleanProperty SW = BooleanProperty.create("sw");
    public static final BooleanProperty SE = BooleanProperty.create("se");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape SHAPE_NW = Block.box(0.0, 0.0, 0.0, 8.0, 16.0, 8.0);
    private static final VoxelShape SHAPE_NE = Block.box(8.0, 0.0, 0.0, 16.0, 16.0, 8.0);
    private static final VoxelShape SHAPE_SW = Block.box(0.0, 0.0, 8.0, 8.0, 16.0, 16.0);
    private static final VoxelShape SHAPE_SE = Block.box(8.0, 0.0, 8.0, 16.0, 16.0, 16.0);

    private final Block parent;

    public JebbCornerPillarBlock(Block parent, BlockBehaviour.Properties properties) {
        super(properties);
        this.parent = parent;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NW, true)
                .setValue(NE, false)
                .setValue(SW, false)
                .setValue(SE, false)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NW, NE, SW, SE, WATERLOGGED);
    }

    private static boolean isFull(BlockState state) {
        return state.getValue(NW) && state.getValue(NE)
                && state.getValue(SW) && state.getValue(SE);
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return !isFull(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        if (isFull(state)) return Shapes.block();
        VoxelShape shape = Shapes.empty();
        if (state.getValue(NW)) shape = Shapes.or(shape, SHAPE_NW);
        if (state.getValue(NE)) shape = Shapes.or(shape, SHAPE_NE);
        if (state.getValue(SW)) shape = Shapes.or(shape, SHAPE_SW);
        if (state.getValue(SE)) shape = Shapes.or(shape, SHAPE_SE);
        return shape;
    }

    private static BooleanProperty cornerFor(double offX, double offZ) {
        boolean east = offX >= 0.5;
        boolean south = offZ >= 0.5;
        if (south && east) return SE;
        if (south) return SW;
        if (east) return NE;
        return NW;
    }

    private static double localOffset(double hit, int origin, int step) {
        return hit - origin + step * 0.5;
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
        double offX = localOffset(hit.x, pos.getX(), face.getStepX());
        double offZ = localOffset(hit.z, pos.getZ(), face.getStepZ());

        if (here.is(this)) {
            BooleanProperty corner = cornerFor(offX, offZ);
            return here.setValue(corner, true);
        }

        FluidState fluid = ctx.getLevel().getFluidState(pos);
        BooleanProperty corner = cornerFor(
                placementOffsetX(face, hit.x, pos),
                placementOffsetZ(face, hit.z, pos));
        return this.defaultBlockState()
                .setValue(NW, false)
                .setValue(NE, false)
                .setValue(SW, false)
                .setValue(SE, false)
                .setValue(corner, true)
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
        double offX = localOffset(hit.x, pos.getX(), face.getStepX());
        double offZ = localOffset(hit.z, pos.getZ(), face.getStepZ());
        BooleanProperty corner = cornerFor(offX, offZ);
        return !state.getValue(corner);
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
    public boolean canPlaceLiquid(@Nullable Player player, BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
        return !isFull(state) && SimpleWaterloggedBlock.super.canPlaceLiquid(player, level, pos, state, fluid);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbor,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, direction, neighbor, level, pos, neighborPos);
    }

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

    // 1.21.x made these parent callbacks protected; keep only public behavior delegation.
}
