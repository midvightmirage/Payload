package net.maksiuhrino.simpleapi.api.util.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import me.shedaniel.autoconfig.util.Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

public record GsonClothConfigSerializer<T extends ConfigContent>(String configName, Class<T> configClass, Gson gson) implements ClothConfigSerializer<T> {
    public GsonClothConfigSerializer(String configName, Class<T> configClass) {
        this(configName, configClass, (new GsonBuilder()).setPrettyPrinting().create());
    }

    private Path getConfigPath() {
        return Utils.getConfigFolder().resolve(this.configName + ".json");
    }

    @Override
    public void serialize(T config) throws ClothSerializationException {
        Path configPath = this.getConfigPath();

        try {
            Files.createDirectories(configPath.getParent());
            BufferedWriter writer = Files.newBufferedWriter(configPath);
            this.gson.toJson(config, writer);
            writer.close();
        } catch (IOException e) {
            throw new ClothSerializationException(e);
        }
    }

    @Override
    public T deserialize() throws ClothSerializationException {
        Path configPath = this.getConfigPath();
        if (Files.exists(configPath, new LinkOption[0])) {
            try {
                BufferedReader reader = Files.newBufferedReader(configPath);
                T ret = (T) (this.gson.fromJson(reader, this.configClass));
                reader.close();
                return ret;
            } catch (JsonParseException | IOException e) {
                throw new ClothSerializationException(e);
            }
        } else {
            return (T) createDefault();
        }
    }

    @Override
    public T createDefault() {
        return (T) (Utils.constructUnsafely(this.configClass));
    }
}
