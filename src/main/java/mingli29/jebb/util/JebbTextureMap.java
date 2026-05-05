package mingli29.jebb.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.Map;

/**
 * Top/side/bottom texture mappings for vanilla parents whose default block model is not
 * {@code cube_all}. Falls back to {@link #defaultMapping(Block)} (single texture for all faces)
 * for parents not present in the registry.
 */
public final class JebbTextureMap {
    public record Faces(ResourceLocation top, ResourceLocation side, ResourceLocation bottom) {
    }

    private static ResourceLocation tex(String name) {
        return new ResourceLocation("minecraft", "block/" + name);
    }

    private static ResourceLocation modTex(String name) {
        return new ResourceLocation("just-enought-building-blocks", "block/" + name);
    }

    private static final Map<String, Faces> BY_PATH = Map.ofEntries(
            // cube_bottom_top family
            Map.entry("grass_block",      new Faces(tex("grass_block_top"),      tex("grass_block_side"),      tex("dirt"))),
            Map.entry("podzol",           new Faces(tex("podzol_top"),           tex("podzol_side"),           tex("dirt"))),
            Map.entry("mycelium",         new Faces(tex("mycelium_top"),         tex("mycelium_side"),         tex("dirt"))),
            Map.entry("rooted_dirt",      new Faces(tex("rooted_dirt_top"),      tex("rooted_dirt_side"),      tex("dirt"))),
            Map.entry("crimson_nylium",   new Faces(tex("crimson_nylium"),       tex("crimson_nylium_side"),   tex("netherrack"))),
            Map.entry("warped_nylium",    new Faces(tex("warped_nylium"),        tex("warped_nylium_side"),    tex("netherrack"))),
            Map.entry("pumpkin",          new Faces(tex("pumpkin_top"),          tex("pumpkin_side"),          tex("pumpkin_top"))),
            Map.entry("melon",            new Faces(tex("melon_top"),            tex("melon_side"),            tex("melon_top"))),
            Map.entry("honey_block",      new Faces(tex("honey_block_top"),      tex("honey_block_side"),      tex("honey_block_bottom"))),
            Map.entry("frosted_ice",      new Faces(tex("frosted_ice_0"),        tex("frosted_ice_0"),         tex("frosted_ice_0"))),
            // cube_column family (top == bottom)
            Map.entry("quartz_block",     new Faces(tex("quartz_block_top"),     tex("quartz_block_side"),     tex("quartz_block_bottom"))),
            Map.entry("bookshelf",        new Faces(tex("oak_planks"),           tex("bookshelf"),             tex("oak_planks"))),
            Map.entry("dried_kelp_block", new Faces(tex("dried_kelp_top"),       tex("dried_kelp_side"),       tex("dried_kelp_bottom"))),
            Map.entry("mangrove_roots",   new Faces(tex("mangrove_roots_top"),   tex("mangrove_roots_side"),   tex("mangrove_roots_top"))),
            Map.entry("muddy_mangrove_roots", new Faces(tex("muddy_mangrove_roots_top"), tex("muddy_mangrove_roots_side"), tex("muddy_mangrove_roots_top"))),
            // Smooth variants reuse another block's texture in vanilla
            Map.entry("smooth_quartz",        new Faces(tex("quartz_block_bottom"),     tex("quartz_block_bottom"),       tex("quartz_block_bottom"))),
            Map.entry("smooth_sandstone",     new Faces(tex("sandstone_top"),           tex("sandstone_top"),             tex("sandstone_top"))),
            Map.entry("smooth_red_sandstone", new Faces(tex("red_sandstone_top"),       tex("red_sandstone_top"),         tex("red_sandstone_top"))),
            // Mod wood-style parents use their supplied mod textures for all generated variant shapes.
            Map.entry("oak_triangle_block",       allMod("oak_triangle_block")),
            Map.entry("acacia_triangle_block",    allMod("acacia_triangle_block")),
            Map.entry("bamboo_triangle_block",    allMod("bamboo_triangle_block")),
            Map.entry("birch_triangle_block",     allMod("birch_triangle_block")),
            Map.entry("cheery_triangle_block",    allMod("cheery_triangle_block")),
            Map.entry("crimson_triangle_block",   allMod("crimson_triangle_block")),
            Map.entry("dark_oak_triangle_block",  allMod("dark_oak_triangle_block")),
            Map.entry("jungle_triangle_block",    allMod("jungle_triangle_block")),
            Map.entry("mangrove_triangle_block",  allMod("mangrove_triangle_block")),
            Map.entry("spruce_triangle_block",    allMod("spruce_triangle_block")),
            Map.entry("warped_triangle_block",    allMod("warped_triangle_block")),
            Map.entry("striped_oak",              allMod("striped_oak")),
            Map.entry("striped_dark_oak",         allMod("striped_dark_oak")),
            Map.entry("striped_bamboo",           allMod("striped_bamboo")),
            Map.entry("striped_acacia_plank",     allMod("striped_acacia_plank")),
            Map.entry("striped_cheery_plank",     allMod("striped_cheery_plank")),
            Map.entry("striped_crimson_plank",    allMod("striped_crimson_plank")),
            Map.entry("striped_jungle_plank",     allMod("striped_jungle_plank")),
            Map.entry("striped_mangrove_plank",   allMod("striped_mangrove_plank")),
            Map.entry("striped_spruce_plank",     allMod("striped_spruce_plank")),
            Map.entry("striped_warped_plank",     allMod("striped_warped_plank")),
            Map.entry("striped_triangle_block",   allMod("striped_triangle_block"))
    );

    private JebbTextureMap() {
    }

    private static Faces all(String texture) {
        ResourceLocation tex = tex(texture);
        return new Faces(tex, tex, tex);
    }

    private static Faces allMod(String texture) {
        ResourceLocation tex = modTex(texture);
        return new Faces(tex, tex, tex);
    }

    public static Faces forParent(Block parent) {
        ResourceLocation key = BuiltInRegistries.BLOCK.getKey(parent);
        if (key == null) {
            return null;
        }
        String path = key.getPath();
        Faces fromMap = BY_PATH.get(path);
        if (fromMap != null) {
            return fromMap;
        }
        return columnTopSideBottomFaces(path);
    }

    /**
     * Logs, stems, and bamboo columns use distinct top/end textures (same layout as bookshelf-style BTS models).
     */
    private static Faces columnTopSideBottomFaces(String path) {
        if (!(path.endsWith("_log") || path.endsWith("_stem")
                || "bamboo_block".equals(path) || "stripped_bamboo_block".equals(path))) {
            return null;
        }
        ResourceLocation side = tex(path);
        ResourceLocation top = tex(path + "_top");
        return new Faces(top, side, top);
    }

    public static Faces defaultMapping(Block parent) {
        ResourceLocation key = BuiltInRegistries.BLOCK.getKey(parent);
        ResourceLocation t = key != null
                ? new ResourceLocation(key.getNamespace(), "block/" + key.getPath())
                : tex("stone");
        return new Faces(t, t, t);
    }
}
