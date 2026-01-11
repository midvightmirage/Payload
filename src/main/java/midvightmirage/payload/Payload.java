package midvightmirage.payload;

import midvightmirage.payload.registry.block.PayloadBlocks;
import midvightmirage.payload.registry.entity.PayloadEntities;
import midvightmirage.payload.registry.item.PayloadItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Payload implements ModInitializer {
    public static final String MOD_ID = "payload";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        PayloadBlocks.INSTANCE.bootstrap();
        PayloadItems.INSTANCE.bootstrap();
        PayloadItems.PayloadCreativeModeTabs.INSTANCE.bootstrap();
        PayloadEntities.INSTANCE.bootstrap();
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
