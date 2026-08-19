package midvightmirage.payload_ui.client.util.registry.ui_style;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import midvightmirage.payload_ui.client.PayloadUIClient;
import midvightmirage.payload_ui.client.util.PayloadUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Range;
import org.joml.Vector2i;
import org.jspecify.annotations.*;
import org.lwjgl.glfw.GLFW;

import java.util.*;
import java.util.function.*;

public class EditorUIStyle extends UIStyle {
    public static final EditorUIStyle INSTANCE = new EditorUIStyle();

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

                if (this.value.isEmpty()) {
                    if (!this.isFocused()) {
                        graphics.text(
                                font,
                                getWithDefaultStyle(text, false),
                                pos.x + ((size.x - font.width(text)) / 2),
                                pos.y + ((size.y - font.lineHeight) / 2),
                                0xFFAAAAAA,
                                false
                        );
                    }
                } else {
                    if (this.isFocused()) {
                        if ((int) (this.frame / 10) % 2 == 1) {
                            int textWidth = font.width(this.value.substring(0, this.position));
                            graphics.text(
                                    font,
                                    getWithDefaultStyle(Component.literal("I"), false),
                                    pos.x + 4 + paddingX + textWidth,
                                    pos.y + ((size.y - font.lineHeight) / 2),
                                    0xFFFFFFFF,
                                    false
                            );
                        }
                    }

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

            @Override
            public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
                if (!this.isActive()) {
                    return false;
                }

                if (isValidClickButton(event.buttonInfo())) {
                    boolean isMouseOver = this.isMouseOver(event.x(), event.y());
                    setFocused(isMouseOver);
                    return true;
                }

                return super.mouseClicked(event, doubleClick);
            }

            @Override
            protected void updateWidgetNarration(NarrationElementOutput output) {

            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
                this.position = value.length();
            }

            @Override
            public boolean keyPressed(KeyEvent event) {
                switch (event.key()) {
                    case GLFW.GLFW_KEY_BACKSPACE -> {
                        if (this.position > 0) {
                            removeChar(-1);
                            return true;
                        }
                    }
                    case GLFW.GLFW_KEY_DELETE -> {
                        if (this.position < this.value.length()) {
                            removeChar(1);
                            return true;
                        }
                    }
                    case GLFW.GLFW_KEY_LEFT -> {
                        if (this.position > 0) {
                            this.position--;
                            return true;
                        }
                    }
                    case GLFW.GLFW_KEY_RIGHT -> {
                        if (this.position < this.value.length()) {
                            this.position++;
                            return true;
                        }
                    }
                    case GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER, GLFW.GLFW_KEY_ESCAPE -> {
                        this.setFocused(false);
                        return true;
                    }
                    default -> {}
                }

                return super.keyPressed(event);
            }

            private void addString(String string) {
                this.value = this.value.substring(0, this.position) + string + this.value.substring(this.position);
                this.position++;
            }

            private void removeChar(@Range(from = -1, to = 1) int direction) {
                if (direction < 0) {
                    this.value = this.value.substring(0, this.position - 1) + this.value.substring(this.position);
                    this.position--;
                } else {
                    this.value = this.value.substring(0, this.position) + this.value.substring(this.position + 1);
                }
            }

            @Override
            public boolean charTyped(CharacterEvent event) {
                if (this.isFocused() && this.isActive()) {
                    addString(event.codepointAsString());
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
    public void createButton(Screen screen, @Nullable List<Map<String, Object>> parent, Supplier<MutableComponent> label, Runnable onPress, Vector2i pos, Vector2i size, int paddingX) {
        var widget = new AbstractWidget(pos.x, pos.y, size.x, size.y, label.get()) {
            private Runnable onPress;

            @Override
            protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
                int color = !isHoveredOrFocused() ? 0xFF666666 : 0xFF777777;

                graphics.fill(
                        pos.x,
                        pos.y,
                        pos.x + size.x,
                        pos.y + size.y,
                        color
                );

                MutableComponent component = label.get();

                Font font = EditorUIStyle.this.getDefaultFont();
                int textWidth = font.width(component);
                int textHeight = font.lineHeight;

                graphics.text(
                        font,
                        EditorUIStyle.this.getWithDefaultStyle(component, false),
                        pos.x+((size.x - textWidth )/2)+paddingX,
                        pos.y+((size.y - textHeight)/2),
                        0xFFFFFFFF,
                        false
                );

                this.handleCursor(graphics);
            }

            @Override
            protected void updateWidgetNarration(NarrationElementOutput output) {

            }

            private void onPress() {
                onPress.run();
            }

            @Override
            public boolean keyPressed(KeyEvent event) {
                if (!this.isActive()) {
                    return false;
                } else if (event.isSelection()) {
                    this.playDownSound(Minecraft.getInstance().getSoundManager());
                    this.onPress();
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void onClick(MouseButtonEvent event, boolean doubleClick) {
                onPress();
            }

            public Runnable getOnPress() {
                return onPress;
            }

            public void setOnPress(Runnable onPress) {
                this.onPress = onPress;
            }
        };
        widget.setOnPress(onPress);
        screen.addWidget(widget);

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
                    put("renderable", widget);
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

    public static Style getStyle(boolean italic, boolean bold, boolean underlined, boolean strikethrough, boolean monospace) {
        return INSTANCE.getDefaultStyle(italic, bold, underlined, strikethrough, monospace);
    }

    public static Font getFont() {
        return INSTANCE.getDefaultFont();
    }

    @Override
    public RenderPipeline getDefaultPipeline(boolean textured) {
        return super.getDefaultPipeline(textured);
    }

    public static RenderPipeline getPipeline(boolean textured) {
        return INSTANCE.getDefaultPipeline(textured);
    }

    public static MutableComponent getWithStyle(MutableComponent component, boolean monospace) {
        return INSTANCE.getWithDefaultStyle(component, monospace);
    }
}
