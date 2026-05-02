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

public final class JebbItemGroups {
    public static final ResourceLocation TAB_ID = new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "main");
    public static CreativeModeTab MAIN_TAB;

    private JebbItemGroups() {
    }

    public static void register() {
        MAIN_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, TAB_ID,
                FabricItemGroup.builder()
                        .title(Component.translatable("itemGroup." + JustEnoughtBuildingBlocks.MOD_ID + ".main"))
                        .icon(() -> new ItemStack(Items.STONE))
                        .displayItems((params, output) -> {
                            for (Block b : JebbBlocks.VERTICAL_SLABS.values()) output.accept(b);
                            for (Block b : JebbBlocks.QUARTERS.values()) output.accept(b);
                        })
                        .build());
    }
}
