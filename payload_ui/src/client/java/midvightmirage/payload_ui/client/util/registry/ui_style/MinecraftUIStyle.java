package midvightmirage.payload_ui.client.util.registry.ui_style;

import midvightmirage.payload_ui.client.util.PayloadUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.joml.Vector2i;

import java.awt.*;
import java.util.LinkedHashMap;
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

    @Override
    public void createLabel(Screen screen, Component label, Vector2i pos, Vector2i area, Color color, boolean shadow, HorizontalAlignment horizontal, VerticalAlignment vertical) {
        Renderable renderable = (graphics, _, _, _) -> {
            Font font = Minecraft.getInstance().font;
            int textWidth = font.width(label);
            int textHeight = font.lineHeight;

            int additionalX = switch (horizontal) {
                case LEFT -> 0;
                case CENTER -> (area.x - textWidth) / 2;
                case RIGHT -> area.x - textWidth;
            };
            int additionalY = switch (vertical) {
                case TOP -> 0;
                case CENTER -> (area.y - textHeight) / 2;
                case BOTTOM -> area.y - textHeight;
            };
            graphics.text(
                    font,
                    label,
                    pos.x + additionalX,
                    pos.y + additionalY,
                    PayloadUtil.toARGB(color),
                    shadow
            );
        };
        this.createCustom(
                Map.of(
                        "data", Map.of(
                                "label",     label,
                                "pos",       pos,
                                "area",      area,
                                "color",     color,
                                "shadow",    shadow,
                                "alignment", Map.of(
                                        "horizontal", horizontal,
                                        "vertical",   vertical
                                )
                        ),
                        "renderable", renderable
                )
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public void createObject(Screen screen, String type, Map<String, Object> args) throws UnsupportedOperationException {
        switch (type) {
            case "button" -> {
                Map<String, Vector2i> bounds = (Map<String, Vector2i>)(args.get("bounds"));
                this.createButton(
                        screen,
                        (Component) args.get("label"),
                        (Button.OnPress) args.get("onPress"),
                        bounds.get("pos"),
                        bounds.get("size")
                );
            }
            case "label" -> {
                Map<String, Object> alignment = (Map<String, Object>)args.getOrDefault("alignment", new LinkedHashMap<>());
                this.createLabel(
                        screen,
                        (Component)           args.get("label"),
                        (Vector2i)            args.get("pos"),
                        (Vector2i)            args.getOrDefault("area", new Vector2i()),
                        (Color)               args.getOrDefault("color", Color.WHITE),
                        (boolean)             args.getOrDefault("shadow", false),
                        (HorizontalAlignment) alignment.getOrDefault("horizontal", HorizontalAlignment.LEFT),
                        (VerticalAlignment)   alignment.getOrDefault("vertical", VerticalAlignment.TOP)
                );
            }
            default -> super.createObject(screen, type, args);
        }
    }
}
