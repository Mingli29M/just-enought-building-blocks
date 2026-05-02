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
    public static final ResourceLocation VERTICAL_SLAB_TAB_ID = new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "vertical_slabs");
    public static final ResourceLocation QUARTER_TAB_ID = new ResourceLocation(JustEnoughtBuildingBlocks.MOD_ID, "quarters");

    public static CreativeModeTab VERTICAL_SLAB_TAB;
    public static CreativeModeTab QUARTER_TAB;

    private JebbItemGroups() {
    }

    public static void register() {
        VERTICAL_SLAB_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, VERTICAL_SLAB_TAB_ID,
                FabricItemGroup.builder()
                        .title(Component.translatable("itemGroup." + JustEnoughtBuildingBlocks.MOD_ID + ".vertical_slabs"))
                        .icon(() -> new ItemStack(Items.OAK_PLANKS))
                        .displayItems((params, output) -> {
                            for (Block b : JebbBlocks.VERTICAL_SLABS.values()) output.accept(b);
                        })
                        .build());

        QUARTER_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, QUARTER_TAB_ID,
                FabricItemGroup.builder()
                        .title(Component.translatable("itemGroup." + JustEnoughtBuildingBlocks.MOD_ID + ".quarters"))
                        .icon(() -> new ItemStack(Items.STONE_BRICKS))
                        .displayItems((params, output) -> {
                            for (Block b : JebbBlocks.QUARTERS.values()) output.accept(b);
                        })
                        .build());
    }
}
