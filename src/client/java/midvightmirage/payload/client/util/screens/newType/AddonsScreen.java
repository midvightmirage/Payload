package midvightmirage.payload.client.util.screens.newType;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import com.mojang.datafixers.util.Pair;
import midvightmirage.payload.Payload;
import midvightmirage.payload.client.handler.PackInfo;
import midvightmirage.payload.client.handler.PayloadHandler;
import midvightmirage.payload.client.util.PackVisibilityType;
import midvightmirage.payload.client.util.PayloadIconRegistry;
import midvightmirage.payload_ui.client.util.PayloadUtil;
import midvightmirage.payload_ui.client.util.Tweens;
import midvightmirage.payload_ui.client.util.registry.ui_style.EditorUIStyle;
import midvightmirage.payload_ui.client.util.registry.ui_style.UIStyle;
import midvightmirage.payload_ui.client.util.registry.ui_style.UIStyles;
import midvightmirage.payload_ui.client.util.screen.PayloadScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.*;
import net.minecraft.world.item.component.TooltipDisplay;
import org.apache.commons.lang3.tuple.Triple;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.jspecify.annotations.NonNull;

import java.awt.*;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

public class AddonsScreen extends PayloadScreen<EditorUIStyle> {
    public AddonsScreen(Screen parent) {
        super(parent, UIStyles.EDITOR, Component.translatable("payload.addons"));
        this.setBackgroundColor(new Color(76, 76, 76));
    }

    private Vector2i xPos;
    private Vector2i xSize;

    private PackVisibilityType packVisibilityType = PackVisibilityType.ICONS_WITH_INFO;
    private int buttonFrame = 0;

    @Override
    protected void init(EditorUIStyle style) {
        buttonPositions.clear();
        packAreas.clear();
        style.createLabel(
                this,
                null,
                //region "ADDONS"
                () -> Component.literal(this.title.copy().getString().toUpperCase()).withStyle(Style.EMPTY.withBold(true)),
                //endregion
                new Vector2i(20, 12),
                new Vector2i(),
                Color.WHITE,
                new LinkedHashMap<>()
        );
        createTextBox(style);
        createPacksGrid(style, packVisibilityType);
        createBottomButtons(style);
    }

    private final List<Pair<Vector2i, Vector2i>> packAreas = new ArrayList<>();
    private final Map<String, List<Vector2i>> buttonPositions = new LinkedHashMap<>();

    private void createTextBox(EditorUIStyle style) {
        int textBoxWidth  = (int)(this.width/1.75f);
        int textBoxHeight = 24;
        int textBoxX      = (this.width-textBoxWidth)/2;
        int textBoxY      = 5;
        style.createTextBox(
                this,
                null,
                () -> Component.literal("Search"),
                new Vector2i(textBoxX,     textBoxY     ),
                new Vector2i(textBoxWidth, textBoxHeight),
                (searched) -> {

                },
                () -> this.xPressed,
                20
        );
        style.createImage(
                this,
                null,
                PayloadIconRegistry.REGISTERED.get("search"),
                new Vector2i(textBoxX + 4, textBoxY + 4),
                new Vector2i(16, 16),
                Color.WHITE
        );

        this.xPos  = new Vector2i(textBoxX + (textBoxWidth - (16 + 4)), textBoxY + 4);
        this.xSize = new Vector2i(16, 16);

        style.createImage(
                this,
                null,
                PayloadIconRegistry.REGISTERED.get("x"),
                xPos,
                xSize,
                Color.WHITE
        );
    }

    private void createPacksGrid(EditorUIStyle style, PackVisibilityType type) {
        int xMax = switch (type) {
            case ICONS_WITH_INFO -> 3;
            case ICONS -> 10;
            case RENDER -> 7;
        };
        int yMax = 5;

        int packsCount = PayloadHandler.getFolders().size();
        Payload.LOGGER.info("Creating packs grid with {} packs.", packsCount);

        boolean debugMode = false;

        for (int y = 0; y < yMax; y++) {
            if (!debugMode) {
                if (y * xMax >= packsCount) {
                    break;
                }
            }
            for (int x = 0; x < xMax; x++) {
                int id = (y*xMax)+x;
                if (!debugMode) {
                    if (id >= packsCount) {
                        break;
                    }
                }
                this.createPack(style, type, x, y, id);
            }
        }
    }

