package mingli29.jebb.client.datagen;

import mingli29.jebb.block.JebbBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class JebbBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    private static final List<TagKey<Block>> MIRROR_TAGS = List.of(
            BlockTags.MINEABLE_WITH_PICKAXE,
            BlockTags.MINEABLE_WITH_AXE,
            BlockTags.MINEABLE_WITH_SHOVEL,
            BlockTags.MINEABLE_WITH_HOE,
            BlockTags.NEEDS_DIAMOND_TOOL,
            BlockTags.NEEDS_IRON_TOOL,
            BlockTags.NEEDS_STONE_TOOL
    );

    public JebbBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        HolderGetter<Block> blocks = provider.lookupOrThrow(Registries.BLOCK);
        for (var e : JebbBlocks.VERTICAL_SLABS.entrySet()) {
            Block parent = e.getKey();
            Block vs = e.getValue();
            Block q = JebbBlocks.QUARTERS.get(parent);
            Block cp = JebbBlocks.CORNER_PILLARS.get(parent);
            Block tagSource = parent == JebbBlocks.OAK_MUZHUAN ? Blocks.OAK_PLANKS : parent;
            ResourceKey<Block> parentKey = BuiltInRegistries.BLOCK.getResourceKey(tagSource).orElseThrow();
            var parentHolder = blocks.getOrThrow(parentKey);
            for (TagKey<Block> tag : MIRROR_TAGS) {
                if (parentHolder.is(tag)) {
                    var builder = this.getOrCreateTagBuilder(tag).add(vs, q);
                    if (cp != null) builder.add(cp);
                }
            }
        }
    }
}
