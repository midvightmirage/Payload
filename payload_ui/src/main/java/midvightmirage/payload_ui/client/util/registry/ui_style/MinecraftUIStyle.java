package midvightmirage.payload_ui.client.util.registry.ui_style;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.MutableComponent;
import org.joml.Vector2i;
import org.jspecify.annotations.Nullable;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class MinecraftUIStyle extends UIStyle {
    @Override
    public void createButton(Screen screen, @Nullable List<Map<String, Object>> parent, MutableComponent label, Button.OnPress onPress, Vector2i pos, Vector2i size) {
        Button button = Button.builder(label, onPress).bounds(pos.x, pos.y, size.x, size.y).build();
        screen.addWidget(button);
        this.createCustom(
                parent,
                Map.of(
                        "data", Map.of(
                                "type",    "button",
                                "label",   label,
                                "onPress", onPress,
                                "area",    Map.of(
                                        "pos",  pos,
                                        "size", size
                                )
                        ),
                        "renderable", button
                )
        );
    }
}
