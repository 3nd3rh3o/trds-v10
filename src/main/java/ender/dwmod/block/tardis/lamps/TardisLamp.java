package ender.dwmod.block.tardis.lamps;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public abstract class TardisLamp extends Block {
    public static final BooleanProperty LIT = BooleanProperty.create("lit");

    public TardisLamp(Properties properties) {
        super(properties);
        defaultBlockState().setValue(LIT, false);
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT);
    }    
}
