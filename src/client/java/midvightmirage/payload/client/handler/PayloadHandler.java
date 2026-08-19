package midvightmirage.payload.client.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.datafixers.util.Pair;
import midvightmirage.payload.Payload;
import midvightmirage.payload.client.handler.pack.block.BlockReader;
import midvightmirage.payload.client.handler.pack.block.type.BlockTypeReader;
import midvightmirage.payload.client.handler.pack.item.ItemReader;
import midvightmirage.payload.client.handler.pack.item.tab.TabReader;
import midvightmirage.payload.client.util.ConversionUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class PayloadHandler {
    private PackInfo packInfo = new PackInfo();
    public static Map<Path, PackInfo> packInfos = new LinkedHashMap<>();
    private final Gson gson;
    public static final PayloadHandler INSTANCE = new PayloadHandler();
    public static List<Path> packs;

    public Map<Path, PackInfo> getPackInfos() {
        return packInfos;
    }

    public static Path getPackPathFromPack(PackInfo.Pack pack) {
        List<Pair<Path, PackInfo>> pairs = ConversionUtil.mapToPairs(packInfos);

        for (Pair<Path, PackInfo> pair : pairs) {
            if (pair.getSecond().getPack() == pack) {
                return pair.getFirst();
            }
        }

        return Path.of("");
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
        return getFolders(getPayloadPacksFolder());
    }

    public void loadPack(Path path) throws IOException {
        BufferedReader reader = Files.newBufferedReader(path.resolve("pack.json"));

        this.packInfo = this.gson.fromJson(reader, PackInfo.class);

        String packId = this.packInfo.getPack().getId();

        if (ids.contains(packId)) {
            throw new RuntimeException("Another pack already has the id of \"" + packId + "\"!");
        }

        ids.add(packId);

        String iconPath = this.packInfo.getPack().getIcon();

        if (iconPath != null) {
            afterInit.add(() -> {
                Identifier id = Identifier.fromNamespaceAndPath("payload_packs", packId);

                Path fullIconPath = path.resolve(iconPath);

                try (InputStream stream = Files.newInputStream(fullIconPath)) {
                    NativeImage image = NativeImage.read(stream);

                    Minecraft.getInstance().getTextureManager().register(
                            id,
                            new DynamicTexture(() -> iconPath, image)
                    );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private final List<String> ids = new ArrayList<>();
    public static final List<Runnable> afterInit = new ArrayList<>();

    public static Path getPayloadPacksFolder() {
        return FabricLoader.getInstance().getGameDir().resolve("payload/packs");
    }

    /**
     * @deprecated Use {@link PayloadHandler#loadPack(Path)}
     */
    @Deprecated
    public void loadPack(String name) throws IOException {
        Path packPath = getPayloadPacksFolder().resolve(name).resolve("pack.json");

        loadPack(packPath);
    }

    public void bootstrap() {
        ids.clear();
        afterInit.clear();
        packs = getFolders();

        for (Path path : packs) {
            try {
                loadPack(path);
                packInfos.put(path, this.packInfo);
                if (this.packInfo.getPack().getDependencies() != null) {
                    for (Map.Entry<String, String> entry : this.packInfo.getPack().getDependencies().entrySet()) {
                        if (!FabricLoader.getInstance().isModLoaded(entry.getKey())) {
                            Payload.LOGGER.error("Mod ID \"{}\" isn't loaded.", entry.getKey());
                        }
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
