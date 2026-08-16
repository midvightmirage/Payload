package midvightmirage.payload_ui.client.compat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Jankson;

import java.nio.file.*;

public final class JanksonToGson {

    public static final Jankson JANKSON = Jankson.builder().build();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static <T> T load(Path path, Class<T> clazz) throws Exception {
        return GSON.fromJson(JANKSON.load(Files.readString(path)).toJson(), clazz);
    }
}
