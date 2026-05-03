package mingli29.jebb.item;

import mingli29.jebb.JustEnoughtBuildingBlocks;
import mingli29.jebb.block.JebbBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

public final class JebbItemGroups {
    public static final int BANNER_WIDTH = 9;
    public static final ResourceLocation MAIN_TAB_ID = new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "main");

    public static CreativeModeTab MAIN_TAB;

    public static JebbSeparatorItem[] BANNER_VERTICAL_SLABS;
    public static JebbSeparatorItem[] BANNER_QUARTERS;
    public static JebbSeparatorItem[] BANNER_CORNER_PILLARS;

    private JebbItemGroups() {
    }

    public static void register() {
        BANNER_VERTICAL_SLABS = registerBanner("divider_vertical_slabs", "jebb.section.vertical_slabs");
        BANNER_QUARTERS = registerBanner("divider_quarters", "jebb.section.quarters");
        BANNER_CORNER_PILLARS = registerBanner("divider_corner_pillars", "jebb.section.corner_pillars");

        MAIN_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, MAIN_TAB_ID,
                FabricItemGroup.builder()
                        .title(Component.translatable("itemGroup." + JustEnoughtBuildingBlocks.MOD_ID + ".main"))
                        .icon(() -> new ItemStack(Items.OAK_PLANKS))
                        .displayItems((params, output) -> {
                            for (JebbSeparatorItem b : BANNER_VERTICAL_SLABS) output.accept(b);
                            for (Block b : JebbBlocks.VERTICAL_SLABS.values()) output.accept(b);
                            for (JebbSeparatorItem b : BANNER_QUARTERS) output.accept(b);
                            for (Block b : JebbBlocks.QUARTERS.values()) output.accept(b);
                            for (JebbSeparatorItem b : BANNER_CORNER_PILLARS) output.accept(b);
                            for (Block b : JebbBlocks.CORNER_PILLARS.values()) output.accept(b);
                        })
                        .build());
    }

    private static JebbSeparatorItem[] registerBanner(String prefix, String labelKey) {
        JebbSeparatorItem[] items = new JebbSeparatorItem[BANNER_WIDTH];
        for (int i = 0; i < BANNER_WIDTH; i++) {
            ResourceLocation id = new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, prefix + "_" + i);
            JebbSeparatorItem item = new JebbSeparatorItem(new Item.Properties().stacksTo(1), labelKey);
            items[i] = Registry.register(BuiltInRegistries.ITEM, id, item);
        }
        return items;
    }
}
