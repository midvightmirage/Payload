package midvightmirage.payload.client.handler.pack.item.tab;

import com.mojang.datafixers.util.Pair;
import midvightmirage.payload.Payload;
import midvightmirage.payload.client.handler.pack.PackJsonReader;
import midvightmirage.payload.registry.item.PayloadItems;
import midvightmirage.payload.client.handler.PackInfo;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class TabReader extends PackJsonReader<TabReader, TabInfo> {
    public static final TabReader INSTANCE = new TabReader();

    public TabReader() {
        super(TabReader.class, TabInfo.class);
    }

    @Override
    public void bootstrap(Path pack, PackInfo info) {
        Pair<Boolean, List<Path>> items = getItems(
                FabricLoader.getInstance().getGameDir().resolve("payload/packs")
                        .resolve(pack).resolve("data/tabs")
        );
        for (Path path : items.getSecond()) {
            try {
                loadItem(path);
            } catch (IOException e) {
                Payload.LOGGER.error(e.getLocalizedMessage());
            }
            Component title;
            switch (objectInfo.getTitle().getType()) {
                case "translatable" -> title = Component.translatable(objectInfo.getTitle().getName());
                case "literal" -> title = Component.literal(objectInfo.getTitle().getName());
                default -> title = Component.literal("null");
            }
            PayloadItems.PayloadCreativeModeTabs.Builder builder = new PayloadItems.PayloadCreativeModeTabs.Builder(
                    Identifier.fromNamespaceAndPath(info.getPack().getId(), objectInfo.getId()),
                    FabricCreativeModeTab.builder()
                            .title(title)
                            .icon(() -> new ItemStack(getItem(objectInfo.getIcon())))
                            .displayItems((_, output) -> {
                                for (String string : objectInfo.getDisplayItems()) {
                                    output.accept(getItem(string));
                                }
                            })
                            .build()
            );
            PayloadItems.PayloadCreativeModeTabs.INSTANCE.registerGroup(builder);
        }
    }
    private Item getItem(String item) {
        Identifier id = Identifier.tryParse(item);
        if (id != null) {
            Optional<Holder.Reference<Item>> itemReference = BuiltInRegistries.ITEM.get(id);
            if (itemReference.isPresent()) {
                return itemReference.get().value();
            }
        }
        throw new RuntimeException("Item with id: " + item + " not found");
    }
}
