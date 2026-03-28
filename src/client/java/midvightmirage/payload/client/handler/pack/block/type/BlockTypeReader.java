package midvightmirage.payload.client.handler.pack.block.type;

import com.mojang.datafixers.util.Pair;
import midvightmirage.payload.Payload;
import midvightmirage.payload.client.handler.PackInfo;
import midvightmirage.payload.client.handler.pack.PackJsonReader;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class BlockTypeReader extends PackJsonReader<BlockTypeReader, BlockTypeInfo> {
    public static final BlockTypeReader INSTANCE = new BlockTypeReader();

    public BlockTypeReader() {
        super(BlockTypeReader.class, BlockTypeInfo.class);
    }

    @Override
    public void bootstrap(Path pack, PackInfo info) {
        Pair<Boolean, List<Path>> blockTypes = getItems(
                FabricLoader.getInstance().getGameDir().resolve("payload/packs")
                        .resolve(pack).resolve("data/blocks/types")
        );

        if (blockTypes.getFirst()) {
            for (Path path : blockTypes.getSecond()) {
                try {
                    loadItem(path);
                } catch (IOException e) {
                    Payload.LOGGER.error(e.getLocalizedMessage());
                }
            }
        }
    }
}
