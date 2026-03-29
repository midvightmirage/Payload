package midvightmirage.payload.client.util.screens.editor;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class AddonEditorScreen extends Screen {
    private final Screen parent;

    public AddonEditorScreen(Screen parent) {
        super(Component.translatable("payload.addons.editor"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);

        graphics.centeredText(
                this.minecraft.font,
                title,
                width / 2,
                5,
                0xFFFFFFFF
        );
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}
