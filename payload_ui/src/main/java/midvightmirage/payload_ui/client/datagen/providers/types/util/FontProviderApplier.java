package midvightmirage.payload_ui.client.datagen.providers.types.util;

import net.minecraft.network.chat.FontDescription;
import net.minecraft.resources.Identifier;

import java.util.*;

public class FontProviderApplier {
    private final Map<Identifier, FontDataType> applied = new LinkedHashMap<>();

    public void add(FontDescription description, FontDataType font) {
        if (description instanceof FontDescription.Resource(Identifier id)) {
            applied.put(id, font);
        } else if (description instanceof FontDescription.AtlasSprite(Identifier atlasId, Identifier _)) {
            applied.put(atlasId, font);
        }
    }

    public Map<Identifier, FontDataType> getApplied() {
        return applied;
    }
}
