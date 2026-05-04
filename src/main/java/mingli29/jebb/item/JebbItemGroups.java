package mingli29.jebb.item;

import mingli29.jebb.JustEnoughtBuildingBlocks;
import mingli29.jebb.block.JebbBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class JebbItemGroups {
    public static final int ROW_WIDTH = 9;
    public static final ResourceLocation MAIN_TAB_ID = new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "main");
    public static final ResourceLocation BLOCKS_TAB_ID = new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "blocks");

    public static CreativeModeTab MAIN_TAB;
    public static CreativeModeTab BLOCKS_TAB;

    public static List<Section> MAIN_SECTIONS;
    public static List<Section> BLOCKS_SECTIONS;

    public static final Map<CreativeModeTab, LinkedHashMap<Integer, SectionRowInfo>> SECTION_ROWS_BY_TAB = new LinkedHashMap<>();

    /** How the creative inventory draws the section header row for this section. */
    public enum SectionBannerStyle {
        DEFAULT,
        VARIANT_VERTICAL_SLAB,
        VARIANT_CORNER_PILLAR,
        VARIANT_QUARTER
    }

    public record Section(String labelKey, SectionBannerStyle bannerStyle, Collection<? extends Block> blocks) {
    }

    public record SectionRowInfo(String labelKey, SectionBannerStyle bannerStyle) {
    }

    private JebbItemGroups() {
    }

    public static List<Section> sectionsForTab(CreativeModeTab tab) {
        if (tab == MAIN_TAB) {
            return MAIN_SECTIONS;
        }
        if (tab == BLOCKS_TAB) {
            return BLOCKS_SECTIONS;
        }
        return null;
    }

    public static Map<Integer, SectionRowInfo> sectionRowsForTab(CreativeModeTab tab) {
        return SECTION_ROWS_BY_TAB.getOrDefault(tab, new LinkedHashMap<>());
    }

    public static void register() {
        List<Block> verticalSlabTab = new ArrayList<>(JebbBlocks.VERTICAL_SLABS.values());
        MAIN_SECTIONS = List.of(
                new Section("jebb.section.corner_pillars", SectionBannerStyle.VARIANT_CORNER_PILLAR, JebbBlocks.CORNER_PILLARS.values()),
                new Section("jebb.section.vertical_slabs", SectionBannerStyle.VARIANT_VERTICAL_SLAB, verticalSlabTab),
                new Section("jebb.section.quarters", SectionBannerStyle.VARIANT_QUARTER, JebbBlocks.QUARTERS.values())
        );

        BLOCKS_SECTIONS = List.of(
                new Section("jebb.section.wood_variants", SectionBannerStyle.DEFAULT, List.of(
                        JebbBlocks.OAK_SQUARE_BRICK,
                        JebbBlocks.ACACIA_SQUARE_BRICK,
                        JebbBlocks.BAMBOO_SQUARE_BRICK,
                        JebbBlocks.BIRCH_SQUARE_BRICK,
                        JebbBlocks.CHERRY_SQUARE_BRICK,
                        JebbBlocks.CRIMSON_SQUARE_BRICK,
                        JebbBlocks.DARK_OAK_SQUARE_BRICK,
                        JebbBlocks.JUNGLE_SQUARE_BRICK,
                        JebbBlocks.MANGROVE_SQUARE_BRICK,
                        JebbBlocks.PALE_OAK_SQUARE_BRICK,
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
                        JebbBlocks.CHISELED_PALE_OAK_PLANKS,
                        JebbBlocks.CHISELED_SPRUCE_PLANKS,
                        JebbBlocks.CHISELED_WARPED_PLANKS
                ))
        );

        MAIN_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, MAIN_TAB_ID,
                FabricItemGroup.builder()
                        .title(Component.translatable("itemGroup." + JustEnoughtBuildingBlocks.MOD_ID + ".main"))
                        .icon(() -> new ItemStack(Items.OAK_PLANKS))
                        .displayItems((params, output) -> {
                            Set<Block> seen = new LinkedHashSet<>();
                            for (Section s : MAIN_SECTIONS) {
                                for (Block b : s.blocks()) {
                                    if (seen.add(b)) {
                                        output.accept(b);
                                    }
                                }
                            }
                        })
                        .build());

        BLOCKS_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, BLOCKS_TAB_ID,
                FabricItemGroup.builder()
                        .title(Component.translatable("itemGroup." + JustEnoughtBuildingBlocks.MOD_ID + ".blocks"))
                        .icon(() -> new ItemStack(JebbBlocks.OAK_SQUARE_BRICK))
                        .displayItems((params, output) -> {
                            Set<Block> seen = new LinkedHashSet<>();
                            for (Section s : BLOCKS_SECTIONS) {
                                for (Block b : s.blocks()) {
                                    if (seen.add(b)) {
                                        output.accept(b);
                                    }
                                }
                            }
                        })
                        .build());
    }
}
