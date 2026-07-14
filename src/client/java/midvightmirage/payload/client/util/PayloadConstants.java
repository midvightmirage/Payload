package midvightmirage.payload.client.util;

import midvightmirage.payload.Payload;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class PayloadConstants {
    public static final Component REQUIRED = Component.translatable("required", "");

    public static final Identifier EXCLAMATION_POPUP = Payload.id("popups/exclamation");
}
