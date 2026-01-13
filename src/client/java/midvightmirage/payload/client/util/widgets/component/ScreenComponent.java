package midvightmirage.payload.client.util.widgets.component;

import net.minecraft.client.gui.GuiGraphics;
import org.joml.Vector2i;

public abstract class ScreenComponent {
    protected Vector2i position;
    protected Vector2i dimensions;
    public ScreenComponent(Vector2i position, Vector2i dimensions) {
        this.position = position;
        this.dimensions = dimensions;
    }

    public ScreenComponent(int x, int y, int width, int height) {
        this(new Vector2i(x, y), new Vector2i(width, height));
    }

    public void setPosition(Vector2i position) {
        this.position = position;
    }

    public void setPosition(int x, int y) {
        this.setPosition(new Vector2i(x, y));
    }

    public void setDimensions(Vector2i dimensions) {
        this.dimensions = dimensions;
    }

    public void setDimensions(int width, int height) {
        this.setDimensions(new Vector2i(width, height));
    }

    public Vector2i getPosition() {
        return position;
    }

    public Vector2i getDimensions() {
        return dimensions;
    }

    public void setX(int x) {
        this.position.x = x;
    }

    public void setY(int y) {
        this.position.y = y;
    }

    public void setWidth(int width) {
        this.dimensions.x = width;
    }

    public void setHeight(int height) {
        this.dimensions.y = height;
    }

    public void changeY(int y) {
        this.position.y += y;
    }

    public abstract void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta);
}
