package midvightmirage.payload.client.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import org.jetbrains.annotations.NotNull;

public class PayloadModelProvider extends FabricModelProvider {
    public PayloadModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(@NotNull BlockModelGenerators blockModelGenerators) {}

    @Override
    public void generateItemModels(@NotNull ItemModelGenerators itemModelGenerators) {}
}
