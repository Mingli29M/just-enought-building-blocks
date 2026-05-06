package mingli29.jebb.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public final class JebbSlabIds {
    private JebbSlabIds() {
    }

    /**
     * Vanilla horizontal slab for a full-block parent, when one exists (e.g. oak_planks -> oak_slab).
     */
    public static Optional<Block> vanillaSlabForParent(Block parent) {
        ResourceLocation key = BuiltInRegistries.BLOCK.getKey(parent);
        if (key == null || !"minecraft".equals(key.getNamespace())) {
            return Optional.empty();
        }
        String path = key.getPath();
        String slabPath = path.endsWith("_planks")
                ? path.substring(0, path.length() - "_planks".length()) + "_slab"
                : path + "_slab";
        return BuiltInRegistries.BLOCK.getOptional(ResourceLocation.fromNamespaceAndPath("minecraft", slabPath));
    }
}
