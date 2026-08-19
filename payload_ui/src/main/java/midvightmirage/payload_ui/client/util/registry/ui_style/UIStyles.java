package midvightmirage.payload_ui.client.util.registry.ui_style;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class UIStyles {
    private static Supplier<Pair<Identifier, UIStyle>> DEFAULT;

    public static final MinecraftUIStyle MINECRAFT = register("minecraft", new MinecraftUIStyle(), true);
    public static final EditorUIStyle EDITOR = register("editor", EditorUIStyle.INSTANCE);

    private static <T extends UIStyle> T register(String name, T style, boolean isDefault) {
        Identifier id = Identifier.fromNamespaceAndPath("payload", name);
        if (isDefault) {
            DEFAULT = () -> Pair.of(id, style);
        }
        return style;
    }

    public static <T extends UIStyle> T register(String name, T style) {
        return register(name, style, false);
    }

    public static Pair<Identifier, UIStyle> getDefault() {
        return DEFAULT.get();
    }

    public static void bootstrap() {}
}
