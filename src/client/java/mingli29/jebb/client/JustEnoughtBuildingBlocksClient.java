package mingli29.jebb.client;

import mingli29.jebb.block.JebbBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public class JustEnoughtBuildingBlocksClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        applyParentRenderLayers(JebbBlocks.VERTICAL_SLABS);
        applyParentRenderLayers(JebbBlocks.QUARTERS);
        applyParentRenderLayers(JebbBlocks.CORNER_PILLARS);
    }

    private static void applyParentRenderLayers(Map<Block, ? extends Block> map) {
        for (Map.Entry<Block, ? extends Block> e : map.entrySet()) {
            RenderType layer = ItemBlockRenderTypes.getChunkRenderType(e.getKey().defaultBlockState());
            if (layer != RenderType.solid()) {
                BlockRenderLayerMap.INSTANCE.putBlock(e.getValue(), layer);
            }
        }
    }
}
