package midvightmirage.payload.client.util.screens.editor;

import com.terraformersmc.modmenu.config.ModMenuConfig;
import midvightmirage.payload.client.handler.PackInfo;
import midvightmirage.payload.client.handler.PayloadHandler;
import midvightmirage.payload.client.util.widgets.component.AddonComponent;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Map;

public class AddonEditorScreen extends Screen {
    private final Screen parent;

    public AddonEditorScreen(Screen parent) {
        super(Component.translatable("payload.addons.editor"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(
                Button.builder(Component.translatable("payload.addons.creation"), _ -> this.minecraft.setScreenAndShow(new PackCreationScreen(this))
                ).bounds(
                        this.width/2 - 154,
                        this.height - Button.DEFAULT_HEIGHT - 8,
                        Button.DEFAULT_WIDTH,
                        Button.DEFAULT_HEIGHT
                ).build()
        );
        this.addRenderableWidget(
                Button.builder(CommonComponents.GUI_DONE, _ -> this.onClose()
                ).bounds(
                        this.width/2+4,
                        this.height-Button.DEFAULT_HEIGHT-8,
                        Button.DEFAULT_WIDTH,
                        Button.DEFAULT_HEIGHT
                ).build()
        );

        Map<Path, PackInfo> packInfos = PayloadHandler.INSTANCE.getPackInfos();

        int addonCount = Math.min(packInfos.size(), 3);

        int paneWidth = this.width / 2 - 8;
        int filtersButtonSize = ModMenuConfig.CONFIG_MODE.getValue() ? 0 : 22;
        int searchWidthMax = paneWidth - 32 - filtersButtonSize;
        int searchBoxWidth = ModMenuConfig.CONFIG_MODE.getValue() ? Math.min(200, searchWidthMax) : searchWidthMax;
        int searchBoxX = paneWidth / 2 - searchBoxWidth / 2 - filtersButtonSize / 2;

        for (int i = 0; i < addonCount; i++) {
            AddonComponent addon = new AddonComponent(0, 42, null);
            addon.setSize(searchBoxWidth, (int)(this.height / 4.75F));
            addon.setPosition(searchBoxX, 52);
            assert !packInfos.isEmpty();
            Path path = (Path) packInfos.keySet().toArray()[i];
            addon.setPath(path);
            PackInfo.Pack pack = packInfos.get(path).getPack();
            addon.setIconPath(pack.getIcon());
            addon.setPack(pack);
            addon.setY(addon.getY() + (((int) (this.height / 4.75F) + 2)) * i);
            addon.setInEditorScreen(true);
            this.addRenderableWidget(addon);
        }
    }

    @Override
    public void extractRenderState(@Nullable GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        if (graphics == null) return;

        super.extractRenderState(graphics, mouseX, mouseY, a);

        graphics.centeredText(
                this.minecraft.font,
                title,
                width / 2,
                8,
                0xFFFFFFFF
        );
    }

    @Override
    public void onClose() {
        this.minecraft.setScreenAndShow(this.parent);
    }
}
