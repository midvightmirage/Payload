package midvightmirage.payload_ui.client.util.registry;


import midvightmirage.payload_ui.client.util.registry.ui_style.UIStyle;
import midvightmirage.payload_ui.client.util.registry.ui_style.UIStyles;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.Optional;

public class PayloadRegistries {
    public static final DefaultedRegistry<UIStyle> UI_STYLE = BuiltInRegistries.registerDefaultedWithIntrusiveHolders(
            PayloadRegistryKeys.UI_STYLE,
            "minecraft",
            (_) -> UIStyles.MINECRAFT
    );

    public static <T> T getDefault(DefaultedRegistry<T> tDefaultedRegistry) {
        Optional<Holder.Reference<T>> tHolder = tDefaultedRegistry.get(tDefaultedRegistry.getDefaultKey());
        return tHolder.orElseThrow().value();
    }
}
