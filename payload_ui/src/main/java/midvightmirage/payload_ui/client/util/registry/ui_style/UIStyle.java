package midvightmirage.payload_ui.client.util.registry.ui_style;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import midvightmirage.payload_ui.client.util.PayloadUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.joml.Vector2i;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.BiConsumer;

public abstract class UIStyle implements Renderable {
    protected final List<Map<String, Object>> objectsData = new ArrayList<>();

    public void createContainer(Screen screen, @Nullable List<Map<String, Object>> parent, BiConsumer<List<Map<String, Object>>, Screen> contentConsumer) {
        List<Map<String, Object>> content = new ArrayList<>();

        contentConsumer.accept(content, screen);

        Renderable renderable = (graphics, mouseX, mouseY, a) -> {
            for (Map<String, Object> map : content) {
                ((Renderable)map.get("renderable")).extractRenderState(graphics, mouseX, mouseY, a);
            }
        };

        this.createCustom(
                parent,
                new HashMap<>() {{
                    put("data",       new HashMap<>() {{
                        put("type",          "container");
                        put("container_type", null);
                    }});
                    put("renderable", renderable);
                }}
        );
    }
    @SuppressWarnings("unchecked")
    public void createVerticalContainer(Screen screen, @Nullable List<Map<String, Object>> parent, int padding, BiConsumer<List<Map<String, Object>>, Screen> contentConsumer) {
        List<Map<String, Object>> content = new ArrayList<>();
        contentConsumer.accept(content, screen);

        Renderable renderable = (graphics, mouseX, mouseY, a) -> {
            int lastY = 0;

            for (Map<String, Object> map : content) {
                Map<String, Vector2i> bounds = (Map<String, Vector2i>) map.get("bounds");
                bounds.get("area").y = lastY;
                lastY += bounds.get("area").y;
                ((Renderable)map.get("renderable")).extractRenderState(graphics, mouseX, mouseY, a);
            }
        };

        this.createCustom(
                parent,
                new HashMap<>() {{
                    put("data",       new HashMap<>() {{
                        put("type",           "container");
                        put("container_type", "vertical");
                        put("padding",         padding);
                    }});
                    put("renderable", renderable);
                }}
        );
    }

    public abstract void createButton(Screen screen, @Nullable List<Map<String, Object>> parent, MutableComponent label, Button.OnPress onPress, Vector2i pos, Vector2i size);
    public void createLabel(Screen screen, @Nullable List<Map<String, Object>> parent, MutableComponent label, Vector2i pos, Vector2i area, Color color, boolean shadow, HorizontalAlignment horizontal, VerticalAlignment vertical, boolean monospace) {
        Font font = this.getDefaultFont();

        if (area == null || area.equals(0, 0))
            area = new Vector2i(font.width(label), font.lineHeight);

        Vector2i finalArea = area;

        Renderable renderable = (graphics, _, _, _) -> {
            int textWidth = font.width(label);
            int textHeight = font.lineHeight;

            int additionalX = switch (horizontal) {
                case LEFT -> 0;
                case CENTER -> (finalArea.x - textWidth) / 2;
                case RIGHT -> finalArea.x - textWidth;
            };
            int additionalY = switch (vertical) {
                case TOP -> 0;
                case CENTER -> (finalArea.y - textHeight) / 2;
                case BOTTOM -> finalArea.y - textHeight;
            };
            graphics.text(
                    font,
                    label.setStyle(getDefaultStyle(
                            label.getStyle().isItalic(),
                            label.getStyle().isBold(),
                            label.getStyle().isUnderlined(),
                            label.getStyle().isStrikethrough(),
                            monospace
                    )),
                    pos.x + additionalX,
                    pos.y + additionalY,
                    PayloadUtil.toARGB(color),
                    shadow
            );
        };
        this.createCustom(
                parent,
                new HashMap<>() {{
                    put("data",       new HashMap<>() {{
                        put("type",     "label");
                        put("label",     label);
                        put("bounds",    new HashMap<>() {{
                            put("pos",  pos);
                            put("area", finalArea);
                        }});
                        put("color",     color);
                        put("shadow",    shadow);
                        put("alignment", new HashMap<>() {{
                                put("horizontal", horizontal);
                                put("vertical",   vertical);
                        }});
                    }});
                    put("renderable", renderable);
                }}
        );
    }
    public void createLabel(Screen screen, @Nullable List<Map<String, Object>> parent, MutableComponent label, Vector2i pos, Vector2i area, Color color, boolean monospace, Object isMonospace) {
        this.createLabel(screen, parent, label, pos, area, color, false, monospace);
    }
    public void createLabel(Screen screen, @Nullable List<Map<String, Object>> parent, MutableComponent label, Vector2i pos, Vector2i area, Color color, HorizontalAlignment horizontal, VerticalAlignment vertical, boolean monospace) {
        this.createLabel(screen, parent, label, pos, area, color, false, horizontal, vertical, monospace);
    }
    public void createLabel(Screen screen, @Nullable List<Map<String, Object>> parent, MutableComponent label, Vector2i pos, Vector2i area, Color color, boolean shadow, boolean monospace) {
        this.createLabel(screen, parent, label, pos, area, color, shadow, HorizontalAlignment.LEFT, VerticalAlignment.TOP, monospace);
    }

