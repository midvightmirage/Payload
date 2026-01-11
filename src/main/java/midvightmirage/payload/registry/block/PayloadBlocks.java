package midvightmirage.payload.registry.block;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PayloadBlocks {
    public static final PayloadBlocks INSTANCE = new PayloadBlocks();
    private Map<Identifier, Block> blocks = new HashMap<>();
    private Map<Identifier, BlockItem> blockItems = new HashMap<>();
    public PayloadBlocks() {}
    public Map<Identifier, Block> getBlocks() {
        return blocks;
    }
    public Block getBlock(Identifier id) {
        return getBlocks().get(id);
    }
    public void setBlocks(Map<Identifier, Block> blocks) {
        this.blocks = blocks;
    }
    public void setBlock(Identifier id, Block block) {
        this.blocks.put(id, block);
    }
    public void setBlockItems(Map<Identifier, BlockItem> blockItems) {
        this.blockItems = blockItems;
    }
    public Map<Identifier, BlockItem> getBlockItems() {
        return blockItems;
    }
    public <T extends Block> void registerBlock(Identifier id, BlockFactory<T> blockFactory, BlockFactory.BlockContext context, boolean shouldRegisterItem) {
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, id);
        Block block = blockFactory.create(context.setProperties(context.properties().setId(blockKey)));
        Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
        blocks.put(id, block);
        if (shouldRegisterItem) {
            ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);
            blockItems.put(id, Registry.register(
                    BuiltInRegistries.ITEM,
                    id,
                    new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix())
            ));
        }
    }

    public <T extends Block> void registerBlock(Identifier id, BlockFactory<T> blockFactory, BlockBehaviour.Properties properties, boolean shouldRegisterItem) {
        registerBlock(id, blockFactory, new BlockFactory.BlockContext(properties), shouldRegisterItem);
    }

    public void bootstrap() {}
    public static class Builder {
        private final Identifier id;
        private final Function<BlockBehaviour.Properties, Block> blockFactory;
        private final BlockBehaviour.Properties settings;
        private final Block block;
        private BlockItem blockItem = null;
        public Builder(Identifier id, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties settings) {
            this.id = id;
            this.blockFactory = blockFactory;
            this.settings = settings;
            ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, id);
            this.block = blockFactory.apply(settings.setId(blockKey));
            Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
        }
        public Builder registerItem() {
            ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);
            this.blockItem = new BlockItem(this.block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
            return this;
        }
        public Identifier name() {return id;}
        public Function<BlockBehaviour.Properties, Block> blockFactory() {return blockFactory;}
        public BlockBehaviour.Properties settings() {return settings;}
        public Block block() {
            return block;
        }
        public BlockItem blockItem() {
            return blockItem;
        }
    }
}
