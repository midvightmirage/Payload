package midvightmirage.payload.client.handler.pack.item.tab;

import java.util.List;

public class TabInfo {
    private String id;
    private Title title = new Title();
    private String icon = "minecraft:air";
    private List<String> displayItems;

    public String getId() {
        return id;
    }
    public Title getTitle() {
        return title;
    }
    public String getIcon() {
        return icon;
    }
    public List<String> getDisplayItems() {
        return displayItems;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setTitle(Title title) {
        this.title = title;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public void setDisplayItems(List<String> displayItems) {
        this.displayItems = displayItems;
    }

    public static class Title {
        private String type = "translatable";
        private String name;

        public String getType() {
            return type;
        }
        public String getName() {
            return name;
        }

        public void setType(String type) {
            this.type = type;
        }
        public void setName(String name) {
            this.name = name;
        }
    }
}
