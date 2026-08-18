package midvightmirage.payload_ui.client.util.registry.ui_style;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import midvightmirage.payload_ui.client.util.PayloadUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

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

        this.createSimpleRenderable(screen, parent, renderable, new HashMap<>() {{
            put("type",           "container");
            put("container_type", "vertical");
            put("padding",         padding);
        }});
    }

    public void createSimpleRenderable(Screen screen, @Nullable List<Map<String, Object>> parent, Renderable renderable, Map<String, Object> additionalData) {
        this.createCustom(
                parent,
                new HashMap<>() {{
                    put("data",       additionalData);
                    put("renderable", renderable    );
                }}
        );
    }

    public abstract void createTextBox(Screen screen, @Nullable List<Map<String, Object>> parent, Supplier<MutableComponent> hint, Vector2i pos, Vector2i size, Consumer<String> onSearch, Supplier<Boolean> shouldClear, int paddingX);
    public abstract void createButton(Screen screen, @Nullable List<Map<String, Object>> parent, Supplier<MutableComponent> label, Runnable onPress, Vector2i pos, Vector2i size, int paddingX);
    public void createLabel(Screen screen, @Nullable List<Map<String, Object>> parent, Supplier<MutableComponent> label, Vector2i pos, Vector2i area, Color color, boolean shadow, HorizontalAlignment horizontal, VerticalAlignment vertical, boolean monospace, Map<String, Object> additionalData) {
        Font font = this.getDefaultFont();

        MutableComponent component = label.get();

        if (area == null || area.equals(0, 0))
            area = new Vector2i(font.width(label.get()), font.lineHeight);

        Vector2i finalArea = area;

        Renderable renderable = (graphics, _, _, _) -> {
            int textWidth = font.width(component);
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
            graphics.pose().pushMatrix();

            Vector2f position = new Vector2f(pos.x + additionalX, pos.y + additionalY);

            graphics.pose().translate(position.x, position.y);
            if (additionalData.containsKey("scale")) {
                Vector2f scale = (Vector2f) additionalData.get("scale");
                graphics.pose().scale(scale.x, scale.y);
            }

            graphics.text(
                    font,
                    this.getWithDefaultStyle(component, monospace),
                    0, 0,
                    PayloadUtil.toARGB(color),
                    shadow
            );

            graphics.pose().popMatrix();
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
    public void createLabel(Screen screen, @Nullable List<Map<String, Object>> parent, Supplier<MutableComponent> label, Vector2i pos, Vector2i area, Color color, boolean monospace, Object isMonospace, Map<String, Object> additionalData) {
        this.createLabel(screen, parent, label, pos, area, color, false, monospace, additionalData);
    }
    public void createLabel(Screen screen, @Nullable List<Map<String, Object>> parent, Supplier<MutableComponent> label, Vector2i pos, Vector2i area, Color color, HorizontalAlignment horizontal, VerticalAlignment vertical, boolean monospace, Map<String, Object> additionalData) {
        this.createLabel(screen, parent, label, pos, area, color, false, horizontal, vertical, monospace, additionalData);
    }
    public void createLabel(Screen screen, @Nullable List<Map<String, Object>> parent, Supplier<MutableComponent> label, Vector2i pos, Vector2i area, Color color, boolean shadow, boolean monospace, Map<String, Object> additionalData) {
        this.createLabel(screen, parent, label, pos, area, color, shadow, HorizontalAlignment.LEFT, VerticalAlignment.TOP, monospace, additionalData);
    }

    public void createLabel(Screen screen, @Nullable List<Map<String, Object>> parent, Supplier<MutableComponent> label, Vector2i pos, Vector2i area, Color color, Map<String, Object> additionalData) {
        this.createLabel(screen, parent, label, pos, area, color, false, false, additionalData);
    }
    public void createLabel(Screen screen, @Nullable List<Map<String, Object>> parent, Supplier<MutableComponent> label, Vector2i pos, Vector2i area, Color color, HorizontalAlignment horizontal, VerticalAlignment vertical, Map<String, Object> additionalData) {
        this.createLabel(screen, parent, label, pos, area, color, false, horizontal, vertical, false, additionalData);
    }
    public void createLabel(Screen screen, @Nullable List<Map<String, Object>> parent, Supplier<MutableComponent> label, Vector2i pos, Vector2i area, Color color, boolean shadow, Map<String, Object> additionalData) {
        this.createLabel(screen, parent, label, pos, area, color, shadow, HorizontalAlignment.LEFT, VerticalAlignment.TOP, false, additionalData);
    }
    public void createFill(Screen screen, @Nullable List<Map<String, Object>> parent, Vector2i pos, Vector2i size, Color color) {
        Renderable renderable = (graphics, _, _, _) -> {
            graphics.fill(
                    this.getDefaultPipeline(false),
                    pos.x,
                    pos.y,
                    pos.x + size.x,
                    pos.y + size.y,
                    PayloadUtil.toARGB(color)
            );
        };
        this.createSimpleRenderable(
                screen,
                parent,
                renderable,
                new HashMap<>() {{
                    put("type", "fill");
                    put("area",  new HashMap<>() {{
                        put("pos",  pos);
                        put("size", size);
                    }});
                    put("color", color);
                }}
        );
    }
    public void createImage(Screen screen, @Nullable List<Map<String, Object>> parent, Identifier id, Vector2i pos, Vector2i size, Color tint) {
        Renderable renderable = (graphics, _, _, _) -> {
            graphics.blit(
                    this.getDefaultPipeline(true),
                    id,
                    pos.x,
                    pos.y,
                    0,
                    0,
                    size.x,
                    size.y,
                    size.x,
                    size.y,
                    PayloadUtil.toARGB(tint)
            );
        };

        this.createCustom(
                parent,
                new HashMap<>() {{
                    put("data",       new HashMap<>() {{
                        put("type", "image");
                        put("id",    id);
                        put("area",  new HashMap<>() {{
                            put("pos",  pos);
                            put("size", size);
                        }});
                        put("tint",  tint);
                    }});
                    put("renderable", renderable);
                }}
        );
    }
    public void createCustom(@Nullable List<Map<String, Object>> parent, Map<String, Object> info) {
        Objects.requireNonNullElse(parent, this.objectsData).add(info);
    }
    public void clear() {
        this.objectsData.clear();
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

    public RenderPipeline getDefaultPipeline(boolean textured) {
        return textured ? RenderPipelines.GUI_TEXTURED : RenderPipelines.GUI;
    }

    public Font getDefaultFont() {
        return Minecraft.getInstance().font;
    }

    public Style getDefaultStyle(boolean italic, boolean bold, boolean underlined, boolean strikethrough, boolean monospace) {
        return Style.EMPTY.withItalic(italic).withBold(bold).withUnderlined(underlined).withStrikethrough(strikethrough);
    }

    protected final MutableComponent getWithDefaultStyle(MutableComponent component, boolean monospace) {
        return component.setStyle(getDefaultStyle(
                component.getStyle().isItalic(),
                component.getStyle().isBold(),
                component.getStyle().isUnderlined(),
                component.getStyle().isStrikethrough(),
                monospace
        ));
    }
}
