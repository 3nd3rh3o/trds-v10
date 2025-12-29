package ender.dwmod.block.tardis.complex.defaultExtDoor;

import ender.dwmod.block.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DefaultExtDoorInteract extends Block {

    
    
    enum Pos implements StringRepresentable{
        TOP_L("top_l"), TOP_R("top_r"),
        MID_R("mid_r"), DOWN_L("down_l"), DOWN_R("down_r");

        private final String name;
    
        Pos(String name)
        {
            this.name = name;
        }

        public static Pos valueOf(int x, int y)
        {
            if (y == 2)
                return x == 0 ? TOP_L : TOP_R;
            else if (y == 1)
                return MID_R;
            else
                return x == 0 ? DOWN_L : DOWN_R;
        }

        @Override
        public String getSerializedName() {
            return name;
        }

        int x() {
            return switch (this)
            {
                case TOP_L, DOWN_L -> 0;
                default -> 1;
            };
        }

		int y() {
			return switch (this)
            {
                case TOP_L, TOP_R -> 2;
                case MID_R -> 1;
                default -> 0;
            };
		}
    }

    static final EnumProperty<Pos> POS = EnumProperty.create("pos", Pos.class);
    static final DirectionProperty FACING = DirectionProperty.create("facing");

    public DefaultExtDoorInteract(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(DefaultExtDoorShapes.OPEN, false));
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POS, FACING, DefaultExtDoorShapes.OPEN);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
            BlockHitResult hitResult) {
        if (!level.isClientSide) {
            Direction facing = state.getValue(FACING);
            BlockPos offset = switch (state.getValue(POS))
            {
                case TOP_L -> pos.below();
                case TOP_R -> pos.below().relative(facing.getClockWise());
                case MID_R -> pos.relative(facing.getClockWise());
                case DOWN_L -> pos.above();
                case DOWN_R -> pos.above().relative(facing.getClockWise());
            };

            return ((DefaultExtDoorCoreBE)level.getBlockEntity(offset)).useItemOn();
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (placer != null && !level.isClientSide && state.getValue(FACING) == Direction.DOWN)
        {
            Direction facing = Direction.fromYRot(placer.getYRot()).getOpposite();
            level.setBlock(pos, state.setValue(FACING, facing).setValue(POS, Pos.DOWN_L), Block.UPDATE_ALL);
            
            level.setBlock(pos.above(2), asBlock().defaultBlockState().setValue(POS, Pos.TOP_L).setValue(FACING, facing), Block.UPDATE_ALL);
            level.setBlock(pos.above(), BlockInit.DEFAULT_EXT_DOOR_CORE.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, facing), Block.UPDATE_ALL);
            
            if (facing == Direction.NORTH)
            {
                level.setBlock(pos.west(), asBlock().defaultBlockState().setValue(POS, Pos.DOWN_R).setValue(FACING, facing), Block.UPDATE_ALL);
                level.setBlock(pos.above().west(), asBlock().defaultBlockState().setValue(POS, Pos.MID_R).setValue(FACING, facing), Block.UPDATE_ALL);
                level.setBlock(pos.above(2).west(), asBlock().defaultBlockState().setValue(POS, Pos.TOP_R).setValue(FACING, facing), Block.UPDATE_ALL);
            }
            else if (facing == Direction.SOUTH)
            {
                level.setBlock(pos.east(), asBlock().defaultBlockState().setValue(POS, Pos.DOWN_R).setValue(FACING, facing), Block.UPDATE_ALL);
                level.setBlock(pos.above().east(), asBlock().defaultBlockState().setValue(POS, Pos.MID_R).setValue(FACING, facing), Block.UPDATE_ALL);
                level.setBlock(pos.above(2).east(), asBlock().defaultBlockState().setValue(POS, Pos.TOP_R).setValue(FACING, facing), Block.UPDATE_ALL);
            }
            else if (facing == Direction.WEST)
            {
                level.setBlock(pos.south(), asBlock().defaultBlockState().setValue(POS, Pos.DOWN_R).setValue(FACING, facing), Block.UPDATE_ALL);
                level.setBlock(pos.above().south(), asBlock().defaultBlockState().setValue(POS, Pos.MID_R).setValue(FACING, facing), Block.UPDATE_ALL);
                level.setBlock(pos.above(2).south(), asBlock().defaultBlockState().setValue(POS, Pos.TOP_R).setValue(FACING, facing), Block.UPDATE_ALL);
            }
            else
            {
                level.setBlock(pos.north(), asBlock().defaultBlockState().setValue(POS, Pos.DOWN_R).setValue(FACING, facing), Block.UPDATE_ALL);
                level.setBlock(pos.above().north(), asBlock().defaultBlockState().setValue(POS, Pos.MID_R).setValue(FACING, facing), Block.UPDATE_ALL);
                level.setBlock(pos.above(2).north(), asBlock().defaultBlockState().setValue(POS, Pos.TOP_R).setValue(FACING, facing), Block.UPDATE_ALL);
            }
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {    
        super.onRemove(state, level, pos, newState, movedByPiston);
        if (state.is(newState.getBlock()))
            return;
        BlockPos basePos = switch (state.getValue(POS))
        {
            case TOP_L -> pos.below(2);
            case TOP_R -> pos.below(2).relative(state.getValue(FACING).getClockWise());
            case MID_R -> pos.below().relative(state.getValue(FACING).getClockWise());
            case DOWN_L -> pos;
            case DOWN_R -> pos.relative(state.getValue(FACING).getClockWise());
        };

        for (int x = 0; x < 2; x++)
            for (int y = 0; y < 3; y++)
                level.destroyBlock(basePos.above(y).relative(state.getValue(FACING).getCounterClockWise(), x), false);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos,
            CollisionContext context) {
        
        return DefaultExtDoorShapes.getShape(state);
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return DefaultExtDoorShapes.getShape(state);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return DefaultExtDoorShapes.getShape(state);
    }    
}
