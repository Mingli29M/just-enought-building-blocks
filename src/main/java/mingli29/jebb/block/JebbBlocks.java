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
import java.util.List;
import java.util.Map;

public final class JebbBlocks {
    public static Block OAK_SQUARE_BRICK;
    public static Block ACACIA_SQUARE_BRICK;
    public static Block BAMBOO_SQUARE_BRICK;
    public static Block BIRCH_SQUARE_BRICK;
    public static Block CHERRY_SQUARE_BRICK;
    public static Block CRIMSON_SQUARE_BRICK;
    public static Block DARK_OAK_SQUARE_BRICK;
    public static Block JUNGLE_SQUARE_BRICK;
    public static Block MANGROVE_SQUARE_BRICK;
    public static Block PALE_OAK_SQUARE_BRICK;
    public static Block SPRUCE_SQUARE_BRICK;
    public static Block WARPED_SQUARE_BRICK;
    public static Block CHISELED_ACACIA_PLANKS;
    public static Block CHISELED_BAMBOO_PLANKS;
    public static Block CHISELED_BIRCH_PLANKS;
    public static Block CHISELED_CHERRY_PLANKS;
    public static Block CHISELED_CRIMSON_PLANKS;
    public static Block CHISELED_DARK_OAK_PLANKS;
    public static Block CHISELED_JUNGLE_PLANKS;
    public static Block CHISELED_MANGROVE_PLANKS;
    public static Block CHISELED_OAK_PLANKS;
    public static Block CHISELED_PALE_OAK_PLANKS;
    public static Block CHISELED_SPRUCE_PLANKS;
    public static Block CHISELED_WARPED_PLANKS;

    public static final Map<Block, JebbVerticalSlabBlock> VERTICAL_SLABS = new LinkedHashMap<>();
    public static final Map<Block, JebbQuarterBlock> QUARTERS = new LinkedHashMap<>();
    public static final Map<Block, JebbCornerPillarBlock> CORNER_PILLARS = new LinkedHashMap<>();

    private JebbBlocks() {
    }

    public static void init() {
        OAK_SQUARE_BRICK = registerSimpleBlock("oak_square_brick", Blocks.OAK_PLANKS);
        ACACIA_SQUARE_BRICK = registerSimpleBlock("acacia_square_brick", Blocks.ACACIA_PLANKS);
        BAMBOO_SQUARE_BRICK = registerSimpleBlock("bamboo_square_brick", Blocks.BAMBOO_PLANKS);
        BIRCH_SQUARE_BRICK = registerSimpleBlock("birch_square_brick", Blocks.BIRCH_PLANKS);
        CHERRY_SQUARE_BRICK = registerSimpleBlock("cherry_square_brick", Blocks.CHERRY_PLANKS);
        CRIMSON_SQUARE_BRICK = registerSimpleBlock("crimson_square_brick", Blocks.CRIMSON_PLANKS);
        DARK_OAK_SQUARE_BRICK = registerSimpleBlock("dark_oak_square_brick", Blocks.DARK_OAK_PLANKS);
        JUNGLE_SQUARE_BRICK = registerSimpleBlock("jungle_square_brick", Blocks.JUNGLE_PLANKS);
        MANGROVE_SQUARE_BRICK = registerSimpleBlock("mangrove_square_brick", Blocks.MANGROVE_PLANKS);
        PALE_OAK_SQUARE_BRICK = registerSimpleBlock("pale_oak_square_brick", Blocks.OAK_PLANKS);
        SPRUCE_SQUARE_BRICK = registerSimpleBlock("spruce_square_brick", Blocks.SPRUCE_PLANKS);
        WARPED_SQUARE_BRICK = registerSimpleBlock("warped_square_brick", Blocks.WARPED_PLANKS);

        CHISELED_ACACIA_PLANKS = registerSimpleBlock("chiseled_acacia_planks", Blocks.ACACIA_PLANKS);
        CHISELED_BAMBOO_PLANKS = registerSimpleBlock("chiseled_bamboo_planks", Blocks.BAMBOO_PLANKS);
        CHISELED_BIRCH_PLANKS = registerSimpleBlock("chiseled_birch_planks", Blocks.BIRCH_PLANKS);
        CHISELED_CHERRY_PLANKS = registerSimpleBlock("chiseled_cherry_planks", Blocks.CHERRY_PLANKS);
        CHISELED_CRIMSON_PLANKS = registerSimpleBlock("chiseled_crimson_planks", Blocks.CRIMSON_PLANKS);
        CHISELED_DARK_OAK_PLANKS = registerSimpleBlock("chiseled_dark_oak_planks", Blocks.DARK_OAK_PLANKS);
        CHISELED_JUNGLE_PLANKS = registerSimpleBlock("chiseled_jungle_planks", Blocks.JUNGLE_PLANKS);
        CHISELED_MANGROVE_PLANKS = registerSimpleBlock("chiseled_mangrove_planks", Blocks.MANGROVE_PLANKS);
        CHISELED_OAK_PLANKS = registerSimpleBlock("chiseled_oak_planks", Blocks.OAK_PLANKS);
        CHISELED_PALE_OAK_PLANKS = registerSimpleBlock("chiseled_pale_oak_planks", Blocks.OAK_PLANKS);
        CHISELED_SPRUCE_PLANKS = registerSimpleBlock("chiseled_spruce_planks", Blocks.SPRUCE_PLANKS);
        CHISELED_WARPED_PLANKS = registerSimpleBlock("chiseled_warped_planks", Blocks.WARPED_PLANKS);

        for (Block parent : BuiltInRegistries.BLOCK) {
            if (!JebbBlockFilter.qualifies(parent)) continue;
            registerVariantsForParent(parent);
        }
        for (Block parent : List.of(
                OAK_SQUARE_BRICK,
                ACACIA_SQUARE_BRICK,
                BAMBOO_SQUARE_BRICK,
                BIRCH_SQUARE_BRICK,
                CHERRY_SQUARE_BRICK,
                CRIMSON_SQUARE_BRICK,
                DARK_OAK_SQUARE_BRICK,
                JUNGLE_SQUARE_BRICK,
                MANGROVE_SQUARE_BRICK,
                PALE_OAK_SQUARE_BRICK,
                SPRUCE_SQUARE_BRICK,
                WARPED_SQUARE_BRICK,
                CHISELED_ACACIA_PLANKS,
                CHISELED_BAMBOO_PLANKS,
                CHISELED_BIRCH_PLANKS,
                CHISELED_CHERRY_PLANKS,
                CHISELED_CRIMSON_PLANKS,
                CHISELED_DARK_OAK_PLANKS,
                CHISELED_JUNGLE_PLANKS,
                CHISELED_MANGROVE_PLANKS,
                CHISELED_OAK_PLANKS,
                CHISELED_PALE_OAK_PLANKS,
                CHISELED_SPRUCE_PLANKS,
                CHISELED_WARPED_PLANKS
        )) {
            registerVariantsForParent(parent);
        }

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

    private static Block registerSimpleBlock(String name, Block parent) {
        Block block = registerBlock(name, new Block(JebbBlockProps.copyFromParent(parent)));
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, name),
                new BlockItem(block, new Item.Properties()));
        return block;
    }
}
