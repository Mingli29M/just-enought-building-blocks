package mingli29.jebb.client.datagen;

import mingli29.jebb.JustEnoughtBuildingBlocks;
import mingli29.jebb.block.JebbBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Optional;
import java.util.function.Consumer;

public class JebbRecipeProvider extends FabricRecipeProvider {
    public JebbRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> output) {
        for (var e : JebbBlocks.VERTICAL_SLABS.entrySet()) {
            Block parent = e.getKey();
            Block vs = e.getValue();
            Block q = JebbBlocks.QUARTERS.get(parent);
            Block cp = JebbBlocks.CORNER_PILLARS.get(parent);
            if (cp == null) {
                continue;
            }
            if (parent.asItem() == Items.AIR) {
                continue;
            }
            String parentPath = BuiltInRegistries.BLOCK.getKey(parent).getPath();

            RecipeProvider.stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, vs, parent, 2);
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, vs, 6)
                    .define('#', parent)
                    .pattern("###")
                    .unlockedBy(RecipeProvider.getHasName(parent), RecipeProvider.has(parent))
                    .save(output);

            if (q != null) {
                RecipeProvider.stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, q, parent, 4);
                ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, q, 4)
                        .define('#', parent)
                        .pattern("##")
                        .unlockedBy(RecipeProvider.getHasName(parent), RecipeProvider.has(parent))
                        .save(output);
            }

            RecipeProvider.stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, cp, parent, 4);
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, cp, 6)
                    .define('#', parent)
                    .pattern("#")
                    .pattern("#")
                    .unlockedBy(RecipeProvider.getHasName(parent), RecipeProvider.has(parent))
                    .save(output);

            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, parent)
                    .requires(vs, 2)
                    .unlockedBy(RecipeProvider.getHasName(vs), RecipeProvider.has(vs))
                    .save(output, modId(parentPath + "_from_vertical_slab"));

            if (q != null) {
                ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, parent)
                        .requires(q, 4)
                        .unlockedBy(RecipeProvider.getHasName(q), RecipeProvider.has(q))
                        .save(output, modId(parentPath + "_from_quarter"));
            }

            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, parent)
                    .requires(cp, 4)
                    .unlockedBy(RecipeProvider.getHasName(cp), RecipeProvider.has(cp))
                    .save(output, modId(parentPath + "_from_corner_pillar"));

            if (q != null) {
                ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, vs)
                        .requires(q, 2)
                        .unlockedBy(RecipeProvider.getHasName(q), RecipeProvider.has(q))
                        .save(output, modId("vertical_slab_" + parentPath + "_from_quarter"));

                RecipeProvider.stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, q, vs, 2);
                RecipeProvider.stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, q, cp, 1);
            }

            RecipeProvider.stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, cp, vs, 2);

            Optional<Block> vanillaSlab = vanillaSlabForParent(parent);
            if (vanillaSlab.isPresent()) {
                ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, vanillaSlab.get())
                        .requires(vs)
                        .unlockedBy(RecipeProvider.getHasName(vs), RecipeProvider.has(vs))
                        .save(output, modId("slab_from_vertical_slab_" + parentPath));

                ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, vs)
                        .requires(vanillaSlab.get())
                        .unlockedBy(RecipeProvider.getHasName(vanillaSlab.get()), RecipeProvider.has(vanillaSlab.get()))
                        .save(output, modId("vertical_slab_" + parentPath + "_from_slab"));
            }
        }

        registerWoodRecipe(output, JebbBlocks.OAK_SQUARE_BRICK, Blocks.OAK_PLANKS);
        registerWoodRecipe(output, JebbBlocks.ACACIA_SQUARE_BRICK, Blocks.ACACIA_PLANKS);
        registerWoodRecipe(output, JebbBlocks.BAMBOO_SQUARE_BRICK, Blocks.BAMBOO_PLANKS);
        registerWoodRecipe(output, JebbBlocks.BIRCH_SQUARE_BRICK, Blocks.BIRCH_PLANKS);
        registerWoodRecipe(output, JebbBlocks.CHERRY_SQUARE_BRICK, Blocks.CHERRY_PLANKS);
        registerWoodRecipe(output, JebbBlocks.CRIMSON_SQUARE_BRICK, Blocks.CRIMSON_PLANKS);
        registerWoodRecipe(output, JebbBlocks.DARK_OAK_SQUARE_BRICK, Blocks.DARK_OAK_PLANKS);
        registerWoodRecipe(output, JebbBlocks.JUNGLE_SQUARE_BRICK, Blocks.JUNGLE_PLANKS);
        registerWoodRecipe(output, JebbBlocks.MANGROVE_SQUARE_BRICK, Blocks.MANGROVE_PLANKS);
        registerWoodRecipe(output, JebbBlocks.PALE_OAK_SQUARE_BRICK, Blocks.OAK_PLANKS);
        registerWoodRecipe(output, JebbBlocks.SPRUCE_SQUARE_BRICK, Blocks.SPRUCE_PLANKS);
        registerWoodRecipe(output, JebbBlocks.WARPED_SQUARE_BRICK, Blocks.WARPED_PLANKS);

        registerWoodRecipe(output, JebbBlocks.CHISELED_ACACIA_PLANKS, Blocks.ACACIA_PLANKS);
        registerWoodRecipe(output, JebbBlocks.CHISELED_BAMBOO_PLANKS, Blocks.BAMBOO_PLANKS);
        registerWoodRecipe(output, JebbBlocks.CHISELED_BIRCH_PLANKS, Blocks.BIRCH_PLANKS);
        registerWoodRecipe(output, JebbBlocks.CHISELED_CHERRY_PLANKS, Blocks.CHERRY_PLANKS);
        registerWoodRecipe(output, JebbBlocks.CHISELED_CRIMSON_PLANKS, Blocks.CRIMSON_PLANKS);
        registerWoodRecipe(output, JebbBlocks.CHISELED_DARK_OAK_PLANKS, Blocks.DARK_OAK_PLANKS);
        registerWoodRecipe(output, JebbBlocks.CHISELED_JUNGLE_PLANKS, Blocks.JUNGLE_PLANKS);
        registerWoodRecipe(output, JebbBlocks.CHISELED_MANGROVE_PLANKS, Blocks.MANGROVE_PLANKS);
        registerWoodRecipe(output, JebbBlocks.CHISELED_OAK_PLANKS, Blocks.OAK_PLANKS);
        registerWoodRecipe(output, JebbBlocks.CHISELED_PALE_OAK_PLANKS, Blocks.OAK_PLANKS);
        registerWoodRecipe(output, JebbBlocks.CHISELED_SPRUCE_PLANKS, Blocks.SPRUCE_PLANKS);
        registerWoodRecipe(output, JebbBlocks.CHISELED_WARPED_PLANKS, Blocks.WARPED_PLANKS);
    }

    private static void registerWoodRecipe(Consumer<FinishedRecipe> output, Block result, Block parent) {
        RecipeProvider.stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, result, parent, 1);
    }

    /**
     * Vanilla horizontal slab id for a full-block parent, when one exists (e.g. oak_planks -> oak_slab).
     */
    private static Optional<Block> vanillaSlabForParent(Block parent) {
        ResourceLocation key = BuiltInRegistries.BLOCK.getKey(parent);
        if (key == null || !"minecraft".equals(key.getNamespace())) {
            return Optional.empty();
        }
        String path = key.getPath();
        String slabPath;
        if (path.endsWith("_planks")) {
            slabPath = path.substring(0, path.length() - "_planks".length()) + "_slab";
        } else {
            slabPath = path + "_slab";
        }
        return BuiltInRegistries.BLOCK.getOptional(new ResourceLocation("minecraft", slabPath));
    }

    private static ResourceLocation modId(String path) {
        return new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, path);
    }
}
