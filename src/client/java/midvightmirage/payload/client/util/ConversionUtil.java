package midvightmirage.payload.client.util;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.SoundType;

import java.util.Map;

import static java.util.Map.entry;

public class ConversionUtil {
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

    public static class RarityConversion {
        public static Rarity stringToRarity(String str) {
            return Rarity.valueOf(str.toUpperCase());
        }

        public static String rarityToString(Rarity rarity) {
            return rarity.getSerializedName();
        }
    }

    public static class SoundTypeConversion {
        public static SoundType getSoundFromString(String string) {
            return soundTypes.get(string);
        }
    }
}
