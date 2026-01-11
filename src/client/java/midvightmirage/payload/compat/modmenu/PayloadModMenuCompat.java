package midvightmirage.payload.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import midvightmirage.payload.compat.modmenu.config.PayloadConfigScreen;

public class PayloadModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (screen) -> new PayloadConfigScreen().getConfigBuilder().setParentScreen(screen).build();
    }
}
