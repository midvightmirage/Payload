package midvightmirage.payload.client.util;

import midvightmirage.payload.Payload;
import midvightmirage.payload_ui.client.util.SVGLoader;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.resources.Identifier;

import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

public class PayloadIconRegistry {
    public static final Map<String, Identifier> REGISTERED = new HashMap<>();
    public static final int BEST_RESOLUTION_SIZE = 128;

    private static boolean registered = false;

    public static void bootstrap() {
        if (!registered) {
            List<String> icons = new ArrayList<>(getAllIcons());
            icons.removeIf(name -> name.equalsIgnoreCase("license"));

            for (String icon : icons) {
                register(icon);
            }
        }
        registered = true;
    }

    private static void register(String name, int width, int height, Color color) {
        Identifier id = Payload.id("icons/" + name.replace('-', '_'));
        Payload.LOGGER.info("Registering icon {}.", id);
        REGISTERED.put(name, SVGLoader.getAndRegisterSVG(id, getIconPath(name), width, height, color));
    }

    private static void register(String name, int width, int height) {
        register(name, width, height, Color.WHITE);
    }

    private static void register(String name, Color color) {
        register(name, BEST_RESOLUTION_SIZE, BEST_RESOLUTION_SIZE, color);
    }

    private static void register(String name) {
        register(name, Color.WHITE);
    }

    private static Path getIconsPath() {
        try {
            ModContainer container = FabricLoader.getInstance().getModContainer(Payload.MOD_ID).orElseThrow();
            Path path = container.findPath("fabric.mod.json").orElseThrow().getParent();
            return path.resolve("payload/icons");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> getAllIcons() {
        List<String> files = new ArrayList<>();

        Path iconsPath = getIconsPath();
        for (File file : Objects.requireNonNull(iconsPath.toFile().listFiles())) {
            String filename = file.toPath().getFileName().toString();
            files.add(filename.substring(0, filename.lastIndexOf('.')));
        }

        return files;
    }

    private static Path getIconPath(String name) {
        try {
            Optional<ModContainer> optionalContainer = FabricLoader.getInstance().getModContainer(Payload.MOD_ID);
            if (optionalContainer.isPresent()) {
                ModContainer container = optionalContainer.get();
                Optional<Path> optionalPath = container.findPath("payload/icons/" + name + ".svg");
                if (optionalPath.isPresent()) {
                    return optionalPath.get();
                }
            }
            throw new Exception("Could not find \"" + name + "\"");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
