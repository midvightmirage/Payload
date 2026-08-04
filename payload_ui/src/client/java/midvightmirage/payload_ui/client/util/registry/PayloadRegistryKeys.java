package midvightmirage.payload_ui.client.util.registry;

import midvightmirage.payload_ui.client.util.registry.ui_style.UIStyle;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class PayloadRegistryKeys {
    public static final ResourceKey<Registry<UIStyle>> UI_STYLE = createRegistryKey("ui_style");

    private static <T> ResourceKey<Registry<T>> createRegistryKey(final String name) {
        return ResourceKey.createRegistryKey(Identifier.withDefaultNamespace(name));
    }
}
