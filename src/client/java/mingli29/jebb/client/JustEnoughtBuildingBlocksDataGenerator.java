package mingli29.jebb.client;

import mingli29.jebb.client.datagen.JebbBlockLootProvider;
import mingli29.jebb.client.datagen.JebbBlockTagProvider;
import mingli29.jebb.client.datagen.JebbModelProvider;
import mingli29.jebb.client.datagen.JebbRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class JustEnoughtBuildingBlocksDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(JebbModelProvider::new);
        pack.addProvider(JebbBlockLootProvider::new);
        pack.addProvider(JebbRecipeProvider::new);
        pack.addProvider(JebbBlockTagProvider::new);
    }
}
