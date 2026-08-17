package midvightmirage.payload.client.util.screens.newType;

import midvightmirage.payload_ui.client.util.registry.ui_style.UIStyle;
import midvightmirage.payload_ui.client.util.registry.ui_style.UIStyles;
import midvightmirage.payload_ui.client.util.screen.PayloadScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.joml.Vector2i;

import java.awt.*;

public class AddonsScreen extends PayloadScreen {
    public AddonsScreen(Screen parent) {
        super(parent, UIStyles.EDITOR, Component.translatable("payload.addons"));
        this.setBackgroundColor(new Color(76, 76, 76));
    }

    @Override
    protected void init(UIStyle style) {
        style.createLabel(
                this,
                null,
                this.title.copy().setStyle(Style.EMPTY),
                new Vector2i(10, 10),
                new Vector2i(),
                Color.WHITE
        );
    }
}
