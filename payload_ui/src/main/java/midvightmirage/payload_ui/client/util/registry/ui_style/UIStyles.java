package midvightmirage.payload_ui.client.util.registry.ui_style;

import midvightmirage.payload_ui.client.util.registry.PayloadRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

public class UIStyles {
    public static final UIStyle MINECRAFT = register("minecraft", new MinecraftUIStyle());

    private static UIStyle register(String name, UIStyle style) {
        return Registry.register(
                PayloadRegistries.UI_STYLE,
                Identifier.fromNamespaceAndPath("payload", name),
                style
        );
    }
}
