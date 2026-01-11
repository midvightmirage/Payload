package midvightmirage.payload.client.datagen;

import midvightmirage.payload.client.datagen.lang.PayloadEnUsLangProvider;
import midvightmirage.payload.client.datagen.lang.PayloadPlPlLangProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class PayloadDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(PayloadEnUsLangProvider::new);
        pack.addProvider(PayloadPlPlLangProvider::new);
        pack.addProvider(PayloadModelProvider::new);
    }
}
