package midvightmirage.payload.client.handler.pack.block;

public class BlockInfo {
    private String id;
    private boolean withItem;
    private String type;
    private Properties properties = new Properties();

    public String getId() {
        return id;
    }
    public boolean getWithItem() {
        return withItem;
    }
    public String getType() {
        return type;
    }
    public Properties getProperties() {
        return properties;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setWithItem(boolean withItem) {
        this.withItem = withItem;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public static class Properties {
        private String instrument = "harp";
        private boolean noOcclusion = false;
        private String soundType = "minecraft:stone";

        public String getInstrument() {
            return instrument;
        }
        public boolean getNoOcclusion() {
            return noOcclusion;
        }
        public String getSoundType() {
            return soundType;
        }

        public void setInstrument(String instrument) {
            this.instrument = instrument;
        }
        public void setNoOcclusion(boolean noOcclusion) {
            this.noOcclusion = noOcclusion;
        }
        public void setSoundType(String soundType) {
            this.soundType = soundType;
        }
    }
}
