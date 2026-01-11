package net.maksiuhrino.simpleapi.api.util.config;

import me.shedaniel.autoconfig.util.Utils;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Jankson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

public record JanksonClothConfigSerializer<T extends ConfigContent>(String definitionName, Class<T> configClass, Jankson jankson) implements ClothConfigSerializer<T> {
    public JanksonClothConfigSerializer(String definitionName, Class<T> configClass) {
        this(definitionName, configClass, Jankson.builder().build());
    }

    private Path getConfigPath() {
        return Utils.getConfigFolder().resolve(definitionName + ".json5");
    }

    @Override
    public void serialize(T config) throws ClothSerializationException {
        Path configPath = this.getConfigPath();

        try {
            Files.createDirectories(configPath.getParent());
            BufferedWriter writer = Files.newBufferedWriter(configPath);
            writer.write(this.jankson.toJson(config).toJson(true, true));
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
                return (T) (this.jankson.fromJson(this.jankson.load(this.getConfigPath().toFile()), this.configClass));
            } catch (Throwable e) {
                throw new ClothSerializationException(e);
            }
        } else {
            return (T) this.createDefault();
        }
    }

    @Override
    public T createDefault() {
        return (T) (Utils.constructUnsafely(this.configClass));
    }
}
