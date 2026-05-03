package mingli29.jebb.client.datagen;

import mingli29.jebb.JustEnoughtBuildingBlocks;
import mingli29.jebb.block.JebbBlocks;
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
import net.minecraft.data.models.model.DelegatedModel;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.Optional;

public class JebbModelProvider extends FabricModelProvider {
    private static final ResourceLocation TEMPLATE_VS = new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "block/template_vertical_slab");
    private static final ResourceLocation TEMPLATE_Q_BN = new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "block/template_quarter_bn");
    private static final ResourceLocation TEMPLATE_Q_BF = new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "block/template_quarter_bf");
    private static final ResourceLocation TEMPLATE_Q_TN = new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "block/template_quarter_tn");
    private static final ResourceLocation TEMPLATE_Q_TF = new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "block/template_quarter_tf");

    private static final ResourceLocation TEMPLATE_VS_BTS = new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "block/template_vertical_slab_bts");
    private static final ResourceLocation TEMPLATE_Q_BN_BTS = new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "block/template_quarter_bn_bts");
    private static final ResourceLocation TEMPLATE_Q_BF_BTS = new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "block/template_quarter_bf_bts");
    private static final ResourceLocation TEMPLATE_Q_TN_BTS = new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "block/template_quarter_tn_bts");
    private static final ResourceLocation TEMPLATE_Q_TF_BTS = new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "block/template_quarter_tf_bts");

    private static final ModelTemplate VS_CHILD = new ModelTemplate(Optional.of(TEMPLATE_VS), Optional.empty(), TextureSlot.ALL);
    private static final ModelTemplate Q_BN = new ModelTemplate(Optional.of(TEMPLATE_Q_BN), Optional.empty(), TextureSlot.ALL);
    private static final ModelTemplate Q_BF = new ModelTemplate(Optional.of(TEMPLATE_Q_BF), Optional.empty(), TextureSlot.ALL);
    private static final ModelTemplate Q_TN = new ModelTemplate(Optional.of(TEMPLATE_Q_TN), Optional.empty(), TextureSlot.ALL);
    private static final ModelTemplate Q_TF = new ModelTemplate(Optional.of(TEMPLATE_Q_TF), Optional.empty(), TextureSlot.ALL);

    private static final ModelTemplate VS_BTS = new ModelTemplate(Optional.of(TEMPLATE_VS_BTS), Optional.empty(),
            TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.BOTTOM);
    private static final ModelTemplate Q_BN_BTS = new ModelTemplate(Optional.of(TEMPLATE_Q_BN_BTS), Optional.empty(),
            TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.BOTTOM);
    private static final ModelTemplate Q_BF_BTS = new ModelTemplate(Optional.of(TEMPLATE_Q_BF_BTS), Optional.empty(),
            TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.BOTTOM);
    private static final ModelTemplate Q_TN_BTS = new ModelTemplate(Optional.of(TEMPLATE_Q_TN_BTS), Optional.empty(),
            TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.BOTTOM);
    private static final ModelTemplate Q_TF_BTS = new ModelTemplate(Optional.of(TEMPLATE_Q_TF_BTS), Optional.empty(),
            TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.BOTTOM);

    public JebbModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators gen) {
        for (var e : JebbBlocks.VERTICAL_SLABS.entrySet()) {
            Block parent = e.getKey();
            JebbVerticalSlabBlock vs = e.getValue();
            String path = BuiltInRegistries.BLOCK.getKey(parent).getPath();
            ResourceLocation slabModel = new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "block/vertical_slab_" + path);
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
                mBn = Q_BN_BTS.create(new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "block/quarter_" + path + "_bn"), tex, gen.modelOutput);
                mBf = Q_BF_BTS.create(new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "block/quarter_" + path + "_bf"), tex, gen.modelOutput);
                mTn = Q_TN_BTS.create(new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "block/quarter_" + path + "_tn"), tex, gen.modelOutput);
                mTf = Q_TF_BTS.create(new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "block/quarter_" + path + "_tf"), tex, gen.modelOutput);
            } else {
                TextureMapping tex = TextureMapping.cube(parent);
                mBn = Q_BN.create(new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "block/quarter_" + path + "_bn"), tex, gen.modelOutput);
                mBf = Q_BF.create(new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "block/quarter_" + path + "_bf"), tex, gen.modelOutput);
                mTn = Q_TN.create(new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "block/quarter_" + path + "_tn"), tex, gen.modelOutput);
                mTf = Q_TF.create(new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "block/quarter_" + path + "_tf"), tex, gen.modelOutput);
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
            JebbVerticalSlabBlock vs = e.getValue();
            String path = BuiltInRegistries.BLOCK.getKey(parent).getPath();
            ResourceLocation blockModel = new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "block/vertical_slab_" + path);
            gen.output.accept(ModelLocationUtils.getModelLocation(vs.asItem()), new DelegatedModel(blockModel));
        }
        for (var e : JebbBlocks.QUARTERS.entrySet()) {
            Block parent = e.getKey();
            JebbQuarterBlock q = e.getValue();
            String path = BuiltInRegistries.BLOCK.getKey(parent).getPath();
            ResourceLocation blockModel = new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "block/quarter_" + path + "_bn");
            gen.output.accept(ModelLocationUtils.getModelLocation(q.asItem()), new DelegatedModel(blockModel));
        }
    }
}
