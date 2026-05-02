package mingli29.jebb;

import mingli29.jebb.block.JebbBlocks;
import mingli29.jebb.item.JebbItemGroups;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JustEnoughtBuildingBlocks implements ModInitializer {
    public static final String MOD_ID = "just-enought-building-blocks";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        JebbBlocks.init();
        JebbItemGroups.register();
    }
}