    private PackInfo.Pack getPackFromId(int id) {
        List<Path> paths = PayloadHandler.getFolders();
        PackInfo info = PayloadHandler.INSTANCE.getPackInfos().get(paths.get(id));
        return info.getPack();
    }

    private void createPack(EditorUIStyle style, PackVisibilityType type, int x, int y, int id) {
        int sx = 2;
        int sy = 2;

        Identifier packImage = Identifier.withDefaultNamespace("textures/misc/unknown_pack.png");

        PackInfo.Pack pack = getPackFromId(id);

        if (packVisibilityType != PackVisibilityType.RENDER) {
            TextureManager textures = minecraft.getTextureManager();

            Identifier iconId = Identifier.fromNamespaceAndPath("payload_packs", pack.getId());

            if (textures.byPath.containsKey(iconId))
                packImage = iconId;
        }

        Vector2i editPos = new Vector2i();
        Vector2i deletePos = new Vector2i();

        switch (type) {
            case ICONS_WITH_INFO -> {
                int rx = ((this.width  - (3 * (125 + sx))) / 2) + x * (125 + sx);
                int ry = ((this.height - (5 * (32  + sy))) / 2) + y * (32  + sy);

                style.createImage(
                        this,
                        null,
                        packImage,
                        new Vector2i(rx, ry),
                        new Vector2i(32, 32),
                        Color.WHITE
                );
                style.createFill(
                        this,
                        null,
                        new Vector2i(rx + 32, ry),
                        new Vector2i(93, 32),
                        new Color(0x666666)
                );

                style.createLabel(
                        this,
                        null,
                        () -> Component.literal(Objects.requireNonNullElse(pack.getName(), "Unknown")).withStyle(Style.EMPTY.withBold(true)),
                        new Vector2i(rx + 34, ry + 3),
                        new Vector2i(),
                        Color.WHITE,
                        new LinkedHashMap<>()
                );
                style.createLabel(
                        this,
                        null,
                        () -> Component.literal(Objects.requireNonNullElse(pack.getDescription(), "")),
                        new Vector2i(rx + 34, ry + 13),
                        new Vector2i(),
                        Color.LIGHT_GRAY,
                        new LinkedHashMap<>() {{
                            put("scale", new Vector2f(0.5f, 0.5f));
                        }}
                );

                packAreas.add(Pair.of(new Vector2i(rx + 32, ry), new Vector2i(125 - 32, 32)));

                editPos = new Vector2i(rx + 97, ry + ((32 - 12)/2) + 4);
                deletePos = new Vector2i(editPos).add(14, 0);
            }
            case ICONS -> {
                int rx = ((this.width  -(10 * (32 + sx))) / 2) + x * (32 + sx);
                int ry = ((this.height -(5  * (32 + sy))) / 2) + y * (32 + sy);

                Vector2i iconSize = new Vector2i(32, 32);

                style.createImage(
                        this,
                        null,
                        packImage,
                        new Vector2i(rx, ry),
                        iconSize,
                        Color.WHITE
                );

                packAreas.add(Pair.of(new Vector2i(rx, ry), iconSize));

                editPos = new Vector2i(rx + 4, ry + ((32 - 12) / 2) + 8);
                deletePos = new Vector2i(editPos).add(14, 0);
            }
            case RENDER -> {
                int rx = ((this.width  -(7 * (48 + sx))) / 2) + x * (48 + sx);
                int ry = ((this.height -(5 * (32 + sy))) / 2) + y * (32 + sy);

                Vector2i iconSize = new Vector2i(48, 32);

                style.createImage(
                        this,
                        null,
                        packImage,
                        new Vector2i(rx, ry),
                        iconSize,
                        Color.WHITE
                );

                packAreas.add(Pair.of(new Vector2i(rx, ry), iconSize));

                editPos = new Vector2i(rx + 20, ry + ((32 - 12) / 2) + 8);
                deletePos = new Vector2i(editPos).add(14, 0);
            }
        }

        if (!buttonPositions.containsKey("edit")) {
            buttonPositions.put("edit", new ArrayList<>());
        }
        if (!buttonPositions.containsKey("delete")) {
            buttonPositions.put("delete", new ArrayList<>());
        }

        buttonPositions.get("edit"  ).add(editPos  );
        buttonPositions.get("delete").add(deletePos);
    }

