package midvightmirage.payload_ui.client;

import midvightmirage.payload_ui.client.util.registry.ui_style.UIStyles;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayloadUIClient implements ClientModInitializer {
    public static final String MOD_ID = "payload_ui";
    public static final Logger LOGGER = LoggerFactory.getLogger(PayloadUIClient.class);

    @Override
    public void onInitializeClient() {
        UIStyles.bootstrap();
    }

    public static Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(MOD_ID, name);
    }
}
