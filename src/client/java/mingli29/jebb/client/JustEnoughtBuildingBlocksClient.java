package mingli29.jebb.client;

import mingli29.jebb.block.JebbBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;

import java.util.Map;

public class JustEnoughtBuildingBlocksClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        applyParentClientRendering(JebbBlocks.VERTICAL_SLABS);
        applyParentClientRendering(JebbBlocks.QUARTERS);
        applyParentClientRendering(JebbBlocks.CORNER_PILLARS);
        applyParentClientRendering(JebbBlocks.HORIZONTAL_SLABS);
    }

    private static void applyParentClientRendering(Map<Block, ? extends Block> map) {
        for (Map.Entry<Block, ? extends Block> e : map.entrySet()) {
            Block parent = e.getKey();
            Block variant = e.getValue();
            RenderType layer = parent instanceof LeavesBlock
                    ? RenderType.cutoutMipped()
                    : ItemBlockRenderTypes.getChunkRenderType(parent.defaultBlockState());
            if (layer != RenderType.solid()) {
                BlockRenderLayerMap.INSTANCE.putBlock(variant, layer);
            }
            ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) ->
                    Minecraft.getInstance().getBlockColors().getColor(parent.defaultBlockState(), view, pos, tintIndex), variant);
            ColorProviderRegistry.ITEM.register((stack, tintIndex) ->
                    Minecraft.getInstance().getBlockColors().getColor(parent.defaultBlockState(), null, null, tintIndex), variant);
        }
    }
}
