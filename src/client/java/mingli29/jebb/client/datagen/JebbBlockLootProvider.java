package mingli29.jebb.client.datagen;

import mingli29.jebb.block.JebbBlocks;
import mingli29.jebb.block.JebbCornerPillarBlock;
import mingli29.jebb.block.JebbQuarterBlock;
import mingli29.jebb.block.JebbVerticalSlabBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
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
    public JebbBlockLootProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        for (Block vs : JebbBlocks.VERTICAL_SLABS.values()) {
            this.add(vs, verticalSlabDrops(vs));
        }
        for (Block q : JebbBlocks.QUARTERS.values()) {
            this.add(q, quarterDrops(q));
        }
        for (Block cp : JebbBlocks.CORNER_PILLARS.values()) {
            this.add(cp, cornerPillarDrops(cp));
        }
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