    private int buttonsX, buttonsY;
    private boolean showChangeStyle = false;

    private void createBottomButtons(EditorUIStyle style) {
        List<Triple<Identifier, MutableComponent, Runnable>> buttons = new ArrayList<>() {{
            add(Triple.of(
                    PayloadIconRegistry.REGISTERED.get("folder"),
                    Component.translatable("payload.addons.packs"),
                    () -> Util.getPlatform().openUri(PayloadHandler.getPayloadPacksFolder().toUri())
            ));
            add(Triple.of(
                    PayloadIconRegistry.REGISTERED.get("server-plus"),
                    Component.translatable("payload.addons.create"),
                    () -> {}
            ));
            add(Triple.of(
                    PayloadIconRegistry.REGISTERED.get("refresh-cw"),
                    Component.translatable("payload.addons.reload"),
                    () -> refresh()
            ));
            add(Triple.of(
                    PayloadIconRegistry.REGISTERED.get("square-arrow-right-exit"),
                    Component.translatable("payload.addons.exit"),
                    AddonsScreen.this::onClose
            ));
        }};

        this.buttonsX = ((this.width  - ((buttons.size()*60) + ((buttons.size() - 1)*2))))/2-13;
        this.buttonsY = (this.height - 30);

        // style button
        style.createButton(
                this,
                null,
                Component::empty,
                () -> showChangeStyle = !showChangeStyle,
                new Vector2i(buttonsX, buttonsY),
                new Vector2i(25, 25),
                0
        );

        for (int i = 0; i < buttons.size(); i++) {
            Triple<Identifier, MutableComponent, Runnable> button = buttons.get(i);

            createButtonWithIcon(
                    style,
                    button.getMiddle(),
                    button.getLeft(),
                    new Vector2i(12, 12),
                    button.getRight(),
                    new Vector2i(
                            buttonsX + 27 + i * 62,
                            buttonsY
                    ),
                    new Vector2i(60, 25)
            );
        }
    }

    private void createButtonWithIcon(EditorUIStyle style, MutableComponent label, Identifier icon, Vector2i iconSize, Runnable runnable, Vector2i pos, Vector2i size) {
        Font font = style.getDefaultFont();

        int width = font.width(label);

        int paddingX = 6;

        style.createButton(
                this,
                null,
                () -> label,
                runnable,
                pos, size,
                (iconSize.x+2 + paddingX)-((size.x-width)/2)
        );

        style.createImage(
                this,
                null,
                icon,
                new Vector2i(pos.x + paddingX, pos.y + (size.y-iconSize.y)/2),
                iconSize,
                Color.WHITE
        );
    }

    private int currentlyHovered;
    private int editHoldFrame;
    private int deleteHoldFrame;

    private boolean onChanger = false;
    private int changerFrame;
    private Vector2i changerPos;
    private Vector2i changerSize;

