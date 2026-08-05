package midvightmirage.payload_ui.client.util.registry.ui_style;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.joml.Vector2i;

import java.awt.*;
import java.util.Map;

public class MinecraftUIStyle extends UIStyle {
    @Override
    public void createButton(Screen screen, Component label, Button.OnPress onPress, Vector2i pos, Vector2i size) {
        Button button = Button.builder(label, onPress).bounds(pos.x, pos.y, size.x, size.y).build();
        screen.addWidget(button);
        this.createCustom(
                Map.of(
                        "data", Map.of(
                                "label", label,
                                "onPress", onPress,
                                "pos", pos,
                                "size", size
                        ),
                        "renderable", button
                )
        );
    }
}
