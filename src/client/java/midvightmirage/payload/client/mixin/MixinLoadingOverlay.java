package midvightmirage.payload.client.mixin;

import midvightmirage.payload.client.handler.PayloadHandler;
import midvightmirage.payload.client.util.PayloadIconRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.server.packs.resources.ReloadInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(LoadingOverlay.class)
public class MixinLoadingOverlay {
    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void payload$atLoadingOverlayInit(Minecraft minecraft, ReloadInstance reload, Consumer<Optional<Throwable>> onFinish, boolean fadeIn, CallbackInfo ci) {
        PayloadIconRegistry.bootstrap();
        for (Runnable runnable : PayloadHandler.afterInit) {
            runnable.run();
        }
    }
}
