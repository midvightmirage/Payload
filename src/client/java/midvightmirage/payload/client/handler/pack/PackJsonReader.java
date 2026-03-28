package midvightmirage.payload.client.handler.pack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.datafixers.util.Pair;
import midvightmirage.payload.Payload;
import midvightmirage.payload.client.handler.PackInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class PackJsonReader<T extends PackJsonReader<T, ?>, E extends PackJsonInfo> {
    protected E objectInfo;
    protected Gson gson;

    private final Class<T> tClass;
    private final Class<E> eClass;

    public PackJsonReader(Class<T> tClass, Class<E> eClass) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.tClass = tClass;
        this.eClass = eClass;
    }

    public abstract void bootstrap(Path pack, PackInfo info);

    public static Pair<Boolean, List<Path>> getItems(Path directory) {
        Pair<Boolean, List<Path>> items = Pair.of(Files.exists(directory), new ArrayList<>());

        if (!Files.exists(directory) || !Files.isDirectory(directory)) {
            return items;
        }

        try (Stream<Path> stream = Files.list(directory)) {
            stream
                    .filter(Files::isRegularFile)
                    .forEach(items.getSecond()::add);
        } catch (IOException e) {
            Payload.LOGGER.error(e.getLocalizedMessage());
        }

        return items;
    }

    public void loadItem(Path path) throws IOException {
        BufferedReader reader = Files.newBufferedReader(path);

        this.objectInfo = this.gson.fromJson(reader, eClass);
    }
}
