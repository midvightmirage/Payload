package midvightmirage.payload_ui.client.datagen.providers.types.util;

import net.minecraft.resources.Identifier;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TTFFontDataType extends FontDataType {
    public TTFFontDataType(Identifier filePath, Vector2f shift, float size, float oversample) {
        super(new ArrayList<>() {{
            add(new HashMap<>() {{
                put("type",      "ttf");
                put("file",       filePath.toString());
                put("shift",      List.of(shift.x, shift.y));
                put("size",       size);
                put("oversample", oversample);
            }});
        }});
    }
}
