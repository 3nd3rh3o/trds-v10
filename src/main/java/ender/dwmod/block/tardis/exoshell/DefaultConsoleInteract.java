package ender.dwmod.block.tardis.exoshell;


import ender.dwmod.block.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.util.StringRepresentable;

public class DefaultConsoleInteract extends Block {
    private enum Pos implements StringRepresentable {
        N("n"), NE("ne"), NW("nw"), S("s"), SE("se"), SW("sw"), E("e"), W("w"), NONE("none");
        // TODO - add up_center ! to have a nice hitbox on the rotor
        private final String name;

        Pos(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }

    private static final VoxelShape SHAPE = Shapes.box(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    private static final VoxelShape SHAPE_UP = Shapes.box(0.0f, 0.0f, 0.0f, 1.0f, 0.2f, 1.0f);
    private static final BooleanProperty UP = BooleanProperty.create("up");
    private static final EnumProperty<Pos> POS = EnumProperty.create("pos", Pos.class);



    public DefaultConsoleInteract(Properties properties) {
        super(properties);
        defaultBlockState().setValue(UP, false);
        defaultBlockState().setValue(POS, Pos.NONE);
    }

    

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(UP, POS);
    }

    


    @Override // Update block state based on console core position.
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        Pos p;
        boolean u;
        if (level.getBlockState(pos.north()).getBlock().equals(BlockInit.DEFAULT_CONSOLE))
        {    
            p = Pos.N;
            u = false;
        }
        else if (level.getBlockState(pos.south()).getBlock().equals(BlockInit.DEFAULT_CONSOLE))
        {
            p = Pos.S;
            u = false;
        }
        else if (level.getBlockState(pos.east()).getBlock().equals(BlockInit.DEFAULT_CONSOLE))
        {
            p = Pos.E;
            u = false;
        }
        else if (level.getBlockState(pos.west()).getBlock().equals(BlockInit.DEFAULT_CONSOLE))
        {
            p = Pos.W;
            u = false;
        }
        else if (level.getBlockState(pos.north().east()).getBlock().equals(BlockInit.DEFAULT_CONSOLE))
        {
            p = Pos.NE;
            u = false;
        }
        else if (level.getBlockState(pos.north().west()).getBlock().equals(BlockInit.DEFAULT_CONSOLE))
        {
            p = Pos.NW;
            u = false;
        }
        else if (level.getBlockState(pos.south().east()).getBlock().equals(BlockInit.DEFAULT_CONSOLE))
        {
            p = Pos.SE;
            u = false;
        }
        else if (level.getBlockState(pos.south().west()).getBlock().equals(BlockInit.DEFAULT_CONSOLE))
        {
            p = Pos.SW;
            u = false;
        }
        else if (level.getBlockState(pos.below().north()).getBlock().equals(BlockInit.DEFAULT_CONSOLE))
        {    
            p = Pos.N;
            u = true;
        }
        else if (level.getBlockState(pos.below().south()).getBlock().equals(BlockInit.DEFAULT_CONSOLE))
        {
            p = Pos.S;
            u = true;
        }
        else if (level.getBlockState(pos.below().east()).getBlock().equals(BlockInit.DEFAULT_CONSOLE))
        {
            p = Pos.E;
            u = true;
        }
        else if (level.getBlockState(pos.below().west()).getBlock().equals(BlockInit.DEFAULT_CONSOLE))
        {
            p = Pos.W;
            u = true;
        }
        else if (level.getBlockState(pos.below().north().east()).getBlock().equals(BlockInit.DEFAULT_CONSOLE))
        {
            p = Pos.NE;
            u = true;
        }
        else if (level.getBlockState(pos.below().north().west()).getBlock().equals(BlockInit.DEFAULT_CONSOLE))
        {
            p = Pos.NW;
            u = true;
        }
        else if (level.getBlockState(pos.below().south().east()).getBlock().equals(BlockInit.DEFAULT_CONSOLE))
        {
            p = Pos.SE;
            u = true;
        }
        else if (level.getBlockState(pos.below().south().west()).getBlock().equals(BlockInit.DEFAULT_CONSOLE))
        {
            p = Pos.SW;
            u = true;
        }
        else
        {
            p = Pos.NONE;
            u = false;
        }