    private final List<Pair<Vector2i, Vector2i>> changerButtons = new ArrayList<>();

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);

        Vector2i mousePos = new Vector2i(mouseX, mouseY);

        if (PayloadUtil.isMouseIn(mousePos, this.xPos, this.xSize)) {
            requestWithFill(graphics, xPos, xSize);
        }

        extractEditAndDelete(graphics, mousePos);

        extractPackVisibilityType(graphics, mousePos);

        if (currentlyHovered >= 0) {
            Pair<Vector2i, Vector2i> area = packAreas.get(currentlyHovered);

            if (PayloadUtil.isMouseIn(mousePos, area.getFirst(), area.getSecond())) {
                graphics.requestCursor(CursorTypes.POINTING_HAND);

                if (!(mouseInEdit || mouseInDelete)) {
                    if (packVisibilityType != PackVisibilityType.ICONS_WITH_INFO) {
                        PackInfo.Pack pack = getPackFromId(currentlyHovered);

                        graphics.tooltip(
                                EditorUIStyle.getFont(),
                                List.of(
                                        toTooltip(
                                                EditorUIStyle.getWithStyle(
                                                        Component.literal(
                                                                Objects.requireNonNullElse(pack.getName(), "Unknown")
                                                        ).setStyle(Style.EMPTY.withBold(true)),
                                                        false
                                                )
                                        ),
                                        toTooltip(
                                                EditorUIStyle.getWithStyle(
                                                        Component.literal(
                                                                Objects.requireNonNullElse(pack.getDescription(), "")
                                                        ).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)),
                                                        false
                                                )
                                        )
                                ),
                                mouseX,
                                mouseY,
                                DefaultTooltipPositioner.INSTANCE,
                                null
                        );
                    }
                }
            }
        }
    }

    private boolean mouseInEdit;
    private boolean mouseInDelete;

    private ClientTooltipComponent toTooltip(MutableComponent component) {
        return ClientTooltipComponent.create(component.getVisualOrderText());
    }

    private void extractPackVisibilityType(GuiGraphicsExtractor graphics, Vector2i mousePos) {
        changerButtons.clear();

        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                packVisibilityType.getId(),
                this.buttonsX + 3,
                this.buttonsY + 3,
                0, 0,
                19, 19,
                19, 19
        );

        onChanger = showChangeStyle;

        int direction = showChangeStyle ? 1 : -1;

        changerFrame += direction;

        int animLength = 25;

        changerFrame = Math.min(changerFrame, animLength);
        changerFrame = Math.max(changerFrame, 0);

        if (changerFrame == 0) return;

        float floatedChangerFrame = this.changerFrame / (float)animLength;

        int maxHeight = 108;

        int height = direction > 0 ? Tweens.sineOut(floatedChangerFrame, 0, maxHeight)
                : Tweens.sineIn(floatedChangerFrame, 0, maxHeight);

        this.changerPos  = new Vector2i(this.buttonsX, this.buttonsY - height);
        this.changerSize = new Vector2i(100,         height                  );

        graphics.enableScissor(
                changerPos.x, changerPos.y,
                changerPos.x+changerSize.x, changerPos.y+changerSize.y
        );

        graphics.fill(
                changerPos.x, changerPos.y,
                changerPos.x+changerSize.x, changerPos.y+changerSize.y,
                0xFF666666
        );

        List<PackVisibilityType> values = Arrays.asList(PackVisibilityType.values());

        for (int i = 0; i < values.size(); i++) {
            PackVisibilityType value = values.get(i);

            int sizeY = 32;

            Vector2i pos = new Vector2i(
                    changerPos.x + 2,
                    changerPos.y + 2 + ((sizeY + 2) * i)
            );

            Vector2i size = new Vector2i(100 - 4, sizeY);

            changerButtons.add(Pair.of(pos, size));

            boolean hovering = PayloadUtil.isMouseIn(mousePos, pos, size);

            int color = hovering ? 0xFF777777 : 0xFF6A6A6A;

            graphics.fill(
                    pos.x, pos.y,
                    pos.x + size.x,
                    pos.y + size.y,
                    color
            );

            Font font = EditorUIStyle.getFont();

            graphics.blit(
                    RenderPipelines.GUI_TEXTURED,
                    value.getId(),
                    pos.x + 2,
                    pos.y + 8,
                    0, 0,
                    16, 16,
                    16, 16
            );

            graphics.text(
                    font,
                    EditorUIStyle.getWithStyle(
                            Component.translatable(value.getSerializedName()),
                            false
                    ),
                    pos.x+20,
                    pos.y + ((size.y-font.lineHeight)/2),
                    0xFFFFFFFF,
                    false
            );

            if (packVisibilityType == value) {
                graphics.blit(
                        RenderPipelines.GUI_TEXTURED,
                        PayloadIconRegistry.REGISTERED.get("check"),
                        (pos.x + size.x) - 18,
                        pos.y + 8,
                        0, 0,
                        16, 16,
                        16, 16
                );
            } else {
                if (PayloadUtil.isMouseIn(mousePos, pos, size)) {
                    graphics.requestCursor(CursorTypes.POINTING_HAND);
                }
            }
        }

        graphics.disableScissor();
    }

    private void extractEditAndDelete(GuiGraphicsExtractor graphics, Vector2i mousePos) {
        for (int i = 0; i < packAreas.size(); i++) {
            Pair<Vector2i, Vector2i> pair = packAreas.get(i);
            if (onChanger) return;
            if (PayloadUtil.isMouseIn(mousePos, pair.getFirst(), pair.getSecond())) {
                if (i != currentlyHovered) {
                    buttonFrame = 0;
                }
                currentlyHovered = i;

                Vector2i editPos   = new Vector2i(buttonPositions.get("edit"  ).get(i));
                Vector2i deletePos = new Vector2i(buttonPositions.get("delete").get(i)).sub(0, 1);

                Vector2i iconSizes = new Vector2i(12, 12);

                mouseInEdit = PayloadUtil.isMouseIn(mousePos, editPos, iconSizes);
                mouseInDelete = PayloadUtil.isMouseIn(mousePos, deletePos, iconSizes);

                float time = buttonFrame/10f;

                int lerpedY = Tweens.sineOut(time, 5, 0);

                editPos  .add(0, lerpedY);
                deletePos.add(0, lerpedY);

                editPos  .sub(0, (int)(editHoldFrame   / 2.5f));
                deletePos.sub(0, (int)(deleteHoldFrame / 2.5f));

                int alpha = (int)((5 - lerpedY) * (255f/5f));

                graphics.blit(
                        RenderPipelines.GUI_TEXTURED,
                        PayloadIconRegistry.REGISTERED.get("square-pen"),
                        editPos.x,
                        editPos.y,
                        0, 0,
                        12, 12,
                        12, 12,
                        PayloadUtil.toARGB(new Color(255, 255, 255, alpha))
                );
                graphics.blit(
                        RenderPipelines.GUI_TEXTURED,
                        PayloadIconRegistry.REGISTERED.get("trash-2"),
                        deletePos.x,
                        deletePos.y,
                        0, 0,
                        12, 12,
                        12, 12,
                        PayloadUtil.toARGB(new Color(255, 255, 255, alpha))
                );

                if (mouseInEdit) {
                    requestWithFill(graphics, editPos, new Vector2i(12, 12), editHoldFrame/5f);
                    editHoldFrame++;
                } else {
                    editHoldFrame--;
                }

                editHoldFrame = Math.min(editHoldFrame, 5);
                editHoldFrame = Math.max(editHoldFrame, 0);

                if (mouseInDelete) {
                    requestWithFill(graphics, deletePos, new Vector2i(12, 12), deleteHoldFrame/5f);
                    deleteHoldFrame++;
                } else {
                    deleteHoldFrame--;
                }

                deleteHoldFrame = Math.min(deleteHoldFrame, 5);
                deleteHoldFrame = Math.max(deleteHoldFrame, 0);

                buttonFrame++;
                buttonFrame = Math.min(buttonFrame, 10);
                buttonFrame = Math.max(buttonFrame, 0 );

                break;
            } else {
                if (i == packAreas.size() - 1) {
                    currentlyHovered = -1;
                }
            }
        }
    }

    private void requestWithFill(GuiGraphicsExtractor graphics, Vector2i pos, Vector2i size, float lerp) {
        graphics.requestCursor(CursorTypes.POINTING_HAND);

        int thickness = 2;
        Vector2i newPos = new Vector2i(pos).add(0, size.y);

        int color1 = PayloadUtil.toARGB(new Color(100, 100, 100, Tweens.sineOut(lerp, 0, 63)));
        int color2 = PayloadUtil.toARGB(new Color(100, 100, 100, Tweens.sineOut(lerp, 0, 127)));

        int half = size.x / 2;

        graphics.enableScissor(newPos.x, newPos.y, newPos.x + half, newPos.y + thickness);

        graphics.pose().pushMatrix();

        graphics.pose().translate(newPos.x, newPos.y);
        graphics.pose().rotate((float) Math.toRadians(-90));

        graphics.fillGradient(
                -thickness, 0,
                0, half,
                color1,
                color2
        );

        graphics.pose().popMatrix();

        graphics.disableScissor();

        graphics.enableScissor(newPos.x + half, newPos.y, newPos.x + size.x, newPos.y + thickness);

        graphics.pose().pushMatrix();

        graphics.pose().translate(newPos.x + half, newPos.y);
        graphics.pose().rotate((float) Math.toRadians(-90));

        graphics.fillGradient(
                -thickness, 0,
                0, half,
                color2,
                color1
        );

        graphics.pose().popMatrix();

        graphics.disableScissor();
    }

    private void requestWithFill(GuiGraphicsExtractor graphics, Vector2i pos, Vector2i size) {
        requestWithFill(graphics, pos, size, 1);
    }

    private void refresh() {
        init();
    }


    private boolean xPressed = false;

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        Vector2i mousePos = new Vector2i((int)event.x(), (int)event.y());
        SoundManager sound = Minecraft.getInstance().getSoundManager();

        if (showChangeStyle) {
            if (PayloadUtil.isMouseIn(mousePos, this.changerPos, this.changerSize)) {
                for (int i = 0; i < changerButtons.size(); i++) {
                    PackVisibilityType type = PackVisibilityType.values()[i];
                    Pair<Vector2i, Vector2i> pos = changerButtons.get(i);

                    if (PayloadUtil.isMouseIn(mousePos, pos.getFirst(), pos.getSecond())) {
                        if (packVisibilityType != type) {
                            packVisibilityType = type;

                            refresh();
                            playDownSound(sound);

                            return true;
                        }
                    }
                }
            } else {
                showChangeStyle = false;
                return true;
            }

            return super.mouseClicked(event, doubleClick);
        }

        if (PayloadUtil.isMouseIn(mousePos, this.xPos, this.xSize)) {
            this.xPressed = true;
            return true;
        }

        if (currentlyHovered >= 0) {
            Vector2i editPos   = buttonPositions.get("edit"  ).get(currentlyHovered);
            Vector2i deletePos = buttonPositions.get("delete").get(currentlyHovered);

            Vector2i iconsSize = new Vector2i(12, 12);

            Pair<Vector2i, Vector2i> packArea = packAreas.get(currentlyHovered);

            if (PayloadUtil.isMouseIn(mousePos, editPos, iconsSize)) {
                this.playDownSound(sound);
                Payload.LOGGER.info("Pressed on edit of pack no. {}.", currentlyHovered + 1);
                return true;
            } else if (PayloadUtil.isMouseIn(mousePos, deletePos, iconsSize)) {
                this.playDownSound(sound);
                Payload.LOGGER.info("Pressed on delete of pack no. {}.", currentlyHovered + 1);
                return true;
            } else if (PayloadUtil.isMouseIn(mousePos, packArea.getFirst(), packArea.getSecond())) {
                this.playDownSound(sound);
                List<Path> folders = PayloadHandler.getFolders();
                if (currentlyHovered < folders.size()) {
                    Util.getPlatform().openUri(folders.get(currentlyHovered).toUri());
                    return true;
                }
            }
        }

        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    public void playDownSound(final SoundManager soundManager) {
        playButtonClickSound(soundManager);
    }

    public static void playButtonClickSound(final SoundManager soundManager) {
        soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if (PayloadUtil.isMouseIn(new Vector2i((int)event.x(), (int)event.y()), this.xPos, this.xSize)) {
            this.xPressed = false;
            return true;
        }

        return super.mouseReleased(event);
    }
}
