package midvightmirage.payload.client.util.widgets.component;

import midvightmirage.payload.client.handler.PackInfo;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.texture.AbstractTexture;
import org.joml.Vector2i;

public class AddonComponent extends ScreenComponent {
    protected AbstractTexture iconTexture;
    private String iconPath;
    private PackInfo.Pack pack;

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

    public PackInfo.Pack getPack() {
        return pack;
    }

    public void setIconTexture(AbstractTexture iconTexture) {
        this.iconTexture = iconTexture;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public void setPack(PackInfo.Pack pack) {
        this.pack = pack;
    }

    @Override
    public void extractWidget(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float delta) {
        guiGraphics.fill(position.x, position.y, dimensions.x + position.x, dimensions.y + position.y, 0xAA343434);
        guiGraphics.text(this.minecraft.font, this.pack.getName(), position.x + (dimensions.x/3), position.y + 12, 0xFFFFFFFF);
    }
}
