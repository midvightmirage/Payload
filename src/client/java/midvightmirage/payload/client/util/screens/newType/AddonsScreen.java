package midvightmirage.payload.client.util.screens.newType;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import midvightmirage.payload.client.util.PayloadIconRegistry;
import midvightmirage.payload_ui.client.util.PayloadUtil;
import midvightmirage.payload_ui.client.util.registry.ui_style.EditorUIStyle;
import midvightmirage.payload_ui.client.util.registry.ui_style.UIStyle;
import midvightmirage.payload_ui.client.util.registry.ui_style.UIStyles;
import midvightmirage.payload_ui.client.util.screen.PayloadScreen;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.joml.Vector2i;

import java.awt.*;

public class AddonsScreen extends PayloadScreen<EditorUIStyle> {
    public AddonsScreen(Screen parent) {
        super(parent, UIStyles.EDITOR, Component.translatable("payload.addons"));
        this.setBackgroundColor(new Color(76, 76, 76));
    }

    private Vector2i xPos;
    private Vector2i xSize;

    @Override
    protected void init(EditorUIStyle style) {
        style.createLabel(
                this,
                null,
                //region "ADDONS"
                () -> Component.literal(this.title.copy().getString().toUpperCase()).withStyle(Style.EMPTY.withBold(true)),
                //endregion
                new Vector2i(20, 15),
                new Vector2i(),
                Color.WHITE
        );
        createTextBox(style);
    }

    private void createTextBox(UIStyle style) {
        int textBoxWidth  = (int)(this.width/1.75f);
        int textBoxHeight = 24;
        int textBoxX      = (this.width-textBoxWidth)/2;
        int textBoxY      = 20;
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

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);

        if (PayloadUtil.isMouseIn(new Vector2i(mouseX, mouseY), this.xPos, this.xSize)) {
            graphics.requestCursor(CursorTypes.POINTING_HAND);

            graphics.fill(
                    xPos.x,
                    xPos.y,
                    xPos.x + xSize.x,
                    xPos.y + xSize.y,
                    0x7F7F7F7F
            );
        }
    }


    private boolean xPressed = false;

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (PayloadUtil.isMouseIn(new Vector2i((int)event.x(), (int)event.y()), this.xPos, this.xSize)) {
            this.xPressed = true;
            return true;
        }

        return super.mouseClicked(event, doubleClick);
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
