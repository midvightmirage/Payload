package midvightmirage.payload.client.handler.pack.item;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import midvightmirage.payload.Payload;
import midvightmirage.payload.registry.item.PayloadItems;
import midvightmirage.payload.client.handler.PackInfo;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ItemReader {
    private ItemInfo itemInfo;
    private final Gson gson;

    public static final ItemReader INSTANCE = new ItemReader();

    public ItemReader() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public static List<Path> getItems(Path directory) {
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

    public void loadItem(Path path) throws IOException {
        BufferedReader reader = Files.newBufferedReader(path);

        this.itemInfo = (ItemInfo) (this.gson.fromJson(reader, ItemInfo.class));
    }

    public void bootstrap(Path pack, PackInfo info) {
        List<Path> items = getItems(
                FabricLoader.getInstance().getGameDir().resolve("payload\\packs")
                        .resolve(pack).resolve("data\\items")
        );
        for (Path path : items) {
            try {
                loadItem(path);
            } catch (IOException e) {
                Payload.LOGGER.error(e.getLocalizedMessage());
            }
            PayloadItems.INSTANCE.registerItem(Identifier.fromNamespaceAndPath(info.getPack().getId(), itemInfo.getId()), new Item.Properties());
        }
    }
}
