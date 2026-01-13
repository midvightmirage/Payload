package midvightmirage.payload.client.util.widgets.component;

import com.mojang.blaze3d.textures.GpuTexture;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.AbstractTexture;
import org.joml.Vector2i;

public class AddonComponent extends ScreenComponent {
    protected AbstractTexture iconTexture;
    private String iconPath;

    public AddonComponent(Vector2i position, Vector2i dimensions, String iconPath) {
        super(position, dimensions);
        this.iconPath = iconPath;
    }

    public AddonComponent(int x, int y, int width, int height, String iconPath) {
        super(x, y, width, height);
    }

    public AddonComponent(int x, int y, String iconPath) {
        super(x, y, 200, 100);
    }

    public AbstractTexture getIconTexture() {
        return iconTexture;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconTexture(AbstractTexture iconTexture) {
        this.iconTexture = iconTexture;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        guiGraphics.fill(position.x, position.y, dimensions.x + position.x, dimensions.y + position.y, 0xAA343434);
    }
}
