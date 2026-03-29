package midvightmirage.payload.client.handler.pack.block.type;

import midvightmirage.payload.client.handler.pack.PackJsonInfo;
import org.apache.commons.lang3.tuple.Triple;

import java.awt.*;
import java.util.List;

public class BlockTypeInfo implements PackJsonInfo {
    private Shape shapes;
    private List<BlockState> blockstates;

    public List<BlockState> getBlockstates() {
        return blockstates;
    }

    public Shape getShapes() {
        return shapes;
    }

    public void setBlockstates(List<BlockState> blockstates) {
        this.blockstates = blockstates;
    }

    public void setShapes(Shape shapes) {
        this.shapes = shapes;
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

    public static class Shapes {
        private List<Triple<Integer, Integer, Integer>> collisionShape;

        public List<Triple<Integer, Integer, Integer>> getCollisionShape() {
            return collisionShape;
        }

        public void setCollisionShape(List<Triple<Integer, Integer, Integer>> collisionShape) {
            this.collisionShape = collisionShape;
        }
    }
}
