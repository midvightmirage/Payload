package midvightmirage.payload.client.datagen;

import midvightmirage.payload_ui.client.PayloadUIClient;
import midvightmirage.payload_ui.client.datagen.providers.types.FontProvider;
import midvightmirage.payload_ui.client.datagen.providers.types.util.*;
import midvightmirage.payload_ui.client.util.registry.ui_style.EditorUIStyle;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;

import net.minecraft.network.chat.FontDescription;
import org.joml.Vector2f;

import java.util.List;

public class PayloadFontProvider extends FontProvider {
    public PayloadFontProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void getFonts(FontProviderApplier applier) {
        for (String font : List.of("inter", "space_mono")) {
            for (String weight : List.of("bold", "bold_italic", "italic", "regular")) {
                EditorUIStyle.FontCollection collection = font.equals("inter") ? EditorUIStyle.FontCollection.INTER : EditorUIStyle.FontCollection.SPACE_MONO;
                FontDescription description = collection.getDescription(weight);
                applier.add(
                        description,
                        new TTFFontDataType(
                                PayloadUIClient.id("ttf/" + font + "/" + font + "_" + weight + ".ttf"),
                                new Vector2f(0.5f, 0.5f),
                                10,
                                6
                        )
                );
            }
        }
    }
}
