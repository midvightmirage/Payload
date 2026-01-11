package midvightmirage.payload.client.datagen.lang;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class PayloadEnUsLangProvider extends FabricLanguageProvider {
    public PayloadEnUsLangProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.@NotNull Provider provider, @NotNull TranslationBuilder translationBuilder) {
        translationBuilder.add("payload.addons", "Addons");
        translationBuilder.add("payload.addonsFolder", "Open Addons Folder");
    }
}
