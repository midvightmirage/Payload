package midvightmirage.payload_ui.client.util.registry.ui_style.json.elements;

public abstract class Element<T> {
    protected String type;

    public String getType() {
        return type;
    }

    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }

    public T setType(String type) {
        this.type = type;
        return this.self();
    }
}
