package mingli29.jebb.client.datagen;

import mingli29.jebb.block.JebbBlocks;
import mingli29.jebb.block.JebbCornerPillarBlock;
import mingli29.jebb.block.JebbQuarterBlock;
import mingli29.jebb.block.JebbVerticalSlabBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class JebbBlockLootProvider extends FabricBlockLootTableProvider {
    public JebbBlockLootProvider(FabricDataOutput dataOutput, java.util.concurrent.CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(dataOutput, registriesFuture);
    }

    @Override
    public void generate() {
        // Add square brick variants
        addSimpleWoodBlockLoot(JebbBlocks.OAK_SQUARE_BRICK);
        addSimpleWoodBlockLoot(JebbBlocks.ACACIA_SQUARE_BRICK);
        addSimpleWoodBlockLoot(JebbBlocks.BAMBOO_SQUARE_BRICK);
        addSimpleWoodBlockLoot(JebbBlocks.BIRCH_SQUARE_BRICK);
        addSimpleWoodBlockLoot(JebbBlocks.CHERRY_SQUARE_BRICK);
        addSimpleWoodBlockLoot(JebbBlocks.CRIMSON_SQUARE_BRICK);
        addSimpleWoodBlockLoot(JebbBlocks.DARK_OAK_SQUARE_BRICK);
        addSimpleWoodBlockLoot(JebbBlocks.JUNGLE_SQUARE_BRICK);
        addSimpleWoodBlockLoot(JebbBlocks.MANGROVE_SQUARE_BRICK);
        addSimpleWoodBlockLoot(JebbBlocks.SPRUCE_SQUARE_BRICK);
        addSimpleWoodBlockLoot(JebbBlocks.WARPED_SQUARE_BRICK);

        // Add chiseled planks variants
        addSimpleWoodBlockLoot(JebbBlocks.CHISELED_ACACIA_PLANKS);
        addSimpleWoodBlockLoot(JebbBlocks.CHISELED_BAMBOO_PLANKS);
        addSimpleWoodBlockLoot(JebbBlocks.CHISELED_BIRCH_PLANKS);
        addSimpleWoodBlockLoot(JebbBlocks.CHISELED_CHERRY_PLANKS);
        addSimpleWoodBlockLoot(JebbBlocks.CHISELED_CRIMSON_PLANKS);
        addSimpleWoodBlockLoot(JebbBlocks.CHISELED_DARK_OAK_PLANKS);
        addSimpleWoodBlockLoot(JebbBlocks.CHISELED_JUNGLE_PLANKS);
        addSimpleWoodBlockLoot(JebbBlocks.CHISELED_MANGROVE_PLANKS);
        addSimpleWoodBlockLoot(JebbBlocks.CHISELED_OAK_PLANKS);
        addSimpleWoodBlockLoot(JebbBlocks.CHISELED_SPRUCE_PLANKS);
        addSimpleWoodBlockLoot(JebbBlocks.CHISELED_WARPED_PLANKS);

        // Triangle blocks
        addSimpleWoodBlockLoot(JebbBlocks.OAK_TRIANGLE_BLOCK);
        addSimpleWoodBlockLoot(JebbBlocks.ACACIA_TRIANGLE_BLOCK);
        addSimpleWoodBlockLoot(JebbBlocks.BAMBOO_TRIANGLE_BLOCK);
        addSimpleWoodBlockLoot(JebbBlocks.BIRCH_TRIANGLE_BLOCK);
        addSimpleWoodBlockLoot(JebbBlocks.CHEERY_TRIANGLE_BLOCK);
        addSimpleWoodBlockLoot(JebbBlocks.CRIMSON_TRIANGLE_BLOCK);
        addSimpleWoodBlockLoot(JebbBlocks.DARK_OAK_TRIANGLE_BLOCK);
        addSimpleWoodBlockLoot(JebbBlocks.JUNGLE_TRIANGLE_BLOCK);
        addSimpleWoodBlockLoot(JebbBlocks.MANGROVE_TRIANGLE_BLOCK);
        addSimpleWoodBlockLoot(JebbBlocks.SPRUCE_TRIANGLE_BLOCK);
        addSimpleWoodBlockLoot(JebbBlocks.WARPED_TRIANGLE_BLOCK);

        // Striped blocks
        addSimpleWoodBlockLoot(JebbBlocks.STRIPED_OAK);
        addSimpleWoodBlockLoot(JebbBlocks.STRIPED_DARK_OAK);
        addSimpleWoodBlockLoot(JebbBlocks.STRIPED_BAMBOO);
        addSimpleWoodBlockLoot(JebbBlocks.STRIPED_ACACIA_PLANK);
        addSimpleWoodBlockLoot(JebbBlocks.STRIPED_CHEERY_PLANK);
        addSimpleWoodBlockLoot(JebbBlocks.STRIPED_CRIMSON_PLANK);
        addSimpleWoodBlockLoot(JebbBlocks.STRIPED_JUNGLE_PLANK);
        addSimpleWoodBlockLoot(JebbBlocks.STRIPED_MANGROVE_PLANK);
        addSimpleWoodBlockLoot(JebbBlocks.STRIPED_SPRUCE_PLANK);
        addSimpleWoodBlockLoot(JebbBlocks.STRIPED_WARPED_PLANK);
        addSimpleWoodBlockLoot(JebbBlocks.STRIPED_TRIANGLE_BLOCK);

        // Add variant blocks (vertical slabs, quarters, corner pillars)
        for (Block vs : JebbBlocks.VERTICAL_SLABS.values()) {
            this.add(vs, verticalSlabDrops(vs));
        }
        for (Block q : JebbBlocks.QUARTERS.values()) {
            this.add(q, quarterDrops(q));
        }
        for (Block cp : JebbBlocks.CORNER_PILLARS.values()) {
            this.add(cp, cornerPillarDrops(cp));
        }
        for (Block hs : JebbBlocks.HORIZONTAL_SLABS.values()) {
            this.add(hs, horizontalSlabDrops(hs));
        }
    }

    private void addSimpleWoodBlockLoot(Block block) {
        this.add(block, LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(this.applyExplosionDecay(
                                        block,
                                        LootItem.lootTableItem(block)))));
    }

    private LootTable.Builder cornerPillarDrops(Block block) {
        LootTable.Builder builder = LootTable.lootTable();
        addCornerPool(builder, block, JebbCornerPillarBlock.NW);
        addCornerPool(builder, block, JebbCornerPillarBlock.NE);
        addCornerPool(builder, block, JebbCornerPillarBlock.SW);
        addCornerPool(builder, block, JebbCornerPillarBlock.SE);
        return builder;
    }

    private void addCornerPool(LootTable.Builder builder, Block block, BooleanProperty corner) {
        builder.withPool(
                LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(
                                this.applyExplosionDecay(
                                        block,
                                        LootItem.lootTableItem(block)
                                                .when(
                                                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                                .setProperties(
                                                                        StatePropertiesPredicate.Builder.properties()
                                                                                .hasProperty(corner, true))))));
    }

    private LootTable.Builder horizontalSlabDrops(Block block) {
        return LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(
                                        this.applyExplosionDecay(
                                                block,
                                                LootItem.lootTableItem(block)
                                                        .apply(
                                                                SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))
                                                                        .when(
                                                                                LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                                                        .setProperties(
                                                                                                StatePropertiesPredicate.Builder.properties()
                                                                                                        .hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))))));
    }

    private LootTable.Builder verticalSlabDrops(Block block) {
        return LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(
                                        this.applyExplosionDecay(
                                                block,
                                                LootItem.lootTableItem(block)
                                                        .apply(
                                                                SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))
                                                                        .when(
                                                                                LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                                                        .setProperties(
                                                                                                StatePropertiesPredicate.Builder.properties()
                                                                                                        .hasProperty(JebbVerticalSlabBlock.DOUBLED, true)))))));
    }

    private LootTable.Builder quarterDrops(Block block) {
        LootTable.Builder builder = LootTable.lootTable();
        addQuarterPool(builder, block, JebbQuarterBlock.BOTTOM_NEAR);
        addQuarterPool(builder, block, JebbQuarterBlock.BOTTOM_FAR);
        addQuarterPool(builder, block, JebbQuarterBlock.TOP_NEAR);
        addQuarterPool(builder, block, JebbQuarterBlock.TOP_FAR);
        return builder;
    }

    private void addQuarterPool(LootTable.Builder builder, Block block, BooleanProperty quad) {
        builder.withPool(
                LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(
                                this.applyExplosionDecay(
                                        block,
                                        LootItem.lootTableItem(block)
                                                .when(
                                                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                                .setProperties(
                                                                        StatePropertiesPredicate.Builder.properties()
                                                                                .hasProperty(quad, true))))));
    }
}
