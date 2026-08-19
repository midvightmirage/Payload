package midvightmirage.payload.client.util.widgets;

import midvightmirage.payload.Payload;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class GuiAddons {
    public static SpriteIconButton addons(int i, Button.OnPress onPress, boolean bl) {
        SpriteIconButton button = SpriteIconButton.builder(Component.translatable("payload.addons"), onPress, bl)
                .width(i)
                .sprite(Payload.id("icon/addons"), 15, 15)
                .build();
        button.active = false;
        button.setTooltip(Tooltip.create(
                Component.translatable("payload.addons")
                        .append("\n")
                        .append(
                                Component.literal("Work in Progress!").
                                        withColor(TextColor.GRAY)
                        )
        ));
        return button;
    }
}
