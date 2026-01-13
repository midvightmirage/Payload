package midvightmirage.payload.client.util.widgets.component;

import net.minecraft.client.gui.GuiGraphics;
import org.joml.Vector2i;

public class AddonComponent extends ScreenComponent {
    public AddonComponent(Vector2i position, Vector2i dimensions) {
        super(position, dimensions);
    }

    public AddonComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public AddonComponent(int x, int y) {
        super(x, y, 200, 100);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        guiGraphics.fill(position.x, position.y, dimensions.x + position.x, dimensions.y + position.y, 0xAA343434);
    }
}
