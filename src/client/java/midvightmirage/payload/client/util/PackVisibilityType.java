package midvightmirage.payload.client.util;

import net.minecraft.resources.Identifier;

public enum PackVisibilityType {
    ICONS_WITH_INFO("layout-list"),
    ICONS("layout-grid"),
    RENDER("menu");

    private final String id;

    PackVisibilityType(String id) {
        this.id = id;
    }

    public Identifier getId() {
        return PayloadIconRegistry.REGISTERED.get(this.id);
    }
}
