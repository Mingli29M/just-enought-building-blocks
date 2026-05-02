package mingli29.jebb.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * {@link BlockBehaviour.Properties#copy(BlockBehaviour)} copies {@code mapColor}, {@code lightEmission},
 * {@code emissiveRendering}, etc. as function references that may read parent-only properties (e.g. {@code LIT}).
 * Our variant blocks do not define those properties, so we must replace those functions with constants
 * taken from the parent's <em>default</em> state.
 */
public final class JebbBlockProps {
    private JebbBlockProps() {
    }

    public static BlockBehaviour.Properties copyFromParent(Block parent) {
        BlockState def = parent.defaultBlockState();
        BlockBehaviour.Properties p = BlockBehaviour.Properties.copy(parent);
        p.mapColor(def.getMapColor(EmptyBlockGetter.INSTANCE, BlockPos.ZERO));
        int light = def.getLightEmission();
        p.lightLevel(state -> light);
        boolean emissive = def.emissiveRendering(EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
        p.emissiveRendering((state, level, pos) -> emissive);
        return p;
    }
}
