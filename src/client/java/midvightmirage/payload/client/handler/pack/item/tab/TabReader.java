package midvightmirage.payload.client.handler.pack.item.tab;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import midvightmirage.payload.Payload;
import midvightmirage.payload.registry.item.PayloadItems;
import midvightmirage.payload.client.handler.PackInfo;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TabReader {
    private TabInfo tabInfo;
    private final Gson gson;

    public static final TabReader INSTANCE = new TabReader();

    public TabReader() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public static List<Path> getTabs(Path directory) {
        List<Path> items = new ArrayList<>();

        if (!Files.exists(directory) || !Files.isDirectory(directory)) {
            return items;
        }

        try (Stream<Path> stream = Files.list(directory)) {
            stream
                    .filter(Files::isRegularFile)
                    .forEach(items::add);
        } catch (IOException e) {
            Payload.LOGGER.error(e.getLocalizedMessage());
        }

        return items;
    }

    public void loadTab(Path path) throws IOException {
        BufferedReader reader = Files.newBufferedReader(path);

        this.tabInfo = (TabInfo) (this.gson.fromJson(reader, TabInfo.class));
    }

    public void bootstrap(Path pack, PackInfo info) {
        List<Path> items = getTabs(
                FabricLoader.getInstance().getGameDir().resolve("payload\\packs")
                        .resolve(pack).resolve("data\\tabs")
        );
        for (Path path : items) {
            try {
                loadTab(path);
            } catch (IOException e) {
                Payload.LOGGER.error(e.getLocalizedMessage());
            }
            Component title;
            switch (tabInfo.getTitle().getType()) {
                case "translatable" -> title = Component.translatable(tabInfo.getTitle().getName());
                case "literal" -> title = Component.literal(tabInfo.getTitle().getName());
                default -> title = Component.literal("null");
            }
            PayloadItems.PayloadCreativeModeTabs.Builder builder = new PayloadItems.PayloadCreativeModeTabs.Builder(
                    Identifier.fromNamespaceAndPath(info.getPack().getId(), tabInfo.getId()),
                    FabricItemGroup.builder()
                            .title(title)
                            .icon(() -> new ItemStack(getItem(tabInfo.getIcon())))
                            .displayItems((params, output) -> {
                                for (String string : tabInfo.getDisplayItems()) {
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
        return BuiltInRegistries.ITEM.get(id).get().value();
    }
}
