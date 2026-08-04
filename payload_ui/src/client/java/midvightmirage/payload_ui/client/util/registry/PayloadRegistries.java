package midvightmirage.payload_ui.client.util.registry;


import midvightmirage.payload_ui.client.util.registry.ui_style.UIStyle;
import midvightmirage.payload_ui.client.util.registry.ui_style.UIStyles;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;

public class PayloadRegistries {
    public static final DefaultedRegistry<UIStyle> UI_STYLE = BuiltInRegistries.registerDefaultedWithIntrusiveHolders(
            PayloadRegistryKeys.UI_STYLE,
            "minecraft",
            (_) -> UIStyles.MINECRAFT
    );
}
