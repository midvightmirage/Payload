package midvightmirage.payload_ui.client.util;

import net.minecraft.util.Mth;

public class Tweens {
    public static int sineIn(float t, int a, int b) {
        float smoothT = 1f - (float) Math.cos(t * Math.PI * 0.5f);
        return (int)(a + (b - a) * smoothT);
    }

    public static int sineOut(float t, int a, int b) {
        float smoothT = (float) Math.sin(t * Math.PI * 0.5f);
        return (int)(a + (b - a) * smoothT);
    }

    public static int sineInOut(float t, int a, int b) {
        float smoothT = (float) (0.5 - 0.5 * Math.cos(t * Math.PI));
        return (int)(a + (b - a) * smoothT);
    }

    public static int lerpInt(float t, int a, int b) {
        return Mth.lerpInt(t, a, b);
    }
}
