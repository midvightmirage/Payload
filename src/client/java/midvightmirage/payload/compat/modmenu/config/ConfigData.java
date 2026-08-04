package midvightmirage.payload.compat.modmenu.config;

import net.maksiuhrino.simpleapi.api.util.config.ConfigContent;

public class ConfigData implements ConfigContent {
    private Testing testing = new Testing();

    public Testing getTesting() {
        return testing;
    }

    public void setTesting(Testing testing) {
        this.testing = testing;
    }

    public static class Testing implements ConfigContent {
        private boolean dependency = false;

        public boolean getDependency() {
            return dependency;
        }

        public void setDependency(boolean dependency) {
            this.dependency = dependency;
        }
    }

}
