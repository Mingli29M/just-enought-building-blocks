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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

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
            if (q == null || cp == null) continue;
            String parentPath = BuiltInRegistries.BLOCK.getKey(parent).getPath();

            RecipeProvider.stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, vs, parent, 2);
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, vs, 6)
                    .define('#', parent)
                    .pattern("###")
                    .unlockedBy(RecipeProvider.getHasName(parent), RecipeProvider.has(parent))
                    .save(output);

            RecipeProvider.stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, q, parent, 4);
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, q, 4)
                    .define('#', parent)
                    .pattern("##")
                    .unlockedBy(RecipeProvider.getHasName(parent), RecipeProvider.has(parent))
                    .save(output);

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

            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, parent)
                    .requires(q, 4)
                    .unlockedBy(RecipeProvider.getHasName(q), RecipeProvider.has(q))
                    .save(output, modId(parentPath + "_from_quarter"));

            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, parent)
                    .requires(cp, 4)
                    .unlockedBy(RecipeProvider.getHasName(cp), RecipeProvider.has(cp))
                    .save(output, modId(parentPath + "_from_corner_pillar"));

            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, vs)
                    .requires(q, 2)
                    .unlockedBy(RecipeProvider.getHasName(q), RecipeProvider.has(q))
                    .save(output, modId("vertical_slab_" + parentPath + "_from_quarter"));

            RecipeProvider.stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, q, vs, 2);
            RecipeProvider.stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, cp, vs, 2);
            RecipeProvider.stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, q, cp, 2);
        }

        RecipeProvider.stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, JebbBlocks.OAK_MUZHUAN, Blocks.OAK_PLANKS, 1);
    }

    private static ResourceLocation modId(String path) {
        return new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, path);
    }
}
