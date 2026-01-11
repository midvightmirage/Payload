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
}
