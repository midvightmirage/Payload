package midvightmirage.payload.client.handler;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.flag.FeatureFlagSet;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class PayloadPackResources extends PathPackResources {
    public static PackLocationInfo LOCATION = new PackLocationInfo(
            "example",
            Component.literal("example"),
            PackSource.BUILT_IN,
            Optional.empty()
    );
    public static Pack.Metadata METADATA = new Pack.Metadata(
            Component.literal("Just an example pack"),
            PackCompatibility.COMPATIBLE,
            FeatureFlagSet.of(),
            List.of()
    );
    public static PackSelectionConfig SELECTION_CONFIG = new PackSelectionConfig(
            true,
            Pack.Position.TOP,
            false
    );

    PayloadPackResources(PackLocationInfo location, String pack) {
        super(location, FabricLoader.getInstance().getGameDir().resolve("payload/packs/").resolve(pack));
    }

    public static Pack.ResourcesSupplier createResourcesSupplier() {
        return new Pack.ResourcesSupplier() {
            @Override
            public @NonNull PackResources openPrimary(@NonNull PackLocationInfo location) {
                return new PayloadPackResources(location, "ExamplePack");
            }

            @Override
            public @NonNull PackResources openFull(@NonNull PackLocationInfo location, Pack.@NonNull Metadata metadata) {
                return new PayloadPackResources(location, "ExamplePack");
            }
        };
    }

    public static Pack createPack() {
        return new Pack(
                LOCATION,
                createResourcesSupplier(),
                METADATA,
                SELECTION_CONFIG
        );
    }
}
