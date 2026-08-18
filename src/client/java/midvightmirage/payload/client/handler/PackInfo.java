package midvightmirage.payload.client.handler;

import org.jspecify.annotations.Nullable;

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
        @Nullable private String name;
        private String id = "";
        @Nullable private String description;
        private String version = "";
        @Nullable private String icon;
        @Nullable private String title;
        @Nullable private Map<String, String> dependencies;

        public void setName(@Nullable String name) {
            this.name = name;
        }
        public void setId(String id) {
            this.id = id;
        }
        public void setDescription(@Nullable String description) {
            this.description = description;
        }
        public void setVersion(String version) {
            this.version = version;
        }
        public void setIcon(@Nullable String icon) {
            this.icon = icon;
        }
        public void setTitle(@Nullable String title) {
            this.title = title;
        }
        public void setDependencies(@Nullable Map<String, String> dependencies) {
            this.dependencies = dependencies;
        }

        public @Nullable String getName() {
            return this.name;
        }
        public String getId() {
            return id;
        }
        public @Nullable String getDescription() {
            return description;
        }
        public String getVersion() {
            return version;
        }
        public @Nullable String getIcon() {
            return icon;
        }
        public @Nullable String getTitle() {
            return title;
        }
        public @Nullable Map<String, String> getDependencies() {
            return dependencies;
        }
    }
}
