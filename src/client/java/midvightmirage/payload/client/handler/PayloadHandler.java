package midvightmirage.payload.client.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import midvightmirage.payload.Payload;
import midvightmirage.payload.client.handler.pack.block.BlockReader;
import midvightmirage.payload.client.handler.pack.block.type.BlockTypeReader;
import midvightmirage.payload.client.handler.pack.item.ItemReader;
import midvightmirage.payload.client.handler.pack.item.tab.TabReader;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class PayloadHandler {
    private PackInfo packInfo = new PackInfo();
    public static Map<Path, PackInfo> packInfos = new HashMap<>();
    private final Gson gson;
    public static final PayloadHandler INSTANCE = new PayloadHandler();
    public static List<Path> packs;

    public Map<Path, PackInfo> getPackInfos() {
        return packInfos;
    }

    public PayloadHandler() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public static void createPayloadFolder() {
        Path payloadFolder = FabricLoader.getInstance().getGameDir().resolve("payload/packs");

        if (!Files.exists(payloadFolder)) {
            try {
                Files.createDirectories(payloadFolder);
                Payload.LOGGER.debug("Created folder: {}", payloadFolder);
            } catch (IOException e) {
                Payload.LOGGER.error(e.getLocalizedMessage());
            }
        }
    }

    public static List<Path> getFolders(Path directory) {
        List<Path> folders = new ArrayList<>();

        if (!Files.exists(directory) || !Files.isDirectory(directory)) {
            createPayloadFolder();
            return folders;
        }

        try (Stream<Path> stream = Files.list(directory)) {
            stream
                    .filter(Files::isDirectory)
                    .forEach(folders::add);
        } catch (IOException e) {
            Payload.LOGGER.error(e.getLocalizedMessage());
        }

        return folders;
    }

    public static List<Path> getFolders() {
        return getFolders(FabricLoader.getInstance().getGameDir().resolve("payload/packs"));
    }

    public void loadPack(Path path) throws IOException {
        BufferedReader reader = Files.newBufferedReader(path.resolve("pack.json"));

        this.packInfo = this.gson.fromJson(reader, PackInfo.class);
    }

    @Deprecated
    public void loadPack(String name) throws IOException {
        Path packPath = FabricLoader.getInstance().getGameDir().resolve("payload/packs").resolve(name).resolve("pack.json");

        loadPack(packPath);
    }

    public void bootstrap() {
        packs = getFolders();

        for (Path path : packs) {
            try {
                loadPack(path);
                packInfos.put(path, this.packInfo);
                for (Map.Entry<String, String> entry : this.packInfo.getPack().getDependencies().entrySet()) {
                    if (!FabricLoader.getInstance().isModLoaded(entry.getKey())) {
                        Payload.LOGGER.error("Mod ID \"{}\" isn't loaded.", entry.getKey());
                    }
                }
                ItemReader.INSTANCE.bootstrap(path, this.packInfo);
                BlockReader.INSTANCE.bootstrap(path, this.packInfo);
                TabReader.INSTANCE.bootstrap(path, this.packInfo);
                BlockTypeReader.INSTANCE.bootstrap(path, this.packInfo);
            } catch (IOException e) {
                Payload.LOGGER.error(e.getLocalizedMessage());
            }
        }
    }
}
