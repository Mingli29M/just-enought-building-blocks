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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

public final class JebbItemGroups {
    public static final int ROW_WIDTH = 9;
    public static final ResourceLocation MAIN_TAB_ID = new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "main");

    public static CreativeModeTab MAIN_TAB;

    public static List<Section> SECTIONS;

    public static final LinkedHashMap<Integer, String> SECTION_ROW_LABELS = new LinkedHashMap<>();

    public record Section(String labelKey, Collection<? extends Block> blocks) {
    }

    private JebbItemGroups() {
    }

    public static void register() {
        SECTIONS = List.of(
                new Section("jebb.section.vertical_slabs", JebbBlocks.VERTICAL_SLABS.values()),
                new Section("jebb.section.quarters", JebbBlocks.QUARTERS.values()),
                new Section("jebb.section.corner_pillars", JebbBlocks.CORNER_PILLARS.values())
        );

        MAIN_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, MAIN_TAB_ID,
                FabricItemGroup.builder()
                        .title(Component.translatable("itemGroup." + JustEnoughtBuildingBlocks.MOD_ID + ".main"))
                        .icon(() -> new ItemStack(Items.OAK_PLANKS))
                        .displayItems((params, output) -> {
                            for (Section s : SECTIONS) {
                                for (Block b : s.blocks()) {
                                    output.accept(b);
                                }
                            }
                        })
                        .build());
    }
}
