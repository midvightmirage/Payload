package midvightmirage.payload_ui.client.util.registry.ui_style;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import midvightmirage.payload_ui.client.PayloadUIClient;
import midvightmirage.payload_ui.client.util.PayloadUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import org.joml.Vector2i;
import org.jspecify.annotations.*;
import org.lwjgl.glfw.GLFW;

import java.util.*;
import java.util.function.*;

public class EditorUIStyle extends UIStyle {
    @Override
    public void createTextBox(Screen screen, @Nullable List<Map<String, Object>> parent, Supplier<MutableComponent> hint, Vector2i pos, Vector2i size, Consumer<String> onSearch, Supplier<Boolean> shouldClear, int paddingX) {
        var widget = new AbstractWidget(pos.x, pos.y, size.x, size.y, hint.get()) {
            private String value = "";
            private int position = 0;
            private float frame = 0;

            @Override
            protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
                int color = 0xFF666666;

                frame += a;

                if (PayloadUtil.isMouseIn(
                        new Vector2i(mouseX, mouseY),
                        pos,
                        size
                )) {
                    graphics.requestCursor(CursorTypes.IBEAM);
                    color = 0xFF777777;
                }

                graphics.fill(
                        EditorUIStyle.this.getDefaultPipeline(false),
                        pos.x,
                        pos.y,
                        pos.x + size.x,
                        pos.y + size.y,
                        color
                );

                MutableComponent text = hint.get();
                Font font = EditorUIStyle.this.getDefaultFont();

                if (!this.isFocused()) {
                    graphics.text(
                            font,
                            getWithDefaultStyle(text, false),
                            pos.x + ((size.x - font.width(text)) / 2),
                            pos.y + ((size.y - font.lineHeight) / 2),
                            0xFFAAAAAA,
                            false
                    );
                } else {
                    if ((int)(this.frame / 10) % 2 == 1) {
                        int textWidth = font.width(this.value);
                        graphics.text(
                                font,
                                getWithDefaultStyle(Component.literal("I"), true),
                                pos.x + 5 + paddingX + textWidth,
                                pos.y + ((size.y - font.lineHeight) / 2),
                                0xFFFFFFFF,
                                false
                        );
                    }

                    if (!this.value.isEmpty()) {
                        graphics.text(
                                font,
                                getWithDefaultStyle(Component.literal(this.value), false),
                                pos.x + 5 + paddingX,
                                pos.y + ((size.y - font.lineHeight) / 2),
                                0xFFFFFFFF,
                                false
                        );
                    }
                }
            }

            @Override
            protected void updateWidgetNarration(NarrationElementOutput output) {

            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            @Override
            public boolean keyPressed(KeyEvent event) {
                switch (event.key()) {
                    case GLFW.GLFW_KEY_BACKSPACE -> {
                        if (!this.value.isEmpty()) {
                            this.value     = this.value.substring(0, this.value.length() - 1);
                            this.position -= 1;
                        }
                        return true;
                    }
                    default -> {}
                }

                return super.keyPressed(event);
            }

            @Override
            public boolean charTyped(CharacterEvent event) {
                if (this.isFocused() && this.isActive()) {
                    this.value    += event.codepointAsString();
                    this.position += 1;
                    this.frame     = 0;
                    return true;
                }

                return super.charTyped(event);
            }
        };

        screen.addWidget(widget);

        Renderable renderable = (graphics, mouseX, mouseY, a) -> {
            widget.extractRenderState(graphics, mouseX, mouseY, a);
            if (shouldClear.get()) {
                widget.setValue("");
            }
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
        Renderable renderable = (graphics, mouseX, mouseY, _) -> {
            graphics.fill(
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

    public static class FontCollection {
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

        public FontDescription getDescription(String name) {
            boolean bold = name.equals("bold") || name.equals("bold_italic");
            boolean italic = name.equals("italic") || name.equals("bold_italic");

            return getDescription(bold, italic);
        }
    }

    @Override
    public Style getDefaultStyle(boolean italic, boolean bold, boolean underlined, boolean strikethrough, boolean monospace) {
        FontCollection collection = monospace ? FontCollection.SPACE_MONO : FontCollection.INTER;
        return Style.EMPTY.withFont(collection.getDescription(bold, italic));
    }

    @Override
    public RenderPipeline getDefaultPipeline(boolean textured) {
        return super.getDefaultPipeline(textured);
    }
}
