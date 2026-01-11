package midvightmirage.payload.client.util.widgets;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class GuiAddons {
    public static SpriteIconButton addons(int i, Button.OnPress onPress, boolean bl) {
        return SpriteIconButton.builder(Component.translatable("options.addons"), onPress, bl)
                .width(i)
                .sprite(Identifier.withDefaultNamespace("icon/language"), 15, 15)
                .build();
    }
}
