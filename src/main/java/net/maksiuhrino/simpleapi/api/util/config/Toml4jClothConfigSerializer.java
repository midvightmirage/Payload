package net.maksiuhrino.simpleapi.api.util.config;

import me.shedaniel.autoconfig.util.Utils;
import me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml.Toml;
import me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml.TomlWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

public record Toml4jClothConfigSerializer<T extends ConfigContent>(String definitionName, Class<T> configClass, TomlWriter tomlWriter) implements ClothConfigSerializer<T> {
    public Toml4jClothConfigSerializer(String definitionName, Class<T> configClass) {
        this(definitionName, configClass, new TomlWriter());
    }

    private Path getConfigPath() {
        return Utils.getConfigFolder().resolve(definitionName + ".toml");
    }

    @Override
    public void serialize(T config) throws ClothSerializationException {
        Path configPath = this.getConfigPath();

        try {
            Files.createDirectories(configPath.getParent());
            this.tomlWriter.write(config, configPath.toFile());
        } catch (IOException e) {
            throw new ClothSerializationException(e);
        }
    }

    @Override
    public T deserialize() throws ClothSerializationException {
        Path configPath = this.getConfigPath();
        if (Files.exists(configPath, new LinkOption[0])) {
            try {
                return (T) ((new Toml()).read(configPath.toFile()).to(this.configClass));
            } catch (IllegalStateException e) {
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
