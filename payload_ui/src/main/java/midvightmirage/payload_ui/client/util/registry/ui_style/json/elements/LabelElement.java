package midvightmirage.payload_ui.client.util.registry.ui_style.json.elements;

import net.minecraft.network.chat.Component;

import java.util.List;

public class LabelElement extends Element<LabelElement> {
    protected Component label;
    protected List<Object> pos = List.of(0, 0);
    protected List<Object> area = List.of(0, 0);
    protected Alignment alignment;

    public Component getLabel() {
        return label;
    }

    public LabelElement setLabel(Component label) {
        this.label = label;
        return this;
    }

    public List<Object> getPos() {
        return pos;
    }

    public LabelElement setPos(List<Object> pos) {
        this.pos = pos;
        return this;
    }

    public List<Object> getArea() {
        return area;
    }

    public LabelElement setArea(List<Object> area) {
        this.area = area;
        return this;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public LabelElement setAlignment(Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public LabelElement() {
        this.type = "label";
    }

    public static class Alignment {
        private String horizontal;
        private String vertical;

        public String getHorizontal() {
            return horizontal;
        }

        public Alignment setHorizontal(String horizontal) {
            this.horizontal = horizontal;
            return this;
        }

        public String getVertical() {
            return vertical;
        }

        public Alignment setVertical(String vertical) {
            this.vertical = vertical;
            return this;
        }
    }
}
