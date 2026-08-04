package midvightmirage.payload_ui.client.util.registry.ui_style;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.joml.Vector2i;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class UIStyle implements Renderable {
    protected final List<Map<String, Object>> objectsData = new ArrayList<>();

    public abstract void createButton(Screen screen, Component label, Button.OnPress onPress, Vector2i pos, Vector2i size);
    public abstract void createLabel (Screen screen, Component label, Vector2i pos, Vector2i area, Color color, boolean shadow, HorizontalAlignment horizontal, VerticalAlignment vertical);
    public void createLabel(Screen screen, Component label, Vector2i pos, Vector2i area, Color color) {
        this.createLabel(screen, label, pos, area, color, false);
    }
    public void createLabel(Screen screen, Component label, Vector2i pos, Vector2i area, Color color, HorizontalAlignment horizontal, VerticalAlignment vertical) {
        this.createLabel(screen, label, pos, area, color, false, horizontal, vertical);
    }
    public void createLabel(Screen screen, Component label, Vector2i pos, Vector2i area, Color color, boolean shadow) {
        this.createLabel(screen, label, pos, area, color, shadow, HorizontalAlignment.LEFT, VerticalAlignment.TOP);
    }
    public void createObject(Screen screen, String type, Map<String, Object> args) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Type " + type + " is not implemented in this style");
    }
    public void createCustom(Map<String, Object> info) {
        this.objectsData.add(info);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
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
}
