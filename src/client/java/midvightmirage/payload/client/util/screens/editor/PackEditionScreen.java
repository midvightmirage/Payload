package midvightmirage.payload.client.util.screens.editor;

import midvightmirage.payload.client.handler.PackInfo;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class PackEditionScreen extends Screen {
    private final PackInfo.Pack pack;
    private final Screen parent;

    public PackEditionScreen(PackInfo.Pack pack, Screen parent) {
        super(Component.translatable("payload.addons.editing", pack.getName()));
        this.pack = pack;
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.centeredText(font, title, width / 2, 10, 0xFFFFFFFF);
    }

    @Override
    public void onClose() {
        minecraft.setScreenAndShow(parent);
    }
}
