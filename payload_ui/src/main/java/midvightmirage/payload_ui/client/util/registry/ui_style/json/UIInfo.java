package midvightmirage.payload_ui.client.util.registry.ui_style.json;

import midvightmirage.payload_ui.client.util.registry.ui_style.json.elements.*;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

public class UIInfo {
    private LabelElement            title = new LabelElement()
            .setType("label")
            .setPos(List.of(0, 0))
            .setArea(List.of(0, 0))
            .setLabel(Component.empty());
    private Theme                   theme;
    private List<? extends Element<?>> elements;

    public LabelElement getTitle() {
        return title;
    }

    public UIInfo setTitle(LabelElement title) {
        this.title = title;
        return this;
    }

    public Theme getTheme() {
        return theme;
    }

    public UIInfo setTheme(Theme theme) {
        this.theme = theme;
        return this;
    }

    public List<? extends Element<?>> getElements() {
        return elements;
    }

    public UIInfo setElements(List<? extends Element<?>> elements) {
        this.elements = elements;
        return this;
    }

    public static class Theme {
        @Nullable private Color backgroundColor = null;

        public @Nullable Color getBackgroundColor() {
            return backgroundColor;
        }

        public Theme setBackgroundColor(@Nullable Color backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }
    }
}
