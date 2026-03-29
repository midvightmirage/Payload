package midvightmirage.payload.client.util.widgets.component;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import midvightmirage.payload.client.handler.PackInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;

public class AddonComponent extends AbstractWidget {
    private String iconPath;
    private PackInfo.Pack pack;
    protected Minecraft minecraft;
    private Path path;

    public AddonComponent(int x, int y, String iconPath, int id) {
        super(x, y, 200, 100, Component.empty());
        this.iconPath = iconPath;
        minecraft = Minecraft.getInstance();
    }

    public PackInfo.Pack getPack() {
        return pack;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public void setPack(PackInfo.Pack pack) {
        this.pack = pack;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    @Override
    public void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        graphics.fill(getX(), getY(), getRight(), getBottom(), 0xAA343434);
        int widthHeight = height - 10;
        if (width < height) {
            widthHeight = width - 10;
        }
        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                Identifier.withDefaultNamespace("textures/misc/unknown_pack.png"),
                getX() + 5,
                getY() + 5,
                0, 0,
                widthHeight,
                widthHeight,
                widthHeight,
                widthHeight
        );
        graphics.text(this.minecraft.font, this.pack.getName(), getX() + widthHeight + 10, getY() + 12, 0xFFFFFFFF);
        graphics.pose().pushMatrix();
        graphics.pose().translate(getX() + widthHeight + 15, getY() + 22);
        graphics.pose().scale(0.75f);
        graphics.text(this.minecraft.font, this.pack.getDescription(), 0, 0, 0xFF8A8A8A);
        graphics.pose().popMatrix();

        if (isHovered()) {
            graphics.requestCursor(CursorTypes.POINTING_HAND);
        }
    }

    @Override
    public void onClick(final @NonNull MouseButtonEvent event, final boolean doubleClick) {
        if (doubleClick) {
            Util.getPlatform().openUri(path.toUri());
        }
    }

    @Override
    protected void updateWidgetNarration(@NonNull NarrationElementOutput output) {}
}
