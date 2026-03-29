package midvightmirage.payload.client.handler.pack.block.type.clazz;

import midvightmirage.payload.client.handler.pack.block.type.BlockTypeInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BlockTypeClass extends Block {
    protected List<BlockTypeInfo.BlockState> blockStates;
    protected List<Property<?>> processedBlockStates;
    protected Map<String, Object> defaultBlockStates;

    public BlockTypeClass(Properties properties, List<BlockTypeInfo.BlockState> blockStates) {
        super(properties);
        this.blockStates = blockStates;
        for (BlockTypeInfo.BlockState blockState : blockStates) {
            String type = blockState.getType();
            Property<?> property;
            if (Objects.equals(type, "int")) {
                property = IntegerProperty.create(blockState.getNameVar(), blockState.getMin(), blockState.getMax());
                processedBlockStates.add(property);
            }
            defaultBlockStates.put(blockState.getNameVar(), blockState.getDefaultValue());
        }
    }

    private VoxelShape shape = Shapes.block();

    public void setVoxelShape(VoxelShape shape) {
        this.shape = shape;
    }

    @Override
    protected @NonNull VoxelShape getShape(@NonNull BlockState state, @NonNull BlockGetter level, @NonNull BlockPos pos, @NonNull CollisionContext context) {
        return shape;
    }

    public VoxelShape getVoxelShape() {
        return shape;
    }

    public final void registerDefaultBlockState(final BlockState state) {
        this.registerDefaultState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        for (Property<?> blockState : this.processedBlockStates) {
            builder.add(blockState);
        }
    }
}
