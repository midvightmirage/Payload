package midvightmirage.payload.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class PayloadDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(
                (dataOutput, registryLookup) ->
                        new PayloadLangProvider(dataOutput, registryLookup, pack)
        );
        pack.addProvider(PayloadModelProvider::new);
    }
}