        level.setBlock(pos, state.setValue(POS, p).setValue(UP, u), 3);
    }
 


    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos,
            CollisionContext context) {
        if (state.getValue(UP))
            return SHAPE_UP;
        else
            return SHAPE;
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        if (state.getValue(UP))
            return SHAPE_UP;
        else
            return SHAPE;
    }

    @Override
    protected VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(UP))
            return SHAPE_UP;
        else
            return SHAPE;
    }

    

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
            BlockHitResult hitResult) {
            // relay to the block entity, to handle raycast.
        return switch (state.getValue(POS))
        {
            case N -> state.getValue(UP) ? ((DefaultConsoleBE)level.getBlockEntity(pos.below().north())).interact(player, hitResult) : ((DefaultConsoleBE)level.getBlockEntity(pos.north())).interact(player, hitResult);
            case S -> state.getValue(UP) ? ((DefaultConsoleBE)level.getBlockEntity(pos.below().south())).interact(player, hitResult) : ((DefaultConsoleBE)level.getBlockEntity(pos.south())).interact(player, hitResult);
            case E -> state.getValue(UP) ? ((DefaultConsoleBE)level.getBlockEntity(pos.below().east())).interact(player, hitResult) : ((DefaultConsoleBE)level.getBlockEntity(pos.east())).interact(player, hitResult);
            case W -> state.getValue(UP) ? ((DefaultConsoleBE)level.getBlockEntity(pos.below().west())).interact(player, hitResult) : ((DefaultConsoleBE)level.getBlockEntity(pos.west())).interact(player, hitResult);
            case NE -> state.getValue(UP) ? ((DefaultConsoleBE)level.getBlockEntity(pos.below().north().east())).interact(player, hitResult) : ((DefaultConsoleBE)level.getBlockEntity(pos.north().east())).interact(player, hitResult);
            case NW -> state.getValue(UP) ? ((DefaultConsoleBE)level.getBlockEntity(pos.below().north().west())).interact(player, hitResult) : ((DefaultConsoleBE)level.getBlockEntity(pos.north().west())).interact(player, hitResult);
            case SE -> state.getValue(UP) ? ((DefaultConsoleBE)level.getBlockEntity(pos.below().south().east())).interact(player, hitResult) : ((DefaultConsoleBE)level.getBlockEntity(pos.south().east())).interact(player, hitResult);
            case SW -> state.getValue(UP) ? ((DefaultConsoleBE)level.getBlockEntity(pos.below().south().west())).interact(player, hitResult) : ((DefaultConsoleBE)level.getBlockEntity(pos.south().west())).interact(player, hitResult);
            default -> InteractionResult.FAIL;
        };
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        
        super.onRemove(state, level, pos, newState, movedByPiston);
        if (state.getBlock() == newState.getBlock()) {
            return;
        }
        BlockPos offset = switch(state.getValue(POS))
        {
            case N -> state.getValue(UP) ? pos.below().north() : pos.north();
            case S -> state.getValue(UP) ? pos.below().south() : pos.south();
            case E -> state.getValue(UP) ? pos.below().east() : pos.east();
            case W -> state.getValue(UP) ? pos.below().west() : pos.west();
            case NE -> state.getValue(UP) ? pos.below().north().east() : pos.north().east();
            case NW -> state.getValue(UP) ? pos.below().north().west() : pos.north().west();
            case SE -> state.getValue(UP) ? pos.below().south().east() : pos.south().east();
            case SW -> state.getValue(UP) ? pos.below().south().west() : pos.south().west();
            default -> pos;
        };

        for (int x = -1; x <= 1; x++)
        {
            for (int y = 0; y <= 1; y++)
            {
                for (int z = -1; z <= 1; z++)
                {
                    BlockPos checkPos = offset.offset(x, y, z);
                    if (level.getBlockState(checkPos).getBlock().equals(BlockInit.DEFAULT_CONSOLE)|| level.getBlockState(checkPos).getBlock().equals(BlockInit.DEFAULT_CONSOLE_INTERACT))
                    {
                        level.destroyBlock(checkPos, false);
                    }
                }
            }
        }
    }    
}
