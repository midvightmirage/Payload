package midvightmirage.payload_ui.client.util;

import com.mojang.datafixers.util.Pair;
import org.joml.Vector2i;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PayloadUtil {
    public static int toARGB(Color color) {
        return (color.getAlpha() << 24)
                | (color.getRed() << 16)
                | (color.getGreen() << 8)
                | color.getBlue();
    }

    public static <K, V> List<Pair<K, V>> mapToPairs(Map<K, V> map) {
        List<Pair<K, V>> pairs = new ArrayList<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            pairs.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        return pairs;
    }

    public static boolean isMouseIn(Vector2i mousePos, Vector2i pos, Vector2i size) {
        return (mousePos.x >= pos.x && mousePos.y >= pos.y && mousePos.x <= pos.x + size.x && mousePos.y <= pos.y + size.y);
    }
}
