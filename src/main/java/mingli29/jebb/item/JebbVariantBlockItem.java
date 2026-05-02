package mingli29.jebb.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class JebbVariantBlockItem extends BlockItem {
    private final Block parent;
    private final String prefixKey;

    public JebbVariantBlockItem(Block variant, Block parent, String prefixKey, Properties properties) {
        super(variant, properties);
        this.parent = parent;
        this.prefixKey = prefixKey;
    }

    @Override
    public Component getName(ItemStack stack) {
        return buildName();
    }

    @Override
    public Component getDescription() {
        return buildName();
    }

    private MutableComponent buildName() {
        return Component.translatable(prefixKey, parent.getName());
    }
}
