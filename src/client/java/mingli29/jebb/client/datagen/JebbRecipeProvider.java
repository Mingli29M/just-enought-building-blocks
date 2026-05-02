package mingli29.jebb.client.datagen;

import mingli29.jebb.block.JebbBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.level.block.Block;

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
            RecipeProvider.stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, vs, parent, 2);
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, vs, 6)
                    .define('#', parent)
                    .pattern("###")
                    .unlockedBy(RecipeProvider.getHasName(parent), RecipeProvider.has(parent))
                    .save(output);
        }
        for (var e : JebbBlocks.QUARTERS.entrySet()) {
            Block parent = e.getKey();
            Block q = e.getValue();
            RecipeProvider.stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, q, parent, 4);
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, q, 4)
                    .define('#', parent)
                    .pattern("##")
                    .unlockedBy(RecipeProvider.getHasName(parent), RecipeProvider.has(parent))
                    .save(output);
        }
    }
}
