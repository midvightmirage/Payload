package midvightmirage.payload_ui.client.util.registry.ui_style;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import midvightmirage.payload_ui.client.PayloadUIClient;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import org.joml.Vector2i;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditorUIStyle extends UIStyle {
    @Override
    public void createButton(Screen screen, @Nullable List<Map<String, Object>> parent, MutableComponent label, Button.OnPress onPress, Vector2i pos, Vector2i size) {
        Renderable renderable = (graphics, mouseX, mouseY, a) -> {
            graphics.fill(
                    this.getDefaultPipeline(),
                    pos.x,
                    pos.y,
                    pos.x + size.x,
                    pos.y + size.y,
                    0xFF666666
            );
        };

        this.createCustom(
                parent,
                new HashMap<>() {{
                    put("data",       new HashMap<>() {{
                        put("type",   "button");
                        put("label",   label);
                        put("onPress", onPress);
                        put("area",    new HashMap<>() {{
                            put("pos",  pos);
                            put("size", size);
                        }});

                    }});
                    put("renderable", renderable);
                }}
        );
    }

    private static class FontCollection {
        public static final FontCollection INTER = new FontCollection("inter");
        public static final FontCollection SPACE_MONO = new FontCollection("space_mono");

        private final FontDescription bold;
        private final FontDescription boldItalic;
        private final FontDescription italic;
        private final FontDescription regular;

        FontCollection(String bold, String boldItalic, String italic, String regular) {
            this.bold = getFromString(bold);
            this.boldItalic = getFromString(boldItalic);
            this.italic = getFromString(italic);
            this.regular = getFromString(regular);
        }

        FontCollection(String name) {
            this(
                    name + "/bold",
                    name + "/bold_italic",
                    name + "/italic",
                    name + "/regular"
            );
        }

        private FontDescription getFromString(String name) {
            Identifier id = PayloadUIClient.id(name);
            return new FontDescription.Resource(id);
        }

        public FontDescription getBold() {
            return bold;
        }
        public FontDescription getBoldItalic() {
            return boldItalic;
        }
        public FontDescription getItalic() {
            return italic;
        }
        public FontDescription getRegular() {
            return regular;
        }

        public FontDescription getDescription(boolean bold, boolean italic) {
            if (bold) {
                return italic ? getBoldItalic() : getBold();
            } else {
                return italic ? getItalic() : getRegular();
            }
        }
    }

    @Override
    public Style getDefaultStyle(boolean italic, boolean bold, boolean underlined, boolean strikethrough, boolean monospace) {
        FontCollection collection = switch (monospace) {
            case true -> FontCollection.SPACE_MONO;
            case false -> FontCollection.INTER;
        };
        return Style.EMPTY.withFont(collection.getDescription(bold, italic));
    }

    @Override
    public RenderPipeline getDefaultPipeline() {
        return super.getDefaultPipeline();
    }
}
