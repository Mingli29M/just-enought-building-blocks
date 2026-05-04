package mingli29.jebb.client.datagen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mingli29.jebb.JustEnoughtBuildingBlocks;
import mingli29.jebb.block.JebbBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class JebbBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    private static final List<TagKey<Block>> MIRROR_TAGS = List.of(
            BlockTags.MINEABLE_WITH_PICKAXE,
            BlockTags.MINEABLE_WITH_AXE,
            BlockTags.MINEABLE_WITH_SHOVEL,
            BlockTags.MINEABLE_WITH_HOE,
            BlockTags.NEEDS_DIAMOND_TOOL,
            BlockTags.NEEDS_IRON_TOOL,
            BlockTags.NEEDS_STONE_TOOL
    );
    private static final Map<ResourceLocation, List<String>> VANILLA_BLOCK_TAG_CACHE = new HashMap<>();

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
            Block tagSource = tagSourceForParent(parent);
            ResourceKey<Block> parentKey = BuiltInRegistries.BLOCK.getResourceKey(tagSource).orElseThrow();
            var parentHolder = blocks.getOrThrow(parentKey);

            List<Block> targets = variantTargets(parent, vs, q, cp);
            ResourceLocation sourceId = BuiltInRegistries.BLOCK.getKey(tagSource);

            for (TagKey<Block> tag : MIRROR_TAGS) {
                if (parentHolder.is(tag) || vanillaTagContains(tag.location(), sourceId, new HashSet<>())) {
                    var builder = this.getOrCreateTagBuilder(tag);
                    for (Block b : targets) {
                        builder.add(b);
                    }
                }
            }
        }
    }

    /**
     * Variant blocks plus our mod-owned parent (e.g. framed oak), so they receive the same mineable/needs_* tags as the vanilla template.
     */
    private static List<Block> variantTargets(Block parent, Block vs, Block q, Block cp) {
        List<Block> out = new ArrayList<>(4);
        out.add(vs);
        if (q != null) {
            out.add(q);
        }
        if (cp != null) {
            out.add(cp);
        }
        if (isOurModBlock(parent)) {
            out.add(parent);
        }
        return out;
    }

    private static boolean isOurModBlock(Block block) {
        return JustEnoughtBuildingBlocks.MOD_ID.equals(BuiltInRegistries.BLOCK.getKey(block).getNamespace());
    }

    private static boolean vanillaTagContains(ResourceLocation tagId, ResourceLocation blockId, Set<ResourceLocation> seen) {
        if (!seen.add(tagId)) {
            return false;
        }

        for (String entry : vanillaBlockTagValues(tagId)) {
            if (entry.startsWith("#")) {
                ResourceLocation nested = new ResourceLocation(entry.substring(1));
                if (vanillaTagContains(nested, blockId, seen)) {
                    return true;
                }
            } else if (entry.equals(blockId.toString())) {
                return true;
            }
        }
        return false;
    }

    private static List<String> vanillaBlockTagValues(ResourceLocation tagId) {
        return VANILLA_BLOCK_TAG_CACHE.computeIfAbsent(tagId, id -> {
            String resourcePath = "data/" + id.getNamespace() + "/tags/blocks/" + id.getPath() + ".json";
            try (InputStream in = JebbBlockTagProvider.class.getClassLoader().getResourceAsStream(resourcePath)) {
                if (in == null) {
                    return List.of();
                }
                JsonObject root = JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
                List<String> values = new ArrayList<>();
                for (JsonElement value : root.getAsJsonArray("values")) {
                    if (value.isJsonPrimitive()) {
                        values.add(value.getAsString());
                    } else if (value.isJsonObject()) {
                        values.add(value.getAsJsonObject().get("id").getAsString());
                    }
                }
                return values;
            } catch (Exception e) {
                throw new IllegalStateException("Failed to read vanilla block tag " + id, e);
            }
        });
    }

    private static Block tagSourceForParent(Block parent) {
        if (parent == JebbBlocks.OAK_SQUARE_BRICK || parent == JebbBlocks.CHISELED_OAK_PLANKS) {
            return Blocks.OAK_PLANKS;
        }
        if (parent == JebbBlocks.ACACIA_SQUARE_BRICK || parent == JebbBlocks.CHISELED_ACACIA_PLANKS) {
            return Blocks.ACACIA_PLANKS;
        }
        if (parent == JebbBlocks.BAMBOO_SQUARE_BRICK || parent == JebbBlocks.CHISELED_BAMBOO_PLANKS) {
            return Blocks.BAMBOO_PLANKS;
        }
        if (parent == JebbBlocks.BIRCH_SQUARE_BRICK || parent == JebbBlocks.CHISELED_BIRCH_PLANKS) {
            return Blocks.BIRCH_PLANKS;
        }
        if (parent == JebbBlocks.CHERRY_SQUARE_BRICK || parent == JebbBlocks.CHISELED_CHERRY_PLANKS) {
            return Blocks.CHERRY_PLANKS;
        }
        if (parent == JebbBlocks.CRIMSON_SQUARE_BRICK || parent == JebbBlocks.CHISELED_CRIMSON_PLANKS) {
            return Blocks.CRIMSON_PLANKS;
        }
        if (parent == JebbBlocks.DARK_OAK_SQUARE_BRICK || parent == JebbBlocks.CHISELED_DARK_OAK_PLANKS) {
            return Blocks.DARK_OAK_PLANKS;
        }
        if (parent == JebbBlocks.JUNGLE_SQUARE_BRICK || parent == JebbBlocks.CHISELED_JUNGLE_PLANKS) {
            return Blocks.JUNGLE_PLANKS;
        }
        if (parent == JebbBlocks.MANGROVE_SQUARE_BRICK || parent == JebbBlocks.CHISELED_MANGROVE_PLANKS) {
            return Blocks.MANGROVE_PLANKS;
        }
        if (parent == JebbBlocks.SPRUCE_SQUARE_BRICK || parent == JebbBlocks.CHISELED_SPRUCE_PLANKS) {
            return Blocks.SPRUCE_PLANKS;
        }
        if (parent == JebbBlocks.WARPED_SQUARE_BRICK || parent == JebbBlocks.CHISELED_WARPED_PLANKS) {
            return Blocks.WARPED_PLANKS;
        }
        if (parent == JebbBlocks.PALE_OAK_SQUARE_BRICK || parent == JebbBlocks.CHISELED_PALE_OAK_PLANKS) {
            return Blocks.OAK_PLANKS;
        }
        return parent;
    }
}
