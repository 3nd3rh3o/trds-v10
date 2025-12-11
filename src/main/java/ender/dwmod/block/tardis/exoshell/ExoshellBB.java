package ender.dwmod.block.tardis.exoshell;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ExoshellBB extends Block {
    public ExoshellBB(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos,
            CollisionContext context) {
        return switch (state) { // TODO - create different shapes for different directions and chamo settings.
            default -> super.getCollisionShape(state, level, pos, context);
        };
    }

       
}
