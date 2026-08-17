package midvightmirage.payload_ui.client.util;

import com.mojang.datafixers.util.Pair;

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
}
