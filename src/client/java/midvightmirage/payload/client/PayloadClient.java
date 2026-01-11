package midvightmirage.payload.client;

import midvightmirage.payload.client.handler.PayloadHandler;
import net.fabricmc.api.ClientModInitializer;

public class PayloadClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        PayloadHandler.INSTANCE.bootstrap();
    }
}
