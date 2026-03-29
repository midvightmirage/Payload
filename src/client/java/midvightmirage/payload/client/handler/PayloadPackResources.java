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

import java.util.List;
import java.util.Optional;

public class PayloadPackResources extends PathPackResources {
    public static PackSelectionConfig SELECTION_CONFIG = new PackSelectionConfig(
            true,
            Pack.Position.TOP,
            false
    );

    PayloadPackResources(PackLocationInfo location, String pack) {
        super(location, FabricLoader.getInstance().getGameDir().resolve("payload/packs/").resolve(pack));
    }

    public static Pack.ResourcesSupplier createResourcesSupplier(String folder) {
        return new Pack.ResourcesSupplier() {
            @Override
            public @NonNull PackResources openPrimary(@NonNull PackLocationInfo location) {
                return new PayloadPackResources(location, folder);
            }

            @Override
            public @NonNull PackResources openFull(@NonNull PackLocationInfo location, Pack.@NonNull Metadata metadata) {
                return new PayloadPackResources(location, folder);
            }
        };
    }

    public static Pack createPack(PackLocationInfo location, String folder, Pack.Metadata metadata) {
        return new Pack(
                location,
                createResourcesSupplier(folder),
                metadata,
                SELECTION_CONFIG
        );
    }

    public static PackLocationInfo createPackLocationInfo(String id, String name) {
        return new PackLocationInfo(
                id,
                Component.literal(name),
                PackSource.BUILT_IN,
                Optional.empty()
        );
    }

    public static Pack.Metadata createPackMetadata(String description) {
        return new Pack.Metadata(
                Component.literal(description),
                PackCompatibility.COMPATIBLE,
                FeatureFlagSet.of(),
                List.of()
        );
    }
}
