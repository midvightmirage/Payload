package midvightmirage.payload_ui.client.datagen.providers.types.util;

import java.util.List;
import java.util.Map;

public abstract class FontDataType {
    public final List<Map<String, Object>> data;

    public FontDataType(List<Map<String, Object>> data) {
        this.data = data;
    }
}
