package mingli29.jebb.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Optional;

public final class JebbSlabs {
    private JebbSlabs() {
    }

    public static Optional<Block> vanillaSlabForParent(Block parent) {
        ResourceLocation key = BuiltInRegistries.BLOCK.getKey(parent);
        if (key == null) {
            return Optional.empty();
        }
        ResourceLocation slabId = slabIdForParent(key);
        return BuiltInRegistries.BLOCK.getOptional(slabId)
                .filter(block -> block != Blocks.AIR);
    }

    private static ResourceLocation slabIdForParent(ResourceLocation parentId) {
        String namespace = parentId.getNamespace();
        String path = parentId.getPath();

        if (path.endsWith("_planks")) {
            return ResourceLocation.fromNamespaceAndPath(namespace, path.replace("_planks", "_slab"));
        }
        if ("bricks".equals(path)) {
            return ResourceLocation.fromNamespaceAndPath(namespace, "brick_slab");
        }
        if (path.endsWith("_bricks")) {
            return ResourceLocation.fromNamespaceAndPath(namespace, path.replace("_bricks", "_brick_slab"));
        }
        if ("quartz_block".equals(path)) {
            return ResourceLocation.fromNamespaceAndPath(namespace, "quartz_slab");
        }

        return ResourceLocation.fromNamespaceAndPath(namespace, path + "_slab");
    }
}