    public void createLabel(Screen screen, @Nullable List<Map<String, Object>> parent, MutableComponent label, Vector2i pos, Vector2i area, Color color) {
        this.createLabel(screen, parent, label, pos, area, color, false, false);
    }
    public void createLabel(Screen screen, @Nullable List<Map<String, Object>> parent, MutableComponent label, Vector2i pos, Vector2i area, Color color, HorizontalAlignment horizontal, VerticalAlignment vertical) {
        this.createLabel(screen, parent, label, pos, area, color, false, horizontal, vertical, false);
    }
    public void createLabel(Screen screen, @Nullable List<Map<String, Object>> parent, MutableComponent label, Vector2i pos, Vector2i area, Color color, boolean shadow) {
        this.createLabel(screen, parent, label, pos, area, color, shadow, HorizontalAlignment.LEFT, VerticalAlignment.TOP, false);
    }
    @SuppressWarnings("unchecked")
    public void createObject(Screen screen, @Nullable List<Map<String, Object>> parent, String type, Map<String, Object> args) throws UnsupportedOperationException {
        switch (type) {
            case "button" -> {
                Map<String, Vector2i> bounds = (Map<String, Vector2i>)(args.get("bounds"));
                this.createButton(
                        screen,
                        parent,
                        (MutableComponent) args.get("label"),
                        (Button.OnPress) args.get("onPress"),
                        bounds.get("pos"),
                        bounds.get("size")
                );
            }
            case "label" -> {
                Map<String, Object> alignment = (Map<String, Object>)args.getOrDefault("alignment", new LinkedHashMap<>());
                Map<String, Vector2i> bounds = (Map<String, Vector2i>)(args.get("bounds"));
                this.createLabel(
                        screen,
                        parent,
                        (MutableComponent)    args.get("label"),
                                              bounds.get("pos"),
                                              bounds.getOrDefault("area", new Vector2i()),
                        (Color)               args.getOrDefault("color", Color.WHITE),
                        (boolean)             args.getOrDefault("shadow", false),
                        (HorizontalAlignment) alignment.getOrDefault("horizontal", HorizontalAlignment.LEFT),
                        (VerticalAlignment)   alignment.getOrDefault("vertical", VerticalAlignment.TOP),
                        (boolean)             args.getOrDefault("monospace", false)
                );
            }
            case "custom" -> this.createCustom(parent, args);
            default -> throw new UnsupportedOperationException("Type " + type + " not implemented yet");
        }
    }
    public void createCustom(@Nullable List<Map<String, Object>> parent, Map<String, Object> info) {
        Objects.requireNonNullElse(parent, this.objectsData).add(info);
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        for (Map<String, Object> objectData : objectsData) {
            Renderable renderable = (Renderable) objectData.get("renderable");
            renderable.extractRenderState(graphics, mouseX, mouseY, a);
        }
    }

    public enum HorizontalAlignment {
        LEFT,
        CENTER,
        RIGHT
    }

    public enum VerticalAlignment {
        TOP,
        CENTER,
        BOTTOM
    }

    public RenderPipeline getDefaultPipeline() {
        return RenderPipelines.GUI_TEXTURED;
    }

    public Font getDefaultFont() {
        return Minecraft.getInstance().font;
    }

    public Style getDefaultStyle(boolean italic, boolean bold, boolean underlined, boolean strikethrough, boolean monospace) {
        return Style.EMPTY.withItalic(italic).withBold(bold).withUnderlined(underlined).withStrikethrough(strikethrough);
    }
}
