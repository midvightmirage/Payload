package midvightmirage.payload.client.handler.pack.block.type;

import midvightmirage.payload.client.handler.pack.PackJsonInfo;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.List;

public class BlockTypeInfo implements PackJsonInfo {
    private List<BlockState> blockstates;

    public List<BlockState> getBlockstates() {
        return blockstates;
    }

    public void setBlockstates(List<BlockState> blockstates) {
        this.blockstates = blockstates;
    }

    public static class BlockState {
        private String name;
        private String type;
        private Object defaultValue;
        private Integer min;
        private Integer max;

        public String getNameVar() {
            return name;
        }

        public String getType() {
            return type;
        }

        public Object getDefaultValue() {
            return defaultValue;
        }

        public Integer getMin() {
            return min;
        }

        public Integer getMax() {
            return max;
        }


        public void setName(String name) {
            this.name = name;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setDefaultValue(Object defaultValue) {
            this.defaultValue = defaultValue;
        }

        public void setMin(Integer min) {
            this.min = min;
        }

        public void setMax(Integer max) {
            this.max = max;
        }
    }
}
