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
            Block hs = JebbBlocks.HORIZONTAL_SLABS.get(parent);
            Block tagSource = tagSourceForParent(parent);
            ResourceKey<Block> parentKey = BuiltInRegistries.BLOCK.getResourceKey(tagSource).orElseThrow();
            var parentHolder = blocks.getOrThrow(parentKey);

            List<Block> targets = variantTargets(parent, vs, q, cp, hs);
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

        addWoodVariantAxeTags();
    }

    private void addWoodVariantAxeTags() {
        var builder = this.getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_AXE);
        for (Block parent : woodVariantParents()) {
            builder.add(parent);
            Block vs = JebbBlocks.VERTICAL_SLABS.get(parent);
            if (vs != null) {
                builder.add(vs);
            }
            Block q = JebbBlocks.QUARTERS.get(parent);
            if (q != null) {
                builder.add(q);
            }
            Block cp = JebbBlocks.CORNER_PILLARS.get(parent);
            if (cp != null) {
                builder.add(cp);
            }
            Block hs = JebbBlocks.HORIZONTAL_SLABS.get(parent);
            if (hs != null) {
                builder.add(hs);
            }
        }
    }

    private List<Block> woodVariantParents() {
        return List.of(
                JebbBlocks.OAK_SQUARE_BRICK,
                JebbBlocks.ACACIA_SQUARE_BRICK,
                JebbBlocks.BAMBOO_SQUARE_BRICK,
                JebbBlocks.BIRCH_SQUARE_BRICK,
                JebbBlocks.CHERRY_SQUARE_BRICK,
                JebbBlocks.CRIMSON_SQUARE_BRICK,
                JebbBlocks.DARK_OAK_SQUARE_BRICK,
                JebbBlocks.JUNGLE_SQUARE_BRICK,
                JebbBlocks.MANGROVE_SQUARE_BRICK,
                JebbBlocks.SPRUCE_SQUARE_BRICK,
                JebbBlocks.WARPED_SQUARE_BRICK,
                JebbBlocks.CHISELED_ACACIA_PLANKS,
                JebbBlocks.CHISELED_BAMBOO_PLANKS,
                JebbBlocks.CHISELED_BIRCH_PLANKS,
                JebbBlocks.CHISELED_CHERRY_PLANKS,
                JebbBlocks.CHISELED_CRIMSON_PLANKS,
                JebbBlocks.CHISELED_DARK_OAK_PLANKS,
                JebbBlocks.CHISELED_JUNGLE_PLANKS,
                JebbBlocks.CHISELED_MANGROVE_PLANKS,
                JebbBlocks.CHISELED_OAK_PLANKS,
                JebbBlocks.CHISELED_SPRUCE_PLANKS,
                JebbBlocks.CHISELED_WARPED_PLANKS,
                JebbBlocks.OAK_TRIANGLE_BLOCK,
                JebbBlocks.ACACIA_TRIANGLE_BLOCK,
                JebbBlocks.BAMBOO_TRIANGLE_BLOCK,
                JebbBlocks.BIRCH_TRIANGLE_BLOCK,
                JebbBlocks.CHEERY_TRIANGLE_BLOCK,
                JebbBlocks.CRIMSON_TRIANGLE_BLOCK,
                JebbBlocks.DARK_OAK_TRIANGLE_BLOCK,
                JebbBlocks.JUNGLE_TRIANGLE_BLOCK,
                JebbBlocks.MANGROVE_TRIANGLE_BLOCK,
                JebbBlocks.SPRUCE_TRIANGLE_BLOCK,
                JebbBlocks.WARPED_TRIANGLE_BLOCK,
                JebbBlocks.STRIPED_OAK,
                JebbBlocks.STRIPED_DARK_OAK,
                JebbBlocks.STRIPED_BAMBOO,
                JebbBlocks.STRIPED_ACACIA_PLANK,
                JebbBlocks.STRIPED_CHEERY_PLANK,
                JebbBlocks.STRIPED_CRIMSON_PLANK,
                JebbBlocks.STRIPED_JUNGLE_PLANK,
                JebbBlocks.STRIPED_MANGROVE_PLANK,
                JebbBlocks.STRIPED_SPRUCE_PLANK,
                JebbBlocks.STRIPED_WARPED_PLANK,
                JebbBlocks.STRIPED_TRIANGLE_BLOCK
        );
    }

    /**
     * Variant blocks plus our mod-owned parent (e.g. framed oak), so they receive the same mineable/needs_* tags as the vanilla template.
     */
    private static List<Block> variantTargets(Block parent, Block vs, Block q, Block cp, Block hs) {
        List<Block> out = new ArrayList<>(5);
        out.add(vs);
        if (q != null) {
            out.add(q);
        }
        if (cp != null) {
            out.add(cp);
        }
        if (hs != null) {
            out.add(hs);
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
        if (parent == JebbBlocks.OAK_SQUARE_BRICK || parent == JebbBlocks.CHISELED_OAK_PLANKS
                || parent == JebbBlocks.OAK_TRIANGLE_BLOCK || parent == JebbBlocks.STRIPED_OAK
                || parent == JebbBlocks.STRIPED_TRIANGLE_BLOCK) {
            return Blocks.OAK_PLANKS;
        }
        if (parent == JebbBlocks.ACACIA_SQUARE_BRICK || parent == JebbBlocks.CHISELED_ACACIA_PLANKS
                || parent == JebbBlocks.ACACIA_TRIANGLE_BLOCK || parent == JebbBlocks.STRIPED_ACACIA_PLANK) {
            return Blocks.ACACIA_PLANKS;
        }
        if (parent == JebbBlocks.BAMBOO_SQUARE_BRICK || parent == JebbBlocks.CHISELED_BAMBOO_PLANKS
                || parent == JebbBlocks.BAMBOO_TRIANGLE_BLOCK || parent == JebbBlocks.STRIPED_BAMBOO) {
            return Blocks.BAMBOO_PLANKS;
        }
        if (parent == JebbBlocks.BIRCH_SQUARE_BRICK || parent == JebbBlocks.CHISELED_BIRCH_PLANKS
                || parent == JebbBlocks.BIRCH_TRIANGLE_BLOCK) {
            return Blocks.BIRCH_PLANKS;
        }
        if (parent == JebbBlocks.CHERRY_SQUARE_BRICK || parent == JebbBlocks.CHISELED_CHERRY_PLANKS
                || parent == JebbBlocks.CHEERY_TRIANGLE_BLOCK || parent == JebbBlocks.STRIPED_CHEERY_PLANK) {
            return Blocks.CHERRY_PLANKS;
        }
        if (parent == JebbBlocks.CRIMSON_SQUARE_BRICK || parent == JebbBlocks.CHISELED_CRIMSON_PLANKS
                || parent == JebbBlocks.CRIMSON_TRIANGLE_BLOCK || parent == JebbBlocks.STRIPED_CRIMSON_PLANK) {
            return Blocks.CRIMSON_PLANKS;
        }
        if (parent == JebbBlocks.DARK_OAK_SQUARE_BRICK || parent == JebbBlocks.CHISELED_DARK_OAK_PLANKS
                || parent == JebbBlocks.DARK_OAK_TRIANGLE_BLOCK || parent == JebbBlocks.STRIPED_DARK_OAK) {
            return Blocks.DARK_OAK_PLANKS;
        }
        if (parent == JebbBlocks.JUNGLE_SQUARE_BRICK || parent == JebbBlocks.CHISELED_JUNGLE_PLANKS
                || parent == JebbBlocks.JUNGLE_TRIANGLE_BLOCK || parent == JebbBlocks.STRIPED_JUNGLE_PLANK) {
            return Blocks.JUNGLE_PLANKS;
        }
        if (parent == JebbBlocks.MANGROVE_SQUARE_BRICK || parent == JebbBlocks.CHISELED_MANGROVE_PLANKS
                || parent == JebbBlocks.MANGROVE_TRIANGLE_BLOCK || parent == JebbBlocks.STRIPED_MANGROVE_PLANK) {
            return Blocks.MANGROVE_PLANKS;
        }
        if (parent == JebbBlocks.SPRUCE_SQUARE_BRICK || parent == JebbBlocks.CHISELED_SPRUCE_PLANKS
                || parent == JebbBlocks.SPRUCE_TRIANGLE_BLOCK || parent == JebbBlocks.STRIPED_SPRUCE_PLANK) {
            return Blocks.SPRUCE_PLANKS;
        }
        if (parent == JebbBlocks.WARPED_SQUARE_BRICK || parent == JebbBlocks.CHISELED_WARPED_PLANKS
                || parent == JebbBlocks.WARPED_TRIANGLE_BLOCK || parent == JebbBlocks.STRIPED_WARPED_PLANK) {
            return Blocks.WARPED_PLANKS;
        }
        return parent;
    }
}
