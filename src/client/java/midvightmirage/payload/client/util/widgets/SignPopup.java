package midvightmirage.payload.client.util.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

public class SignPopup extends AbstractWidget {
    private final Identifier texture;

    public SignPopup(Identifier texture, int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
        this.texture = texture;
    }

    public SignPopup(Identifier texture, int x, int y, Component message) {
        this(texture, x, y, 8, 8, message);
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                texture,
                getX(), getY(),
                width, height
        );

        Minecraft minecraft = Minecraft.getInstance();

        if (isHoveredOrFocused()) {
            graphics.text(minecraft.font, message, getX() + 10, getY(), 0xFFFFFFFF);
        }
    }

    @Override
    protected void updateWidgetNarration(@Nullable NarrationElementOutput output) {

    }
}
