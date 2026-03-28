package midvightmirage.payload.client.handler.pack.item;

import com.mojang.datafixers.util.Pair;
import midvightmirage.payload.Payload;
import midvightmirage.payload.client.handler.pack.PackJsonReader;
import midvightmirage.payload.client.util.ConversionUtil;
import midvightmirage.payload.registry.item.PayloadItems;
import midvightmirage.payload.client.handler.PackInfo;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ItemReader extends PackJsonReader<ItemReader, ItemInfo> {
    public static final ItemReader INSTANCE = new ItemReader();

    public ItemReader() {
        super(ItemReader.class, ItemInfo.class);
    }

    @Override
    public void bootstrap(Path pack, PackInfo info) {
        Pair<Boolean, List<Path>> items = getItems(
                FabricLoader.getInstance().getGameDir().resolve("payload/packs")
                        .resolve(pack).resolve("data/items")
        );
        for (Path path : items.getSecond()) {
            try {
                loadItem(path);
            } catch (IOException e) {
                Payload.LOGGER.error(e.getLocalizedMessage());
            }
            Item.Properties props = createProperties();
            PayloadItems.INSTANCE.registerItem(Identifier.fromNamespaceAndPath(info.getPack().getId(), objectInfo.getId()), props);
        }
    }

    private Item.@NonNull Properties createProperties() {
        Item.Properties props = new Item.Properties();
        if (objectInfo.getProperties().getUseCooldown() != null) {
            props.useCooldown(objectInfo.getProperties().getUseCooldown());
        }
        if (objectInfo.getProperties().getStacksTo() != null) {
            props.stacksTo(objectInfo.getProperties().getStacksTo());
        }
        if (objectInfo.getProperties().getDurability() != null) {
            props.durability(objectInfo.getProperties().getDurability());
        }
        if (objectInfo.getProperties().getRarity() != null) {
            props.rarity(ConversionUtil.RarityConversion.stringToRarity(objectInfo.getProperties().getRarity()));
        }
        return props;
    }
}
