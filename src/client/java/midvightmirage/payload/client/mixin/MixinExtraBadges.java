package midvightmirage.payload.client.mixin;

import dev.syoritohatsuki.modmenubadgeslib.client.ExtraBadges;
import dev.syoritohatsuki.modmenubadgeslib.client.dto.ExtraBadge;
import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Debug
@Mixin(ExtraBadges.class)
public abstract class MixinExtraBadges {
    @Shadow @Final
    private static Map<String, Set<ExtraBadge>> INTERNAL;

    @Inject(method = "loadInternal", at = @At("TAIL"))
    private static void payload$checkForWIP(CallbackInfo ci) {
        FabricLoader.getInstance().getAllMods().forEach(mod -> {
            var badges = new HashSet<ExtraBadge>();

            var modmenu = mod.getMetadata().getCustomValue("modmenu");

            if (modmenu == null) return;

            var wip = modmenu.getAsObject().get("wip");

            if (wip == null) return;

            if (wip.getAsBoolean()) {
                badges.add(new ExtraBadge("WIP", 0xFFBB0000, 0xFF880000, 0xFFFFFFFF, false));
            }

            if (!badges.isEmpty()) {
                if (INTERNAL.containsKey(mod.getMetadata().getId())) {
                    INTERNAL.get(mod.getMetadata().getId()).addAll(badges);
                } else {
                    INTERNAL.put(mod.getMetadata().getId(), badges);
                }
            }
        });
    }
}
