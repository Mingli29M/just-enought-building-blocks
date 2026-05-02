package mingli29.jebb.block;

import mingli29.jebb.JustEnoughtBuildingBlocks;
import mingli29.jebb.util.JebbBlockFilter;
import mingli29.jebb.util.JebbBlockProps;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.LinkedHashMap;
import java.util.Map;

public final class JebbBlocks {
    public static final Map<Block, JebbVerticalSlabBlock> VERTICAL_SLABS = new LinkedHashMap<>();
    public static final Map<Block, JebbQuarterBlock> QUARTERS = new LinkedHashMap<>();

    private JebbBlocks() {
    }

    public static void init() {
        for (Block parent : BuiltInRegistries.BLOCK) {
            if (!JebbBlockFilter.qualifies(parent)) continue;
            ResourceLocation parentKey = BuiltInRegistries.BLOCK.getKey(parent);
            String parentPath = parentKey.getPath();

            JebbVerticalSlabBlock vs = registerBlock(
                    "vertical_slab_" + parentPath,
                    new JebbVerticalSlabBlock(JebbBlockProps.copyFromParent(parent)));
            JebbQuarterBlock q = registerBlock(
                    "quarter_" + parentPath,
                    new JebbQuarterBlock(JebbBlockProps.copyFromParent(parent)));

            registerItem("vertical_slab_" + parentPath, vs);
            registerItem("quarter_" + parentPath, q);

            VERTICAL_SLABS.put(parent, vs);
            QUARTERS.put(parent, q);
        }
        JustEnoughtBuildingBlocks.LOGGER.info(
                "Registered {} vertical slab variants and {} quarter variants",
                VERTICAL_SLABS.size(), QUARTERS.size());
    }

    private static <B extends Block> B registerBlock(String name, B block) {
        ResourceLocation id = new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, name);
        return Registry.register(BuiltInRegistries.BLOCK, id, block);
    }

    private static Item registerItem(String name, Block block) {
        ResourceLocation id = new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, name);
        BlockItem item = new BlockItem(block, new Item.Properties());
        Item registered = Registry.register(BuiltInRegistries.ITEM, id, item);
        item.registerBlocks(Item.BY_BLOCK, item);
        return registered;
    }
}
