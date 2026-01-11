package net.maksiuhrino.simpleapi.api.util.config;

public interface ClothConfigSerializer<T extends ConfigContent> {
    void serialize(T var1) throws ClothSerializationException;

    T deserialize() throws ClothSerializationException;

    T createDefault();

    public static class ClothSerializationException extends Exception {
        public ClothSerializationException(Throwable cause) {
            super(cause);
        }
    }

    @FunctionalInterface
    public interface Factory<T extends ConfigContent> {
        ClothConfigSerializer<T> create(String var1, Class<T> var2);
    }
}
