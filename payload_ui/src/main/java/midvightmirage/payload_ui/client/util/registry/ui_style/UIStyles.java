package midvightmirage.payload_ui.client.util.registry.ui_style;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class UIStyles {
    private static final Map<Identifier, UIStyle> UI_STYLES = new HashMap<>();
    private static Supplier<Pair<Identifier, UIStyle>> DEFAULT;

    public static Map<Identifier, UIStyle> getUiStyles() {
        return UI_STYLES;
    }

    public static final MinecraftUIStyle MINECRAFT = register("minecraft", new MinecraftUIStyle(), true);
    public static final EditorUIStyle EDITOR = register("editor", new EditorUIStyle());

    private static <T extends UIStyle> T register(String name, T style, boolean isDefault) {
        Identifier id = Identifier.fromNamespaceAndPath("payload", name);
        UI_STYLES.put(
                id,
                style
        );
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
