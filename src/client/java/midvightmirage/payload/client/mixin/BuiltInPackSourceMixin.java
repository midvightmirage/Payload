package midvightmirage.payload.client.mixin;

import midvightmirage.payload.client.handler.PayloadPackResources;
import net.minecraft.client.resources.ClientPackSource;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.Pack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(BuiltInPackSource.class)
public class BuiltInPackSourceMixin {
    @Inject(
            method = "listBundledPacks",
            at = @At("TAIL")
    )
    private void payload$registerPackResources(Consumer<Pack> packConsumer, CallbackInfo ci) {
        if(!((BuiltInPackSource) (Object) this instanceof ClientPackSource)) return;
        packConsumer.accept(
                PayloadPackResources.createPack(
                        PayloadPackResources.createPackLocationInfo("example", "Example Pack"),
                        "ExamplePack",
                        PayloadPackResources.createPackMetadata("Just an example pack")
                )
        );
    }
}
