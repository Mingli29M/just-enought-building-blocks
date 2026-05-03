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
            // cube_column family (top == bottom)
            Map.entry("quartz_block",     new Faces(tex("quartz_block_top"),     tex("quartz_block_side"),     tex("quartz_block_bottom"))),
            Map.entry("bookshelf",        new Faces(tex("bookshelf_top"),        tex("bookshelf"),             tex("bookshelf_top"))),
            Map.entry("dried_kelp_block", new Faces(tex("dried_kelp_top"),       tex("dried_kelp_side"),       tex("dried_kelp_bottom"))),
            Map.entry("mangrove_roots",   new Faces(tex("mangrove_roots_top"),   tex("mangrove_roots_side"),   tex("mangrove_roots_top"))),
            Map.entry("muddy_mangrove_roots", new Faces(tex("muddy_mangrove_roots_top"), tex("muddy_mangrove_roots_side"), tex("muddy_mangrove_roots_top")))
    );

    private JebbTextureMap() {
    }

    public static Faces forParent(Block parent) {
        ResourceLocation key = BuiltInRegistries.BLOCK.getKey(parent);
        if (key == null) return null;
        return BY_PATH.get(key.getPath());
    }

    public static Faces defaultMapping(Block parent) {
        ResourceLocation key = BuiltInRegistries.BLOCK.getKey(parent);
        ResourceLocation t = key != null
                ? new ResourceLocation(key.getNamespace(), "block/" + key.getPath())
                : tex("stone");
        return new Faces(t, t, t);
    }
}
