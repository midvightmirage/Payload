package midvightmirage.payload.client.datagen.lang;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class PayloadPlPlLangProvider extends FabricLanguageProvider {
    public PayloadPlPlLangProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "pl_pl", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.@NotNull Provider provider, @NotNull TranslationBuilder translationBuilder) {
        translationBuilder.add("payload.addons", "Dodatki");
        translationBuilder.add("payload.addonsFolder", "Otwórz Folder z Dodatkami");
    }
}
