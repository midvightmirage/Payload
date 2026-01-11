package midvightmirage.payload.registry.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PayloadItems {
    public static final PayloadItems INSTANCE = new PayloadItems();
    private Map<Identifier, Item> items = new HashMap<Identifier, Item>();
    public PayloadItems() {}
    public static <GenericItem extends Item> GenericItem register(Identifier id, Function<Item.Properties, GenericItem> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);

        GenericItem item = itemFactory.apply(settings.setId(itemKey));

        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }
    public static Item register(Identifier id, Item.Properties settings) {
        return register(id, Item::new, settings);
    }
    public <GenericItem extends Item> void registerItem(Identifier id, Function<Item.Properties, GenericItem> itemFactory, Item.Properties settings) {
        addItem(id, register(id, itemFactory, settings));
    }
    public void registerItem(Identifier id, Item.Properties settings) {
        registerItem(id, Item::new, settings);
    }
    public void setItemList(Map<Identifier, Item> items) {
        this.items = items;
    }
    public Map<Identifier, Item> getItems() {
        return this.items;
    }
    public void addItem(Identifier itemName, Item item) {
        this.items.put(itemName, item);
    }
    public Item getItem(Identifier name) {
        return this.items.get(name);
    }
    public void bootstrap() {}
    public static class PayloadCreativeModeTabs {
        public static final PayloadCreativeModeTabs INSTANCE = new PayloadCreativeModeTabs();
        private Map<Identifier, Builder> creativeModeTabs = new HashMap<>();
        public PayloadCreativeModeTabs() {}
        public void registerGroup(@NotNull Builder groupBuilder) {
            groupBuilder.register();
            this.creativeModeTabs.put(groupBuilder.id(), groupBuilder);
        }
        public void setCreativeModeTabs(Map<Identifier, Builder> creativeModeTabs) {
            this.creativeModeTabs = creativeModeTabs;
        }
        public Map<Identifier, Builder> getCreativeModeTabs() {
            return this.creativeModeTabs;
        }
        public void bootstrap() {}
        public Builder getCreativeModeTab(Identifier id) {
            return this.creativeModeTabs.get(id);
        }
        public static class Builder {
            private final Identifier id;
            private final ResourceKey<CreativeModeTab> key;
            private final CreativeModeTab group;
            public Builder(Identifier id, CreativeModeTab group) {
                this.id = id;
                this.key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, id);
                this.group = group;
            }
            public Builder register() {
                Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, group);
                return this;
            }
            public void addItems(ItemGroupEvents.ModifyEntries t) {
                ItemGroupEvents.modifyEntriesEvent(key).register(t);
            }
            public ResourceKey<CreativeModeTab> key() {
                return this.key;
            }
            public Identifier id() {
                return this.id;
            }
            public CreativeModeTab group() {
                return this.group;
            }
        }
    }
}
