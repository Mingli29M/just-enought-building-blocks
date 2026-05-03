package mingli29.jebb.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

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
            "oak_log", "spruce_log", "birch_log", "jungle_log", "acacia_log",
            "dark_oak_log", "mangrove_log", "cherry_log", "bamboo_block",
            "crimson_stem", "warped_stem",
            "oak_wood", "spruce_wood", "birch_wood", "jungle_wood", "acacia_wood",
            "dark_oak_wood", "mangrove_wood", "cherry_wood",
            "crimson_hyphae", "warped_hyphae",
            "stripped_oak_log", "stripped_spruce_log", "stripped_birch_log",
            "stripped_jungle_log", "stripped_acacia_log", "stripped_dark_oak_log",
            "stripped_mangrove_log", "stripped_cherry_log", "stripped_bamboo_block",
            "stripped_crimson_stem", "stripped_warped_stem",
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
            "sculk_sensor", "calibrated_sculk_sensor"
    );

    private JebbBlockFilter() {
    }

    public static boolean qualifies(Block block) {
        ResourceLocation key = BuiltInRegistries.BLOCK.getKey(block);
        if (key == null || !"minecraft".equals(key.getNamespace())) return false;
        if (DENY.contains(key.getPath())) return false;
        if (block instanceof EntityBlock) return false;
        BlockState state = block.defaultBlockState();
        if (state.isAir()) return false;
        if (state.getRenderShape() != RenderShape.MODEL) return false;
        return Block.isShapeFullBlock(state.getCollisionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO));
    }
}
