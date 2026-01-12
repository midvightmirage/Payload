package midvightmirage.payload.client.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import midvightmirage.payload.Payload;
import midvightmirage.payload.client.handler.pack.block.BlockReader;
import midvightmirage.payload.client.handler.pack.item.ItemReader;
import midvightmirage.payload.client.handler.pack.item.tab.TabReader;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.repository.PackRepository;

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
    private Map<Path, PackInfo> packInfos = null;
    private final Gson gson;
    public static final PayloadHandler INSTANCE = new PayloadHandler();

    public PayloadHandler() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.packInfos = new HashMap<>();
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
        List<Path> packs = getFolders();

        for (Path path : packs) {
            try {
                loadPack(path);
                this.packInfos.put(path, this.packInfo);
                for (Map.Entry<String, String> entry : this.packInfo.getPack().getDependencies().entrySet()) {
                    if (!FabricLoader.getInstance().isModLoaded(entry.getKey())) {
                        Payload.LOGGER.error("Mod ID \"{}\" isn't loaded.", entry.getKey());
                    }
                }
                ItemReader.INSTANCE.bootstrap(path, this.packInfo);
                BlockReader.INSTANCE.bootstrap(path, this.packInfo);
                TabReader.INSTANCE.bootstrap(path, this.packInfo);
            } catch (IOException e) {
                Payload.LOGGER.error(e.getLocalizedMessage());
            }
        }
    }
}
