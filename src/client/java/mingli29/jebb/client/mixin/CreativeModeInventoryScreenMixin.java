package mingli29.jebb.client.mixin;

import mingli29.jebb.client.render.JebbBannerRenderer;
import mingli29.jebb.item.JebbItemGroups;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreenMixin extends AbstractContainerScreen<CreativeModeInventoryScreen.ItemPickerMenu> {

    @Shadow
    private static CreativeModeTab selectedTab;

    @Shadow
    private float scrollOffs;

    private CreativeModeInventoryScreenMixin() {
        super(null, null, null);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void jebb$renderSectionBanners(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (selectedTab != JebbItemGroups.MAIN_TAB || JebbItemGroups.SECTION_ROW_LABELS.isEmpty()) {
            return;
        }

        int totalItems = selectedTab.getDisplayItems().size();
        int totalRows = Mth.ceil(totalItems / (float) JebbItemGroups.ROW_WIDTH);
        int maxScroll = Math.max(0, totalRows - 5);
        int rowsScrolled = Math.round(maxScroll * scrollOffs);

        for (Map.Entry<Integer, String> entry : JebbItemGroups.SECTION_ROW_LABELS.entrySet()) {
            int sectionRow = entry.getKey();
            int visibleRow = sectionRow - rowsScrolled;
            if (visibleRow < 0 || visibleRow > 4) {
                continue;
            }
            int x = this.leftPos + 9;
            int y = this.topPos + 18 + visibleRow * 18;
            JebbBannerRenderer.draw(guiGraphics, x, y, entry.getValue());
        }
    }
}
