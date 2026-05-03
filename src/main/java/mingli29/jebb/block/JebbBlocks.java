package mingli29.jebb.block;

import mingli29.jebb.JustEnoughtBuildingBlocks;
import mingli29.jebb.item.JebbVariantBlockItem;
import mingli29.jebb.util.JebbBlockFilter;
import mingli29.jebb.util.JebbBlockProps;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.LinkedHashMap;
import java.util.Map;

public final class JebbBlocks {
    public static Block OAK_MUZHUAN;

    public static final Map<Block, JebbVerticalSlabBlock> VERTICAL_SLABS = new LinkedHashMap<>();
    public static final Map<Block, JebbQuarterBlock> QUARTERS = new LinkedHashMap<>();
    public static final Map<Block, JebbCornerPillarBlock> CORNER_PILLARS = new LinkedHashMap<>();

    private JebbBlocks() {
    }

    public static void init() {
        OAK_MUZHUAN = registerBlock("oak_muzhuan", new Block(JebbBlockProps.copyFromParent(Blocks.OAK_PLANKS)));
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "oak_muzhuan"),
                new BlockItem(OAK_MUZHUAN, new Item.Properties()));

        for (Block parent : BuiltInRegistries.BLOCK) {
            if (!JebbBlockFilter.qualifies(parent)) continue;
            registerVariantsForParent(parent);
        }
        registerVariantsForParent(OAK_MUZHUAN);

        JustEnoughtBuildingBlocks.LOGGER.info(
                "Registered {} vertical slab, {} quarter, {} corner pillar variants",
                VERTICAL_SLABS.size(), QUARTERS.size(), CORNER_PILLARS.size());
    }

    private static void registerVariantsForParent(Block parent) {
        ResourceLocation parentKey = BuiltInRegistries.BLOCK.getKey(parent);
        String parentPath = parentKey.getPath();
        boolean skipQuarter = JebbBlockFilter.skipsQuarterVariants(parent);

        JebbVerticalSlabBlock vs = registerBlock(
                "vertical_slab_" + parentPath,
                new JebbVerticalSlabBlock(parent, JebbBlockProps.copyFromParent(parent)));
        JebbQuarterBlock q = null;
        if (!skipQuarter) {
            q = registerBlock(
                    "quarter_" + parentPath,
                    new JebbQuarterBlock(parent, JebbBlockProps.copyFromParent(parent)));
            registerVariantItem("quarter_" + parentPath, q, parent, "jebb.quarter");
        }
        JebbCornerPillarBlock cp = registerBlock(
                "corner_pillar_" + parentPath,
                new JebbCornerPillarBlock(parent, JebbBlockProps.copyFromParent(parent)));

        registerVariantItem("vertical_slab_" + parentPath, vs, parent, "jebb.vertical_slab");
        registerVariantItem("corner_pillar_" + parentPath, cp, parent, "jebb.corner_pillar");

        VERTICAL_SLABS.put(parent, vs);
        if (q != null) {
            QUARTERS.put(parent, q);
        }
        CORNER_PILLARS.put(parent, cp);
    }

    private static <B extends Block> B registerBlock(String name, B block) {
        ResourceLocation id = new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, name);
        return Registry.register(BuiltInRegistries.BLOCK, id, block);
    }

    private static Item registerVariantItem(String name, Block block, Block parent, String prefixKey) {
        ResourceLocation id = new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, name);
        BlockItem item = new JebbVariantBlockItem(block, parent, prefixKey, new Item.Properties());
        Item registered = Registry.register(BuiltInRegistries.ITEM, id, item);
        item.registerBlocks(Item.BY_BLOCK, item);
        return registered;
    }
}
