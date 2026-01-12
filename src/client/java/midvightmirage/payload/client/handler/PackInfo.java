package midvightmirage.payload.client.handler;

import java.util.Map;

public class PackInfo {
    public PackInfo() {}

    private Pack pack;

    public void setPack(Pack pack) {
        this.pack = pack;
    }

    public Pack getPack() {
        return this.pack;
    }

    public static class Pack {
        private String name;
        private String id;
        private String description;
        private String version;
        private String icon = "";
        private Map<String, String> dependencies;

        public void setName(String name) {
            this.name = name;
        }
        public void setId(String id) {
            this.id = id;
        }
        public void setDescription(String description) {
            this.description = description;
        }
        public void setVersion(String version) {
            this.version = version;
        }
        public void setIcon(String icon) {
            this.icon = icon;
        }
        public void setDependencies(Map<String, String> dependencies) {
            this.dependencies = dependencies;
        }

        public String getName() {
            return this.name;
        }
        public String getId() {
            return id;
        }
        public String getDescription() {
            return description;
        }
        public String getVersion() {
            return version;
        }
        public String getIcon() {
            return icon;
        }
        public Map<String, String> getDependencies() {
            return dependencies;
        }
    }
}
