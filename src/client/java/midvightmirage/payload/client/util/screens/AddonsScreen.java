package midvightmirage.payload.client.util.screens;

import com.terraformersmc.modmenu.config.ModMenuConfig;
import com.terraformersmc.modmenu.util.ModMenuScreenTexts;
import midvightmirage.payload.client.handler.PackInfo;
import midvightmirage.payload.client.handler.PayloadHandler;
import midvightmirage.payload.client.util.widgets.component.AddonComponent;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class AddonsScreen extends Screen {
    private final Screen parent;
    private List<Path> packs;
    private int paneWidth;
    private int searchBoxX;
    private int searchBoxWidth;
    private EditBox searchBox;
    private AddonComponent addons = new AddonComponent(0, 42, null);

    public AddonsScreen(Screen parent) {
        super(Component.translatable("payload.addons"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.paneWidth = this.width / 2 - 8;
        int filtersButtonSize = ModMenuConfig.CONFIG_MODE.getValue() ? 0 : 22;
        int searchWidthMax = this.paneWidth - 32 - filtersButtonSize;
        this.searchBoxWidth = ModMenuConfig.CONFIG_MODE.getValue() ? Math.min(200, searchWidthMax) : searchWidthMax;
        this.searchBoxX = this.paneWidth / 2 - searchBoxWidth / 2 - filtersButtonSize / 2;
        this.searchBox = new EditBox(this.font, this.searchBoxX, 22, searchBoxWidth, 20, this.searchBox, ModMenuScreenTexts.SEARCH);
        //this.searchBox.setResponder((text) -> this.modList.filter(text, false));
        this.addRenderableWidget(searchBox);
        this.addRenderableWidget(Button.builder(Component.translatable("payload.addonsFolder"), (button) ->
            Util.getPlatform().openUri(getAddonsFolder().toUri())
        ).bounds(this.width/2-154,this.height-Button.DEFAULT_HEIGHT-8, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT).build());
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose()).bounds(this.width/2+4, this.height-Button.DEFAULT_HEIGHT-8, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT).build());
    }

    private static Path getAddonsFolder() {
        return FabricLoader.getInstance().getGameDir().resolve("payload/packs");
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);

        graphics.drawCenteredString(this.minecraft.font, this.title, this.searchBoxX + (this.searchBoxWidth / 2), 8, -1);

        addons.setDimensions(this.searchBoxWidth, (int)(this.height / 4.75F));

        addons.setPosition(this.searchBoxX, 52);

        Map<Path, PackInfo> packInfos = PayloadHandler.INSTANCE.getPackInfos();

        assert !packInfos.isEmpty();
        addons.setIconPath(packInfos.get(packInfos.keySet().stream().findFirst().get()).getPack().getIcon());

        addons.renderWidget(graphics, mouseX, mouseY, delta);

        int addonCount = Math.min(packInfos.size(), 3);

        for (int i = 0; i < addonCount - 1; i++) {
            addons.changeY((int) (this.height / 4.75F) + 2);

            addons.renderWidget(graphics, mouseX, mouseY, delta);
        }
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}
