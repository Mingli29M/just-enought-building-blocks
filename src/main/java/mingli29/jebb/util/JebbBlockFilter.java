package mingli29.jebb.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;
import java.util.Set;

public final class JebbBlockFilter {
    private static final Set<String> DENY = Set.of(
            "crafting_table", "cartography_table", "fletching_table", "loom",
            "smithing_table", "grindstone", "stonecutter", "composter", "anvil",
            "chipped_anvil", "damaged_anvil",
            "piston", "sticky_piston", "piston_head", "moving_piston",
            "dispenser", "dropper", "observer", "hopper",
            "tnt",
            "bedrock", "barrier", "command_block", "chain_command_block",
            "repeating_command_block", "structure_block", "structure_void",
            "jigsaw", "spawner", "end_portal", "end_gateway", "nether_portal",
            "light", "budding_amethyst",
            "oak_wood", "spruce_wood", "birch_wood", "jungle_wood", "acacia_wood",
            "dark_oak_wood", "mangrove_wood", "cherry_wood",
            "crimson_hyphae", "warped_hyphae",
            "stripped_oak_wood", "stripped_spruce_wood", "stripped_birch_wood",
            "stripped_jungle_wood", "stripped_acacia_wood", "stripped_dark_oak_wood",
            "stripped_mangrove_wood", "stripped_cherry_wood",
            "stripped_crimson_hyphae", "stripped_warped_hyphae",
            "hay_block", "bone_block", "basalt", "polished_basalt",
            "quartz_pillar", "purpur_pillar",
            "ochre_froglight", "verdant_froglight", "pearlescent_froglight",
            "target", "jukebox", "note_block", "lodestone",
            "sandstone", "red_sandstone", "chiseled_sandstone", "cut_sandstone",
            "chiseled_red_sandstone", "cut_red_sandstone",
            "sculk_catalyst", "respawn_anchor",
            "infested_stone", "infested_cobblestone", "infested_stone_bricks",
            "infested_mossy_stone_bricks", "infested_cracked_stone_bricks",
            "infested_chiseled_stone_bricks", "infested_deepslate",
            "reinforced_deepslate",
            "carved_pumpkin", "jack_o_lantern",
            "sculk_sensor", "calibrated_sculk_sensor",
            "sponge", "wet_sponge",
            "brown_mushroom_block", "red_mushroom_block", "mushroom_stem",
            "melon", "magma_block",
            "snow_block",
            "chorus_flower", "chorus_plant"
    );

    private static final Set<String> ORE_PATHS = Set.of(
            "coal_ore", "deepslate_coal_ore",
            "iron_ore", "deepslate_iron_ore",
            "copper_ore", "deepslate_copper_ore",
            "gold_ore", "deepslate_gold_ore",
            "lapis_ore", "deepslate_lapis_ore",
            "redstone_ore", "deepslate_redstone_ore",
            "diamond_ore", "deepslate_diamond_ore",
            "emerald_ore", "deepslate_emerald_ore",
            "nether_quartz_ore", "nether_gold_ore"
    );

    /**
     * Extra paths for "natural" terrain not always covered by vanilla tags (temporary filter).
     */
    private static final Set<String> TEMP_NATURAL_EXTRA = Set.of(
            "gravel", "clay", "mud", "muddy_mangrove_roots",
            "grass_block", "podzol", "mycelium", "rooted_dirt",
            "soul_sand", "soul_soil",
            "end_stone",
            "sculk",
            "ancient_debris",
            "nether_quartz_ore", "nether_gold_ore",
            "obsidian", "crying_obsidian",
            "prismarine", "prismarine_bricks", "dark_prismarine",
            "sea_lantern", "netherrack", "warped_nylium", "crimson_nylium",
            "moss_block"
    );

    private JebbBlockFilter() {
    }

    /**
     * Temporary: exclude common worldgen / raw terrain blocks so only crafted or decorative
     * building blocks get variants. Uses vanilla {@link BlockTags} plus a small explicit set.
     */
    private static boolean isTemporaryNaturalBlock(Holder<Block> holder) {
        return holder.is(BlockTags.BASE_STONE_OVERWORLD)
                || holder.is(BlockTags.BASE_STONE_NETHER)
                || holder.is(BlockTags.DIRT)
                || holder.is(BlockTags.SAND)
                || holder.is(BlockTags.SNOW)
                || holder.is(BlockTags.ICE)
                || holder.is(BlockTags.TERRACOTTA)
                || holder.is(BlockTags.COAL_ORES)
                || holder.is(BlockTags.IRON_ORES)
                || holder.is(BlockTags.COPPER_ORES)
                || holder.is(BlockTags.GOLD_ORES)
                || holder.is(BlockTags.LAPIS_ORES)
                || holder.is(BlockTags.REDSTONE_ORES)
                || holder.is(BlockTags.DIAMOND_ORES)
                || holder.is(BlockTags.EMERALD_ORES);
    }

    /**
     * Reserved for future opt-outs; logs and stems now receive quarter variants like other parents.
     */
    public static boolean skipsQuarterVariants(Block block) {
        return false;
    }

    public static boolean qualifies(Block block) {
        ResourceLocation key = BuiltInRegistries.BLOCK.getKey(block);
        if (key == null || !"minecraft".equals(key.getNamespace())) return false;
        String path = key.getPath();
        if ("honey_block".equals(path)) return true;
        if ("slime_block".equals(path) || "redstone_block".equals(path) || "redstone_lamp".equals(path)) return false;
        if (DENY.contains(path)) return false;
        if (ORE_PATHS.contains(path)) return false;
        if (path.startsWith("waxed_")) return false;
        if (TEMP_NATURAL_EXTRA.contains(path)) return false;
        Optional<Holder.Reference<Block>> holderOpt = BuiltInRegistries.BLOCK.getResourceKey(block)
                .flatMap(BuiltInRegistries.BLOCK::getHolder);
        if (holderOpt.isPresent() && isTemporaryNaturalBlock(holderOpt.get())) return false;
        if (block instanceof EntityBlock) return false;
        BlockState state = block.defaultBlockState();
        if (state.isAir()) return false;
        if (state.getRenderShape() != RenderShape.MODEL) return false;
        return Block.isShapeFullBlock(state.getCollisionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO));
    }
}
