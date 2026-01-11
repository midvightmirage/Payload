package midvightmirage.payload.registry.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

@FunctionalInterface
public interface BlockFactory<T extends Block> {
    T create(BlockContext context);

    class BlockContext {
        private Block baseBlock;
        private BlockBehaviour.Properties properties;

        public BlockContext(
                Block baseBlock,
                BlockBehaviour.Properties properties
        ) {
            this.baseBlock = baseBlock;
            this.properties = properties;
        }

        public BlockContext(BlockBehaviour.Properties properties) {
            this(null, properties);
        }

        public BlockContext setProperties(BlockBehaviour.Properties properties) {
            this.properties = properties;
            return this;
        }

        public BlockBehaviour.Properties properties() {
            return properties;
        }
        public BlockBehaviour.Properties props() {
            return properties;
        }
    }
}
