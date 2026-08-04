package midvightmirage.payload_ui.client.util.screen;

import midvightmirage.payload_ui.client.util.registry.PayloadRegistries;
import midvightmirage.payload_ui.client.util.registry.ui_style.UIStyle;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class PayloadScreen extends Screen {
    private final UIStyle style;
    private final Screen parent;

    public PayloadScreen(Screen parent, UIStyle style, Component title) {
        super(title);
        this.style = style;
        this.parent = parent;
    }

    public PayloadScreen(Screen parent, Component title) {
        this(parent, PayloadRegistries.getDefault(PayloadRegistries.UI_STYLE), title);
    }

    @Override
    protected final void init() {
        this.init(this.style);
    }

    protected void init(UIStyle style) {}

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);

        this.style.extractRenderState(graphics, mouseX, mouseY, a);
    }

    @Override
    public void onClose() {
        this.minecraft.gui.setScreen(this.parent);
    }
}
