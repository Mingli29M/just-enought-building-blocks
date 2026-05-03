package mingli29.jebb.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Placeholder banner item used to visually divide sections inside the creative tab.
 * Holds a translation key for its display name; the texture is supplied by an item model
 * pointing at any vanilla icon (paper) so we ship no PNGs.
 */
public class JebbSeparatorItem extends Item {
    private final String labelKey;

    public JebbSeparatorItem(Properties properties, String labelKey) {
        super(properties);
        this.labelKey = labelKey;
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(labelKey);
    }

    @Override
    public Component getDescription() {
        return Component.translatable(labelKey);
    }
}
