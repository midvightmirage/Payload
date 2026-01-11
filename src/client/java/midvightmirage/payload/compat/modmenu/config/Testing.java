package midvightmirage.payload.compat.modmenu.config;

import net.maksiuhrino.simpleapi.api.util.config.ConfigContent;

public class Testing implements ConfigContent {
    private boolean dependency = false;

    public boolean getDependency() {
        return dependency;
    }

    public void setDependency(boolean dependency) {
        this.dependency = dependency;
    }
}
