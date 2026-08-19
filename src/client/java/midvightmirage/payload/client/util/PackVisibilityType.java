package midvightmirage.payload.client.util;

import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;

public enum PackVisibilityType implements StringRepresentable {
    ICONS_WITH_INFO("payload.addons.visibility.icons_with_info", "layout-list"),
    ICONS("payload.addons.visibility.icons", "layout-grid"),
    RENDER("payload.addons.visibility.render", "menu");

    private final String name;
    private final String id;

    PackVisibilityType(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public Identifier getId() {
        return PayloadIconRegistry.REGISTERED.get(this.id);
    }

    @Override
    public @NonNull String getSerializedName() {
        return name;
    }
}
