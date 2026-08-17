package midvightmirage.payload_ui.client.util.screen;

import midvightmirage.payload_ui.client.util.PayloadUtil;
import midvightmirage.payload_ui.client.util.registry.ui_style.UIStyle;
import midvightmirage.payload_ui.client.util.registry.ui_style.UIStyles;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

import java.awt.*;

public abstract class PayloadScreen<T extends UIStyle> extends Screen {
    private final T style;
    private final Screen parent;
    private Color backgroundColor = null;

    public PayloadScreen(Screen parent, T style, Component title) {
        super(title);
        this.style = style;
        this.parent = parent;
    }

    @SuppressWarnings("unchecked")
    public PayloadScreen(Screen parent, Component title) {
        this(parent, (T)UIStyles.getDefault().getSecond(), title);
    }

    @Override
    protected final void init() {
        this.style.clear();
        this.init(this.style);
    }

    protected void init(T style) {}

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);

        if (this.backgroundColor != null) {
            graphics.fill(
                    0,
                    0,
                    graphics.guiWidth(),
                    graphics.guiHeight(),
                    PayloadUtil.toARGB(this.backgroundColor)
            );
        }
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);

        this.style.extractRenderState(graphics, mouseX, mouseY, a);
    }

    @Override
    public void onClose() {
        this.minecraft.gui.setScreen(this.parent);
    }

    protected void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
