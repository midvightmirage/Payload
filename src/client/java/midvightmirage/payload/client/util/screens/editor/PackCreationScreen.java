package midvightmirage.payload.client.util.screens.editor;

import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import midvightmirage.payload.client.handler.*;
import midvightmirage.payload.client.util.widgets.SignPopup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static midvightmirage.payload.client.util.PayloadConstants.*;

public class PackCreationScreen extends Screen {
    private final Screen parent;
    private Button createButton;
    private Pair<EditBox, SignPopup> packName;
    private Pair<EditBox, SignPopup> packDescription;
    private EditBox packId;

    public PackCreationScreen(Screen parent) {
        super(Component.translatable("payload.addons.creation"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        int x = this.width/2-154;
        int y = 40;

        var packNameEditBox = new EditBox(
                font,
                x,
                y,
                200,
                20,
                Component.translatable("payload.addons.config.pack_name")
        );

        this.packName = Pair.of(
                packNameEditBox,
                new SignPopup(
                        EXCLAMATION_POPUP,
                        x + 201,
                        y + 6,
                        Component.translatable("util.field_empty", packNameEditBox.getMessage())
                )
        );
        this.packName.getFirst().setCanLoseFocus(true);
        this.packName.getFirst().setHint(REQUIRED);
        this.packName.getSecond().active = false;

        y = 75;

        var packDescriptionEditBox = new EditBox(
                font,
                x,
                y,
                200,
                20,
                Component.translatable("payload.addons.config.pack_desc")
        );

        this.packDescription = Pair.of(
                packDescriptionEditBox,
                new SignPopup(
                        EXCLAMATION_POPUP,
                        x + 201,
                        y + 6,
                        Component.translatable("util.field_empty", packDescriptionEditBox.getMessage())
                )
        );
        this.packDescription.getFirst().setCanLoseFocus(true);
        this.packDescription.getFirst().setHint(REQUIRED);
        this.packDescription.getSecond().active = false;

        y = 110;

        this.packId = new EditBox(
                font,
                x,
                y,
                200,
                20,
                Component.translatable("payload.addons.config.pack_id")
        );
        this.packId.setCanLoseFocus(true);
        this.packId.setHint(Component.translatable("optional", ", " + I18n.get("util.leave_blank")));

        this.createButton = Button.builder(Component.translatable("mco.create.world"), _ -> {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    PackInfo packInfo = new PackInfo();
                    packInfo.setPack(new PackInfo.Pack());
                    packInfo.getPack().setName(this.packName.getFirst().getValue());
                    String id;
                    if (!this.packId.getValue().isEmpty()) {
                        id = this.packId.getValue();
                    } else {
                        id = this.packName.getFirst().getValue().replaceAll(" ", "_")
                                .replaceAll("/", "_")
                                .replaceAll(":", "_")
                                .toLowerCase();
                    }
                    packInfo.getPack().setId(id);
                    packInfo.getPack().setDescription(this.packDescription.getFirst().getValue());
                    String folderName = this.packName.getFirst().getValue().replaceAll(" ", "");
                    try {
                        Path packFolder = FabricLoader.getInstance().getGameDir().resolve("payload/packs").resolve(folderName);
                        if (!Files.exists(packFolder)) {
                            Files.createDirectories(packFolder);
                        }
                        BufferedWriter writer = Files.newBufferedWriter(FabricLoader.getInstance().getGameDir().resolve("payload/packs/" + folderName + "/pack.json"));
                        gson.toJson(packInfo, writer);
                        writer.close();
                        PayloadHandler.INSTANCE.bootstrap();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        ).bounds(
                this.width/2-154,
                this.height-Button.DEFAULT_HEIGHT-Button.DEFAULT_SPACING,
                Button.DEFAULT_WIDTH,
                Button.DEFAULT_HEIGHT
        ).build();

        this.addRenderableWidget(packName.getFirst());
        this.addRenderableWidget(packName.getSecond());
        this.addRenderableWidget(packDescription.getFirst());
        this.addRenderableWidget(packDescription.getSecond());
        this.addRenderableWidget(packId);

        this.addRenderableWidget(createButton);

        createButton.active = false;

        this.addRenderableWidget(
                Button.builder(Component.translatable("gui.cancel"), _ -> this.onClose()
                ).bounds(
                        this.width/2+4,
                        this.height-Button.DEFAULT_HEIGHT-Button.DEFAULT_SPACING,
                        Button.DEFAULT_WIDTH,
                        Button.DEFAULT_HEIGHT
                ).build()
        );
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);

        graphics.centeredText(font, title, width / 2, 5, 0xFFFFFFFF);

        boolean isPackNameGood = (!this.packName.getFirst().getValue().isEmpty());
        boolean isPackDescriptionGood = (!this.packDescription.getFirst().getValue().isEmpty());

        this.createButton.active = isPackNameGood && isPackDescriptionGood;
        this.packName.getSecond().visible = !isPackNameGood;
        this.packDescription.getSecond().visible = !isPackDescriptionGood;

        graphics.text(font, packName.getFirst().getMessage(), width / 2 - 150, 30, 0xFFFFFFFF);
        graphics.text(font, packDescription.getFirst().getMessage(), width / 2 - 150, 65, 0xFFFFFFFF);
        graphics.text(font, packId.getMessage(), width / 2 - 150, 100, 0xFFFFFFFF);
    }

    @Override
    public void onClose() {
        minecraft.setScreenAndShow(parent);
    }
}
