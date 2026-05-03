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
    public static final ResourceLocation MAIN_TAB_ID = new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "main");

    public static CreativeModeTab MAIN_TAB;

    public static JebbSeparatorItem DIVIDER_VERTICAL_SLABS;
    public static JebbSeparatorItem DIVIDER_QUARTERS;
    public static JebbSeparatorItem DIVIDER_CORNER_PILLARS;

    private JebbItemGroups() {
    }

    public static void register() {
        DIVIDER_VERTICAL_SLABS = registerSeparator("divider_vertical_slabs", "jebb.section.vertical_slabs");
        DIVIDER_QUARTERS = registerSeparator("divider_quarters", "jebb.section.quarters");
        DIVIDER_CORNER_PILLARS = registerSeparator("divider_corner_pillars", "jebb.section.corner_pillars");

        MAIN_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, MAIN_TAB_ID,
                FabricItemGroup.builder()
                        .title(Component.translatable("itemGroup." + JustEnoughtBuildingBlocks.MOD_ID + ".main"))
                        .icon(() -> new ItemStack(Items.OAK_PLANKS))
                        .displayItems((params, output) -> {
                            output.accept(DIVIDER_VERTICAL_SLABS);
                            for (Block b : JebbBlocks.VERTICAL_SLABS.values()) output.accept(b);
                            output.accept(DIVIDER_QUARTERS);
                            for (Block b : JebbBlocks.QUARTERS.values()) output.accept(b);
                            output.accept(DIVIDER_CORNER_PILLARS);
                            for (Block b : JebbBlocks.CORNER_PILLARS.values()) output.accept(b);
                        })
                        .build());
    }

    private static JebbSeparatorItem registerSeparator(String name, String labelKey) {
        ResourceLocation id = new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, name);
        JebbSeparatorItem item = new JebbSeparatorItem(new Item.Properties().stacksTo(1), labelKey);
        return Registry.register(BuiltInRegistries.ITEM, id, item);
    }
}
