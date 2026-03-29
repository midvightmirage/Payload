package midvightmirage.payload.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class PayloadLangProvider extends FabricLanguageProvider {
    public PayloadLangProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup, FabricDataGenerator.Pack pack) {
        super(dataOutput, "en_us", registryLookup);
        pack.addProvider(PlPlLangProvider::new);
    }

    @Override
    public void generateTranslations(HolderLookup.@NotNull Provider provider, @NotNull TranslationBuilder translationBuilder) {
        translationBuilder.add("payload.addons", "Addons");
        translationBuilder.add("payload.addonsFolder", "Open Addons Folder");
        translationBuilder.add("payload.addons.editor", "Editor (Beta)");
    }

    public static class PlPlLangProvider extends FabricLanguageProvider {
        public PlPlLangProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(dataOutput, "pl_pl", registryLookup);
        }

        @Override
        public void generateTranslations(HolderLookup.@NotNull Provider provider, @NotNull TranslationBuilder translationBuilder) {
            translationBuilder.add("payload.addons", "Dodatki");
            translationBuilder.add("payload.addonsFolder", "Otwórz Folder z Dodatkami");
            translationBuilder.add("payload.addons.editor", "Edytor (Beta)");
        }
    }
}
