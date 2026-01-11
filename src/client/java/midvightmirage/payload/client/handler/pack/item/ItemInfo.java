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
        private float useCooldown;
        private int stacksTo;
        private int durability;
        private String rarity;

        public float getUseCooldown() {
            return useCooldown;
        }
        public int getStacksTo() {
            return stacksTo;
        }
        public int getDurability() {
            return durability;
        }
        public String getRarity() {
            return rarity;
        }

        public void setUseCooldown(float useCooldown) {
            this.useCooldown = useCooldown;
        }
        public void setStacksTo(int stacksTo) {
            this.stacksTo = stacksTo;
        }
        public void setDurability(int durability) {
            this.durability = durability;
        }
        public void setRarity(String rarity) {
            this.rarity = rarity;
        }
    }
}
