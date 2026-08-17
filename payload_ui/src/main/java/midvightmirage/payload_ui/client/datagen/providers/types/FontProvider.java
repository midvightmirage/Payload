package midvightmirage.payload_ui.client.datagen.providers.types;

import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import midvightmirage.payload_ui.client.datagen.providers.types.util.FontDataType;
import midvightmirage.payload_ui.client.datagen.providers.types.util.FontProviderApplier;
import midvightmirage.payload_ui.client.util.PayloadUtil;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.data.*;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class FontProvider implements DataProvider {
    private final FabricPackOutput output;

    public FontProvider(FabricPackOutput output) {
        this.output = output;
    }

    public abstract void getFonts(FontProviderApplier applier);

    private static JsonElement toJson(Object value) {
        if (value instanceof Map<?, ?> map) {
            JsonObject object = new JsonObject();

            for (var entry : map.entrySet()) {
                object.add(String.valueOf(entry.getKey()), toJson(entry.getValue()));
            }

            return object;
        }

        if (value instanceof List<?> list) {
            JsonArray array = new JsonArray();

            for (Object element : list) {
                array.add(toJson(element));
            }

            return array;
        }

        if (value instanceof Number number) {
            return new JsonPrimitive(number);
        }

        if (value instanceof Boolean bool) {
            return new JsonPrimitive(bool);
        }

        if (value instanceof Character character) {
            return new JsonPrimitive(character);
        }

        return new JsonPrimitive(String.valueOf(value));
    }

    private static JsonArray toJsonArray(List<?> list) {
        JsonArray array = new JsonArray();

        for (Object value : list) {
            array.add(toJson(value));
        }

        return array;
    }

    @Override
    public @NonNull CompletableFuture<?> run(@NonNull CachedOutput cachedOutput) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        FontProviderApplier applier = new FontProviderApplier();
        getFonts(applier);

        List<Pair<Identifier, FontDataType>> pairs = PayloadUtil.mapToPairs(applier.getApplied());

        for (Pair<Identifier, FontDataType> pair : pairs) {
            Identifier   id   = pair.getFirst();
            FontDataType font = pair.getSecond();

            Path path = output.getOutputFolder()
                    .resolve("assets")
                    .resolve(id.getNamespace())
                    .resolve("font")
                    .resolve(id.getPath() + ".json");

            JsonObject json = new JsonObject();
            json.add("providers", toJsonArray(font.data));

            CompletableFuture<?> future = DataProvider.saveStable(
                    cachedOutput,
                    json,
                    path
            );

            futures.add(future);
        }

        return CompletableFuture.allOf(
                futures.toArray(new CompletableFuture<?>[0])
        );
    }

    @Override
    public @NonNull String getName() {
        return "Font Provider";
    }
}
