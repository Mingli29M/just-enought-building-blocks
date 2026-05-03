package mingli29.jebb.client.mixin;

import mingli29.jebb.client.render.JebbBannerRenderer;
import mingli29.jebb.item.JebbItemGroups;
import net.minecraft.client.gui.GuiGraphics;
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
public abstract class CreativeModeInventoryScreenMixin {

    @Shadow
    private static CreativeModeTab selectedTab;

    @Shadow
    private float scrollOffs;

    @Inject(method = "render", at = @At("TAIL"))
    private void jebb$renderSectionBanners(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        Map<Integer, JebbItemGroups.SectionRowInfo> sectionRows = JebbItemGroups.sectionRowsForTab(selectedTab);
        if ((selectedTab != JebbItemGroups.MAIN_TAB && selectedTab != JebbItemGroups.BLOCKS_TAB)
                || sectionRows.isEmpty()) {
            return;
        }

        int totalItems = selectedTab.getDisplayItems().size();
        int totalRows = Mth.ceil(totalItems / (float) JebbItemGroups.ROW_WIDTH);
        int maxScroll = Math.max(0, totalRows - 5);
        int rowsScrolled = Math.round(maxScroll * scrollOffs);
        AbstractContainerScreenAccessor pos = (AbstractContainerScreenAccessor) (Object) this;
        int leftPos = pos.jebb$getLeftPos();
        int topPos = pos.jebb$getTopPos();

        for (Map.Entry<Integer, JebbItemGroups.SectionRowInfo> entry : sectionRows.entrySet()) {
            int sectionRow = entry.getKey();
            int visibleRow = sectionRow - rowsScrolled;
            if (visibleRow < 0 || visibleRow > 4) {
                continue;
            }
            int x = leftPos + 9;
            int y = topPos + 18 + visibleRow * 18;
            JebbItemGroups.SectionRowInfo info = entry.getValue();
            JebbBannerRenderer.draw(guiGraphics, x, y, info.labelKey(), info.bannerStyle());
        }
    }
}
