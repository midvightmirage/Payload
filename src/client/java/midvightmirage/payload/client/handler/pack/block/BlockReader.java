package midvightmirage.payload.client.handler.pack.block;

import com.mojang.datafixers.util.Pair;
import midvightmirage.payload.Payload;
import midvightmirage.payload.client.handler.pack.PackJsonReader;
import midvightmirage.payload.client.util.ConversionUtil;
import midvightmirage.payload.registry.block.BlockFactory;
import midvightmirage.payload.registry.block.PayloadBlocks;
import midvightmirage.payload.client.handler.PackInfo;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.util.Map.entry;

public class BlockReader extends PackJsonReader<BlockReader, BlockInfo> {
    public static Map<String, BlockFactory<? extends Block>> blockTypes = Map.ofEntries(
            entry("air", ctx -> new AirBlock(ctx.props())),
            entry("amethyst", ctx -> new AmethystBlock(ctx.props())),
            entry("anvil", ctx -> new AnvilBlock(ctx.props())),
            entry("normal", ctx -> new Block(ctx.props())),
            entry("stairs", ctx -> new StairBlock(Blocks.STONE.defaultBlockState(), ctx.props())),
            entry("stair", ctx -> new StairBlock(Blocks.STONE.defaultBlockState(), ctx.props())),
            entry("slab", ctx -> new SlabBlock(ctx.props()))
    );

    public static void addBlockType(String name, BlockFactory<? extends Block> blockFactory) {
        blockTypes.put(name, blockFactory);
    }

    public static final BlockReader INSTANCE = new BlockReader();

    public BlockReader() {
        super(BlockReader.class, BlockInfo.class);
    }

    @Override
    public void bootstrap(Path pack, PackInfo info) {
        Pair<Boolean, List<Path>> blocks = getItems(
                FabricLoader.getInstance().getGameDir().resolve("payload/packs")
                        .resolve(pack).resolve("data/blocks")
        );
        for (Path path : blocks.getSecond()) {
            try {
                loadItem(path);
            } catch (IOException e) {
                Payload.LOGGER.error(e.getLocalizedMessage());
            }
            BlockBehaviour.Properties props = BlockBehaviour.Properties.of();
            if (this.objectInfo.getProperties().getNoOcclusion())
                props.noOcclusion();
            if (this.objectInfo.getProperties().getSoundType() != null || !Objects.equals(this.objectInfo.getProperties().getSoundType(), "minecraft:stone"))
                props.sound(ConversionUtil.SoundTypeConversion.getSoundFromString(this.objectInfo.getProperties().getSoundType()));

            PayloadBlocks.INSTANCE.registerBlock(
                    Identifier.fromNamespaceAndPath(info.getPack().getId(), this.objectInfo.getId()),
                    getContext(this.objectInfo),
                    props,
                    this.objectInfo.getWithItem()
            );
        }
    }

    private BlockFactory<? extends Block> getContext(BlockInfo blockInfo) {
        return blockTypes.get(blockInfo.getType());
    }
}
