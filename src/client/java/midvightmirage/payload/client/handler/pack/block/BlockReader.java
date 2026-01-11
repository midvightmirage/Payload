package midvightmirage.payload.client.handler.pack.block;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import midvightmirage.payload.Payload;
import midvightmirage.payload.registry.block.BlockFactory;
import midvightmirage.payload.registry.block.PayloadBlocks;
import midvightmirage.payload.client.handler.PackInfo;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Map.entry;

public class BlockReader {
    public static Map<String, BlockFactory<? extends Block>> blockTypes = Map.of(
            "air", ctx -> new AirBlock(ctx.props()),
            "amethyst", ctx -> new AmethystBlock(ctx.props()),
            "anvil", ctx -> new AnvilBlock(ctx.props()),
            "normal", ctx -> new Block(ctx.props()),
            "stairs", ctx -> new StairBlock(Blocks.STONE.defaultBlockState(), ctx.props()),
            "stair", ctx -> new StairBlock(Blocks.STONE.defaultBlockState(), ctx.props()),
            "slab", ctx -> new SlabBlock(ctx.props())
    );
    public static Map<String, SoundType> soundTypes = Map.ofEntries(
            entry("minecraft:empty", SoundType.EMPTY),
            entry("minecraft:wood", SoundType.WOOD),
            entry("minecraft:gravel", SoundType.GRAVEL),
            entry("minecraft:grass", SoundType.GRASS),
            entry("minecraft:lily_pad", SoundType.LILY_PAD),
            entry("minecraft:stone", SoundType.STONE),
            entry("minecraft:metal", SoundType.METAL),
            entry("minecraft:glass", SoundType.GLASS),
            entry("minecraft:wool", SoundType.WOOL),
            entry("minecraft:sand", SoundType.SAND),
            entry("minecraft:snow", SoundType.SNOW),
            entry("minecraft:powder_snow", SoundType.POWDER_SNOW),
            entry("minecraft:ladder", SoundType.LADDER),
            entry("minecraft:anvil", SoundType.ANVIL),
            entry("minecraft:slime_block", SoundType.SLIME_BLOCK),
            entry("minecraft:honey_block", SoundType.HONEY_BLOCK),

            entry("minecraft:netherite_block", SoundType.NETHERITE_BLOCK),

            entry("minecraft:copper", SoundType.COPPER),

            entry("minecraft:iron", SoundType.IRON)
    );
    private BlockInfo blockInfo;
    private final Gson gson;

    public static final BlockReader INSTANCE = new BlockReader();

    public BlockReader() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public static List<Path> getItems(Path directory) {
        List<Path> blocks = new ArrayList<>();

        if (!Files.exists(directory) || !Files.isDirectory(directory)) {
            return blocks;
        }

        try (Stream<Path> stream = Files.list(directory)) {
            stream
                    .filter(Files::isRegularFile)
                    .forEach(blocks::add);
        } catch (IOException e) {
            Payload.LOGGER.error(e.getLocalizedMessage());
        }

        return blocks;
    }

    public void loadItem(Path path) throws IOException {
        BufferedReader reader = Files.newBufferedReader(path);

        this.blockInfo = this.gson.fromJson(reader, BlockInfo.class);
    }

    public void bootstrap(Path pack, PackInfo info) {
        List<Path> blocks = getItems(
                FabricLoader.getInstance().getGameDir().resolve("payload\\packs")
                        .resolve(pack).resolve("data\\blocks")
        );
        for (Path path : blocks) {
            try {
                loadItem(path);
            } catch (IOException e) {
                Payload.LOGGER.error(e.getLocalizedMessage());
            }
            BlockBehaviour.Properties props = BlockBehaviour.Properties.of();
            if (blockInfo.getProperties().getNoOcclusion())
                props.noOcclusion();
            if (blockInfo.getProperties().getSoundType() != null || !Objects.equals(blockInfo.getProperties().getSoundType(), "minecraft:stone"))
                props.sound(getSoundFromString(blockInfo.getProperties().getSoundType()));

            PayloadBlocks.INSTANCE.registerBlock(
                    Identifier.fromNamespaceAndPath(info.getPack().getId(), blockInfo.getId()),
                    getContext(blockInfo),
                    props,
                    blockInfo.getWithItem()
            );
        }
    }

    private SoundType getSoundFromString(String string) {
        return soundTypes.get(string);
    }

    private BlockFactory<? extends Block> getContext(BlockInfo blockInfo) {
        return blockTypes.get(blockInfo.getType());
    }
}
