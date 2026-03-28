package midvightmirage.payload.client.handler.pack.item;

public class ItemInfo {
    private String id;
    private Properties properties = new Properties();

    public String getId() {
        return id;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public static class Properties {
        private Float useCooldown;
        private Integer stacksTo;
        private Integer durability;
        private String rarity;

        public Float getUseCooldown() {
            return useCooldown;
        }
        public Integer getStacksTo() {
            return stacksTo;
        }
        public Integer getDurability() {
            return durability;
        }
        public String getRarity() {
            return rarity;
        }

        public void setUseCooldown(Float useCooldown) {
            this.useCooldown = useCooldown;
        }
        public void setStacksTo(Integer stacksTo) {
            this.stacksTo = stacksTo;
        }
        public void setDurability(Integer durability) {
            this.durability = durability;
        }
        public void setRarity(String rarity) {
            this.rarity = rarity;
        }
    }
}
