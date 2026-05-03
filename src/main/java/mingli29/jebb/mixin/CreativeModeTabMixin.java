package mingli29.jebb.mixin;

import mingli29.jebb.item.JebbItemGroups;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Mixin(CreativeModeTab.class)
public abstract class CreativeModeTabMixin {

    @Mutable
    @Shadow
    private Collection<ItemStack> displayItems;

    @Inject(method = "buildContents", at = @At("TAIL"))
    private void jebb$injectSectionBanners(CreativeModeTab.ItemDisplayParameters parameters, CallbackInfo ci) {
        CreativeModeTab self = (CreativeModeTab) (Object) this;
        if (self != JebbItemGroups.MAIN_TAB || JebbItemGroups.SECTIONS == null) {
            return;
        }

        Set<Block> sectionBlocks = new HashSet<>();
        for (JebbItemGroups.Section section : JebbItemGroups.SECTIONS) {
            sectionBlocks.addAll(section.blocks());
        }

        List<ItemStack> remaining = new ArrayList<>(this.displayItems);
        Set<ItemStack> claimed = new LinkedHashSet<>();

        List<ItemStack> result = new ArrayList<>();
        JebbItemGroups.SECTION_ROW_LABELS.clear();
        int currentRow = 0;

        for (JebbItemGroups.Section section : JebbItemGroups.SECTIONS) {
            JebbItemGroups.SECTION_ROW_LABELS.put(currentRow, section.labelKey());
            for (int i = 0; i < JebbItemGroups.ROW_WIDTH; i++) {
                result.add(ItemStack.EMPTY);
            }
            currentRow++;

            int placed = 0;
            for (Block block : section.blocks()) {
                ItemStack found = takeFirstFor(remaining, block);
                if (found == null) {
                    continue;
                }
                claimed.add(found);
                result.add(found);
                placed++;
            }

            int rowRem = placed % JebbItemGroups.ROW_WIDTH;
            int pad = rowRem == 0 ? 0 : JebbItemGroups.ROW_WIDTH - rowRem;
            for (int i = 0; i < pad; i++) {
                result.add(ItemStack.EMPTY);
            }
            currentRow += (placed + pad) / JebbItemGroups.ROW_WIDTH;
        }

        for (ItemStack stack : remaining) {
            if (claimed.contains(stack)) {
                continue;
            }
            result.add(stack);
        }

        this.displayItems = result;
    }

    private static ItemStack takeFirstFor(List<ItemStack> stacks, Block block) {
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack candidate = stacks.get(i);
            if (!candidate.isEmpty() && candidate.getItem() == block.asItem()) {
                stacks.remove(i);
                return candidate;
            }
        }
        return null;
    }
}
