package ender.dwmod.block.tardis.complex.defaultExtDoor;

import com.mojang.serialization.MapCodec;

import ender.dwmod.block.BlockEntityInit;
import ender.dwmod.block.tardis.EndoshellBlocks;
import ender.dwmod.tardis.TardisRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DefaultExtDoorCore extends HorizontalDirectionalBlock implements EntityBlock {

    public DefaultExtDoorCore(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(DefaultExtDoorShapes.OPEN, false));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos arg0, BlockState arg1) {
        return new DefaultExtDoorCoreBE(arg0, arg1);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(DefaultExtDoorCore::new);
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, DefaultExtDoorShapes.OPEN);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
        if (state.is(newState.getBlock()))
            return;
        BlockPos basePos = pos.below();

        for (int x = 0; x < 2; x++)
            for (int y = 0; y < 3; y++)
                level.destroyBlock(basePos.above(y).relative(state.getValue(FACING).getCounterClockWise(), x), false);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
            BlockHitResult hitResult) {
        if (!level.isClientSide) {
            if (TardisRegistries.get(pos) != null) {
                return ((DefaultExtDoorCoreBE) level.getBlockEntity(pos)).useItemOn();
            }
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        Direction facing = state.getValue(FACING);
        BlockPos basePos = pos.below();
        boolean open = state.getValue(DefaultExtDoorShapes.OPEN);
        for (int x = 0; x < 2; x++)
            for (int y = 0; y < 3; y++)
                if (!(x == 0 && y == 1))
                    level.setBlock(basePos.above(y).relative(facing.getCounterClockWise(), x),
                            EndoshellBlocks.TARDIS_DEFAULT_EXT_DOOR.defaultBlockState()
                                    .setValue(DefaultExtDoorInteract.FACING, facing)
                                    .setValue(DefaultExtDoorInteract.POS, DefaultExtDoorInteract.Pos.valueOf(x, y))
                                    .setValue(DefaultExtDoorShapes.OPEN, open),
                            Block.UPDATE_ALL);
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

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
            BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, BlockEntityInit.DEFAULT_EXT_DOOR_CORE_BE,
                DefaultExtDoorCoreBE::tick);
    }

    @SuppressWarnings("unchecked")
    @org.jetbrains.annotations.Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
            BlockEntityType<A> serverType, BlockEntityType<E> clientType, BlockEntityTicker<? super E> ticker) {
        return clientType == serverType ? (@org.jetbrains.annotations.Nullable BlockEntityTicker<A>) ticker : null;
    }
}