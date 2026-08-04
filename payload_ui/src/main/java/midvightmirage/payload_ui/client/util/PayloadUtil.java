package midvightmirage.payload_ui.client.util;

import java.awt.*;

public class PayloadUtil {
    public static int toARGB(Color color) {
        return (color.getAlpha() << 24)
                | (color.getRed() << 16)
                | (color.getGreen() << 8)
                | color.getBlue();
    }
}
