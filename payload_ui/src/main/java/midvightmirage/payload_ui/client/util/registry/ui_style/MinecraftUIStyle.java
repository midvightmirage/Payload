package midvightmirage.payload_ui.client.util.registry.ui_style;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.MutableComponent;
import org.joml.Vector2i;
import org.jspecify.annotations.Nullable;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MinecraftUIStyle extends UIStyle {
    @Override
    public void createTextBox(Screen screen, @Nullable List<Map<String, Object>> parent, Supplier<MutableComponent> hint, Vector2i pos, Vector2i size, Consumer<String> onSearch, Supplier<Boolean> shouldClear, int paddingX) {
        EditBox box = new EditBox(this.getDefaultFont(), pos.x, pos.y, size.x, size.y, hint.get());
        box.setHint(hint.get());
        box.setResponder(onSearch);
        screen.addWidget(box);
        Renderable renderable = (graphics, mouseX, mouseY, a) -> {
            if (shouldClear.get()) {
                box.setValue("");
            }

            box.extractRenderState(graphics, mouseX, mouseY, a);
        };
        this.createCustom(
                parent,
                new HashMap<>() {{
                    put("data",       new HashMap<>() {{
                        put("hint",        hint);
                        put("area",        new HashMap<>() {{
                            put("pos",  pos);
                            put("size", size);
                        }});
                        put("onSearch",    onSearch);
                        put("shouldClear", shouldClear);
                    }});
                    put("renderable", renderable);
                }}
        );
    }

    @Override
    public void createButton(Screen screen, @Nullable List<Map<String, Object>> parent, Supplier<MutableComponent> label, Button.OnPress onPress, Vector2i pos, Vector2i size) {
        Button button = Button.builder(label.get(), onPress).bounds(pos.x, pos.y, size.x, size.y).build();
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
