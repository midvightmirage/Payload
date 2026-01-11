package midvightmirage.payload.compat.modmenu.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import midvightmirage.payload.Payload;
import net.maksiuhrino.simpleapi.api.util.config.ClothConfigSerializer;
import net.maksiuhrino.simpleapi.api.util.config.GsonClothConfigSerializer;
import net.minecraft.network.chat.Component;

import java.util.Collection;
import java.util.LinkedList;

public class PayloadConfigScreen {
    private GsonClothConfigSerializer<ConfigData> configSerializer;
    private ConfigData configData;

    public PayloadConfigScreen() {
        configSerializer = new GsonClothConfigSerializer<>(Payload.MOD_ID, ConfigData.class);
        try {
            configData = configSerializer.deserialize();
        } catch (ClothConfigSerializer.ClothSerializationException e) {
            Payload.LOGGER.error(e.getLocalizedMessage());
        }
    }

    public ConfigBuilder getConfigBuilder() {
        try {
            this.configData = configSerializer.deserialize();
        } catch (ClothConfigSerializer.ClothSerializationException e) {
            Payload.LOGGER.error(e.getLocalizedMessage());
        }

        ConfigBuilder builder = ConfigBuilder.create().setTitle(Component.translatable("title.cloth-config.config"));
        builder.setGlobalized(true);
        builder.setGlobalizedExpanded(false);
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory testing = builder.getOrCreateCategory(Component.literal("Category"));
        SubCategoryBuilder depends = entryBuilder.startSubCategory(Component.literal("Testing")).setExpanded(true);
        BooleanListEntry dependency = entryBuilder.startBooleanToggle(Component.literal("A Cool Toggle"), configData.getTesting().getDependency()).setSaveConsumer(
                (b) -> configData.getTesting().setDependency(b)
        ).build();
        depends.add(dependency);
        Collection<BooleanListEntry> toggles = new LinkedList();
        depends.addAll(toggles);
        testing.addEntry(depends.build());

        builder.setSavingRunnable(
                () -> {
                    try {
                        configSerializer.serialize(this.configData);
                    } catch (ClothConfigSerializer.ClothSerializationException e) {
                        Payload.LOGGER.error(e.getLocalizedMessage());
                    }
                }
        );

        return builder;
    }
}
