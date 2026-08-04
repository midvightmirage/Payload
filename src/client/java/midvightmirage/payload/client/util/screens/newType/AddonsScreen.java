package midvightmirage.payload.client.util.screens.newType;

import midvightmirage.payload_ui.client.util.screen.PayloadScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class AddonsScreen extends PayloadScreen {
    public AddonsScreen(Screen parent) {
        super(parent, Component.translatable("payload.addons"));
    }
}
