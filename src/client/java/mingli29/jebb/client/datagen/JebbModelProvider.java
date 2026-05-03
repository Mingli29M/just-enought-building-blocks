package mingli29.jebb.client.datagen;

import mingli29.jebb.JustEnoughtBuildingBlocks;
import mingli29.jebb.block.JebbBlocks;
import mingli29.jebb.block.JebbCornerPillarBlock;
import mingli29.jebb.block.JebbQuarterBlock;
import mingli29.jebb.block.JebbVerticalSlabBlock;
import mingli29.jebb.util.JebbTextureMap;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.Condition;
import net.minecraft.data.models.blockstates.MultiPartGenerator;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.Optional;

public class JebbModelProvider extends FabricModelProvider {
    private static ResourceLocation modLoc(String path) {
        return new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, path);
    }

    private static ModelTemplate templateAll(String path) {
        return new ModelTemplate(Optional.of(modLoc("block/" + path)), Optional.empty(), TextureSlot.ALL);
    }

    private static ModelTemplate templateBts(String path) {
        return new ModelTemplate(Optional.of(modLoc("block/" + path)), Optional.empty(),
                TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.BOTTOM);
    }

    private static final ModelTemplate VS_CHILD = templateAll("template_vertical_slab");
    private static final ModelTemplate Q_BN = templateAll("template_quarter_bn");
    private static final ModelTemplate Q_BF = templateAll("template_quarter_bf");
    private static final ModelTemplate Q_TN = templateAll("template_quarter_tn");
    private static final ModelTemplate Q_TF = templateAll("template_quarter_tf");
    private static final ModelTemplate CP_NW = templateAll("template_corner_pillar_nw");
    private static final ModelTemplate CP_NE = templateAll("template_corner_pillar_ne");
    private static final ModelTemplate CP_SW = templateAll("template_corner_pillar_sw");
    private static final ModelTemplate CP_SE = templateAll("template_corner_pillar_se");

    private static final ModelTemplate VS_BTS = templateBts("template_vertical_slab_bts");
    private static final ModelTemplate Q_BN_BTS = templateBts("template_quarter_bn_bts");
    private static final ModelTemplate Q_BF_BTS = templateBts("template_quarter_bf_bts");
    private static final ModelTemplate Q_TN_BTS = templateBts("template_quarter_tn_bts");
    private static final ModelTemplate Q_TF_BTS = templateBts("template_quarter_tf_bts");
    private static final ModelTemplate CP_NW_BTS = templateBts("template_corner_pillar_nw_bts");
    private static final ModelTemplate CP_NE_BTS = templateBts("template_corner_pillar_ne_bts");
    private static final ModelTemplate CP_SW_BTS = templateBts("template_corner_pillar_sw_bts");
    private static final ModelTemplate CP_SE_BTS = templateBts("template_corner_pillar_se_bts");

    private static final ModelTemplate ITEM_VS = templateAll("template_item_vertical_slab");
    private static final ModelTemplate ITEM_Q = templateAll("template_item_quarter");
    private static final ModelTemplate ITEM_CP = templateAll("template_item_corner_pillar");

    private static final ModelTemplate ITEM_VS_BTS = templateBts("template_item_vertical_slab_bts");
    private static final ModelTemplate ITEM_Q_BTS = templateBts("template_item_quarter_bts");
    private static final ModelTemplate ITEM_CP_BTS = templateBts("template_item_corner_pillar_bts");

    public JebbModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators gen) {
        for (var e : JebbBlocks.VERTICAL_SLABS.entrySet()) {
            Block parent = e.getKey();
            JebbVerticalSlabBlock vs = e.getValue();
            String path = BuiltInRegistries.BLOCK.getKey(parent).getPath();
            ResourceLocation slabModel = modLoc("block/vertical_slab_" + path);
            JebbTextureMap.Faces faces = JebbTextureMap.forParent(parent);
            if (faces != null) {
                VS_BTS.create(slabModel, btsMapping(faces), gen.modelOutput);
            } else {
                VS_CHILD.create(slabModel, TextureMapping.cube(parent), gen.modelOutput);
            }
            ResourceLocation parentModel = ModelLocationUtils.getModelLocation(parent);
            gen.blockStateOutput.accept(
                    MultiVariantGenerator.multiVariant(vs)
                            .with(PropertyDispatch.properties(
                                            JebbVerticalSlabBlock.FACING,
                                            JebbVerticalSlabBlock.DOUBLED,
                                            JebbVerticalSlabBlock.WATERLOGGED)
                                    .generate((facing, doubled, _waterlogged) -> {
                                        if (doubled) {
                                            return Variant.variant()
                                                    .with(VariantProperties.MODEL, parentModel);
                                        }
                                        VariantProperties.Rotation y = switch (facing) {
                                            case NORTH -> VariantProperties.Rotation.R0;
                                            case EAST -> VariantProperties.Rotation.R90;
                                            case SOUTH -> VariantProperties.Rotation.R180;
                                            case WEST -> VariantProperties.Rotation.R270;
                                            default -> VariantProperties.Rotation.R0;
                                        };
                                        return Variant.variant()
                                                .with(VariantProperties.MODEL, slabModel)
                                                .with(VariantProperties.Y_ROT, y);
                                    })));
        }

        for (var e : JebbBlocks.QUARTERS.entrySet()) {
            Block parent = e.getKey();
            JebbQuarterBlock q = e.getValue();
            String path = BuiltInRegistries.BLOCK.getKey(parent).getPath();
            JebbTextureMap.Faces faces = JebbTextureMap.forParent(parent);
            ResourceLocation mBn;
            ResourceLocation mBf;
            ResourceLocation mTn;
            ResourceLocation mTf;
            if (faces != null) {
                TextureMapping tex = btsMapping(faces);
                mBn = Q_BN_BTS.create(modLoc("block/quarter_" + path + "_bn"), tex, gen.modelOutput);
                mBf = Q_BF_BTS.create(modLoc("block/quarter_" + path + "_bf"), tex, gen.modelOutput);
                mTn = Q_TN_BTS.create(modLoc("block/quarter_" + path + "_tn"), tex, gen.modelOutput);
                mTf = Q_TF_BTS.create(modLoc("block/quarter_" + path + "_tf"), tex, gen.modelOutput);
            } else {
                TextureMapping tex = TextureMapping.cube(parent);
                mBn = Q_BN.create(modLoc("block/quarter_" + path + "_bn"), tex, gen.modelOutput);
                mBf = Q_BF.create(modLoc("block/quarter_" + path + "_bf"), tex, gen.modelOutput);
                mTn = Q_TN.create(modLoc("block/quarter_" + path + "_tn"), tex, gen.modelOutput);
                mTf = Q_TF.create(modLoc("block/quarter_" + path + "_tf"), tex, gen.modelOutput);
            }

            MultiPartGenerator multipart = MultiPartGenerator.multiPart(q);
            addQuarterPart(multipart, Direction.Axis.Z, JebbQuarterBlock.BOTTOM_NEAR, mBn, VariantProperties.Rotation.R0);
            addQuarterPart(multipart, Direction.Axis.Z, JebbQuarterBlock.BOTTOM_FAR, mBf, VariantProperties.Rotation.R0);
            addQuarterPart(multipart, Direction.Axis.Z, JebbQuarterBlock.TOP_NEAR, mTn, VariantProperties.Rotation.R0);
            addQuarterPart(multipart, Direction.Axis.Z, JebbQuarterBlock.TOP_FAR, mTf, VariantProperties.Rotation.R0);
            addQuarterPart(multipart, Direction.Axis.X, JebbQuarterBlock.BOTTOM_NEAR, mBn, VariantProperties.Rotation.R90);
            addQuarterPart(multipart, Direction.Axis.X, JebbQuarterBlock.BOTTOM_FAR, mBf, VariantProperties.Rotation.R90);
            addQuarterPart(multipart, Direction.Axis.X, JebbQuarterBlock.TOP_NEAR, mTn, VariantProperties.Rotation.R90);
            addQuarterPart(multipart, Direction.Axis.X, JebbQuarterBlock.TOP_FAR, mTf, VariantProperties.Rotation.R90);
            gen.blockStateOutput.accept(multipart);
        }

        for (var e : JebbBlocks.CORNER_PILLARS.entrySet()) {
            Block parent = e.getKey();
            JebbCornerPillarBlock cp = e.getValue();
            String path = BuiltInRegistries.BLOCK.getKey(parent).getPath();
            JebbTextureMap.Faces faces = JebbTextureMap.forParent(parent);
            ResourceLocation mNw;
            ResourceLocation mNe;
            ResourceLocation mSw;
            ResourceLocation mSe;
            if (faces != null) {
                TextureMapping tex = btsMapping(faces);
                mNw = CP_NW_BTS.create(modLoc("block/corner_pillar_" + path + "_nw"), tex, gen.modelOutput);
                mNe = CP_NE_BTS.create(modLoc("block/corner_pillar_" + path + "_ne"), tex, gen.modelOutput);
                mSw = CP_SW_BTS.create(modLoc("block/corner_pillar_" + path + "_sw"), tex, gen.modelOutput);
                mSe = CP_SE_BTS.create(modLoc("block/corner_pillar_" + path + "_se"), tex, gen.modelOutput);
            } else {
                TextureMapping tex = TextureMapping.cube(parent);
                mNw = CP_NW.create(modLoc("block/corner_pillar_" + path + "_nw"), tex, gen.modelOutput);
                mNe = CP_NE.create(modLoc("block/corner_pillar_" + path + "_ne"), tex, gen.modelOutput);
                mSw = CP_SW.create(modLoc("block/corner_pillar_" + path + "_sw"), tex, gen.modelOutput);
                mSe = CP_SE.create(modLoc("block/corner_pillar_" + path + "_se"), tex, gen.modelOutput);
            }

            MultiPartGenerator multipart = MultiPartGenerator.multiPart(cp);
            multipart.with(Condition.condition().term(JebbCornerPillarBlock.NW, true),
                    Variant.variant().with(VariantProperties.MODEL, mNw));
            multipart.with(Condition.condition().term(JebbCornerPillarBlock.NE, true),
                    Variant.variant().with(VariantProperties.MODEL, mNe));
            multipart.with(Condition.condition().term(JebbCornerPillarBlock.SW, true),
                    Variant.variant().with(VariantProperties.MODEL, mSw));
            multipart.with(Condition.condition().term(JebbCornerPillarBlock.SE, true),
                    Variant.variant().with(VariantProperties.MODEL, mSe));
            gen.blockStateOutput.accept(multipart);
        }
    }

    private static TextureMapping btsMapping(JebbTextureMap.Faces faces) {
        return new TextureMapping()
                .put(TextureSlot.TOP, faces.top())
                .put(TextureSlot.SIDE, faces.side())
                .put(TextureSlot.BOTTOM, faces.bottom())
                .put(TextureSlot.PARTICLE, faces.side());
    }

    private static void addQuarterPart(
            MultiPartGenerator multipart,
            Direction.Axis axis,
            BooleanProperty quad,
            ResourceLocation model,
            VariantProperties.Rotation yRot
    ) {
        Condition when = Condition.and(
                Condition.condition().term(JebbQuarterBlock.AXIS, axis),
                Condition.condition().term(quad, true));
        multipart.with(when, Variant.variant()
                .with(VariantProperties.MODEL, model)
                .with(VariantProperties.Y_ROT, yRot));
    }

    @Override
    public void generateItemModels(ItemModelGenerators gen) {
        for (var e : JebbBlocks.VERTICAL_SLABS.entrySet()) {
            Block parent = e.getKey();
            String path = BuiltInRegistries.BLOCK.getKey(parent).getPath();
            createItemModel(gen, parent, e.getValue().asItem(), "vertical_slab_item_" + path, ITEM_VS, ITEM_VS_BTS);
        }
        for (var e : JebbBlocks.QUARTERS.entrySet()) {
            Block parent = e.getKey();
            String path = BuiltInRegistries.BLOCK.getKey(parent).getPath();
            createItemModel(gen, parent, e.getValue().asItem(), "quarter_item_" + path, ITEM_Q, ITEM_Q_BTS);
        }
        for (var e : JebbBlocks.CORNER_PILLARS.entrySet()) {
            Block parent = e.getKey();
            String path = BuiltInRegistries.BLOCK.getKey(parent).getPath();
            createItemModel(gen, parent, e.getValue().asItem(), "corner_pillar_item_" + path, ITEM_CP, ITEM_CP_BTS);
        }
    }

    private static void createItemModel(
            ItemModelGenerators gen,
            Block parent,
            Item item,
            String modelName,
            ModelTemplate templateAll,
            ModelTemplate templateBts
    ) {
        ResourceLocation itemModel = ModelLocationUtils.getModelLocation(item);
        JebbTextureMap.Faces faces = JebbTextureMap.forParent(parent);
        if (faces != null) {
            templateBts.create(itemModel, btsMapping(faces), gen.output);
        } else {
            templateAll.create(itemModel, TextureMapping.cube(parent), gen.output);
        }
    }
}
