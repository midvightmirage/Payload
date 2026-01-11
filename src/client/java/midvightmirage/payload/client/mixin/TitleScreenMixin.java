package midvightmirage.payload.client.mixin;

import midvightmirage.payload.client.mixin.accessor.*;
import midvightmirage.payload.client.util.screens.ModsScreen;
import midvightmirage.payload.client.util.widgets.GuiAddons;
import midvightmirage.payload.compat.modmenu.config.PayloadConfigScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin {
    @Inject(method = "init", at = @At("TAIL"))
    private void injectInit(CallbackInfo ci) {
        List<Renderable> renderables = ((ScreenAccessor) this).payload$getRenderables();
        List<NarratableEntry> narratables = ((ScreenAccessor) this).payload$getNarratables();
        List<GuiEventListener> children = ((ScreenAccessor) this).payload$getChildren();

        int l = ((ScreenAccessor)this).payload$getHeight() / 4 + 48;

        if (Minecraft.getInstance().isDemo()) {
            l += 24;
        }

        SpriteIconButton button = GuiAddons.addons(
                20, btn -> Minecraft.getInstance().setScreen(new ModsScreen(Minecraft.getInstance().screen)), true
        );
        int var10001 = ((ScreenAccessor)this).payload$getWidth() / 2 - 124;
        l += 48;
        button.setPosition(var10001, l);

        renderables.add(button);
        narratables.add(button);
        children.add(button);
    }
}
