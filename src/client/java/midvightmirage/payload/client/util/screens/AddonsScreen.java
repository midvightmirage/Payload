package midvightmirage.payload.client.util.screens;

import com.terraformersmc.modmenu.config.ModMenuConfig;
import com.terraformersmc.modmenu.util.ModMenuScreenTexts;
import midvightmirage.payload.client.handler.PackInfo;
import midvightmirage.payload.client.handler.PayloadHandler;
import midvightmirage.payload.client.util.screens.editor.AddonEditorScreen;
import midvightmirage.payload.client.util.widgets.component.AddonComponent;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Map;

public class AddonsScreen extends Screen {
    private final Screen parent;
    private int searchBoxX;
    private int searchBoxWidth;
    private EditBox searchBox;

    public AddonsScreen(Screen parent) {
        super(Component.translatable("payload.addons"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int paneWidth = this.width / 2 - 8;
        int filtersButtonSize = ModMenuConfig.CONFIG_MODE.getValue() ? 0 : 22;
        int searchWidthMax = paneWidth - 32 - filtersButtonSize;
        this.searchBoxWidth = ModMenuConfig.CONFIG_MODE.getValue() ? Math.min(200, searchWidthMax) : searchWidthMax;
        this.searchBoxX = paneWidth / 2 - searchBoxWidth / 2 - filtersButtonSize / 2;
        this.searchBox = new EditBox(this.font, this.searchBoxX, 22, searchBoxWidth, 20, this.searchBox, ModMenuScreenTexts.SEARCH);
        this.addRenderableWidget(searchBox);
        this.addRenderableWidget(Button.builder(Component.translatable("payload.addonsFolder"), (_) ->
            Util.getPlatform().openUri(getAddonsFolder().toUri())
        ).bounds(this.width/2-154,this.height-Button.DEFAULT_HEIGHT-8, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT).build());
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, _ -> this.onClose()).bounds(this.width/2+4, this.height-Button.DEFAULT_HEIGHT-8, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT).build());

        Map<Path, PackInfo> packInfos = PayloadHandler.INSTANCE.getPackInfos();

        int addonCount = Math.min(packInfos.size(), 3);

        for (int i = 0; i < addonCount; i++) {
            AddonComponent addon = new AddonComponent(0, 42, null);
            addon.setSize(this.searchBoxWidth, (int)(this.height / 4.75F));
            addon.setPosition(this.searchBoxX, 52);
            assert !packInfos.isEmpty();
            Path path = (Path) packInfos.keySet().toArray()[i];
            addon.setPath(path);
            PackInfo.Pack pack = packInfos.get(path).getPack();
            addon.setIconPath(pack.getIcon());
            addon.setPack(pack);
            addon.setY(addon.getY() + (((int) (this.height / 4.75F) + 2)) * i);
            this.addRenderableWidget(addon);
        }

        this.addRenderableWidget(
                new Button.Builder(
                        Component.translatable("payload.addons.editor"),
                        _ -> this.minecraft.setScreen(new AddonEditorScreen(this))
                ).build()
        );
    }

    private static Path getAddonsFolder() {
        return FabricLoader.getInstance().getGameDir().resolve("payload/packs");
    }

    @Override
    public void extractRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);

        graphics.centeredText(this.minecraft.font, this.title, this.searchBoxX + (this.searchBoxWidth / 2), 8, -1);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}
