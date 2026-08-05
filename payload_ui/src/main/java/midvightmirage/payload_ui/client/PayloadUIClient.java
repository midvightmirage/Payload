package midvightmirage.payload_ui.client;

import midvightmirage.payload_ui.client.util.registry.ui_style.UIStyles;
import net.fabricmc.api.ClientModInitializer;

public class PayloadUIClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        UIStyles.bootstrap();
    }
}
