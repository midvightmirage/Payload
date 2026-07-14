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
        translationBuilder.add("payload.addons.editing", "Editing pack \"%d\"");
        translationBuilder.add("payload.addons.creation", "Pack creation");

        translationBuilder.add("payload.addons.config.pack_name", "Pack Name");
        translationBuilder.add("payload.addons.config.pack_desc", "Pack Description");
        translationBuilder.add("payload.addons.config.pack_id", "Pack ID");

        translationBuilder.add("optional", "Optional%d");
        translationBuilder.add("required", "Required%d");

        translationBuilder.add("opt_comma", "%d, %d");

        translationBuilder.add("util.field_empty", "The field \"%d\" is empty!");
        translationBuilder.add("util.leave_blank", "leave blank, to generate one");

        translationBuilder.add("modmenu.descriptionTranslation.payload", "A simple registry mod for Minecraft.");
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
            translationBuilder.add("payload.addons.editing", "Edytowanie paczki \"%d\"");
            translationBuilder.add("payload.addons.creation", "Tworzenie paczki");

            translationBuilder.add("payload.addons.config.pack_name", "Nazwa Paczki");
            translationBuilder.add("payload.addons.config.pack_desc", "Opis Paczki");
            translationBuilder.add("payload.addons.config.pack_id", "ID Paczki");

            translationBuilder.add("optional", "Opcjonalne%d");
            translationBuilder.add("required", "Wymagane%d");

            translationBuilder.add("modmenu.descriptionTranslation.payload", "Prosty w użyciu mod rejestru do Minecraft.");

            translationBuilder.add("util.field_empty", "Pole \"%d\" jest puste!");
            translationBuilder.add("util.leave_blank", "zostaw puste, by wygenerować");
        }
    }
}
