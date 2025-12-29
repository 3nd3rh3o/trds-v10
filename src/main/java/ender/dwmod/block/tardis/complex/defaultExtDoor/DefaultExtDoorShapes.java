package ender.dwmod.block.tardis.complex.defaultExtDoor;

import java.util.Map;
import java.util.stream.Stream;

import org.joml.Vector2i;

import ender.dwmod.block.tardis.EndoshellBlocks;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class DefaultExtDoorShapes {
    static final Map<Vector2i, Map<Direction, VoxelShape>> CLOSE_SHAPES;
    static final Map<Vector2i, Map<Direction, VoxelShape>> OPEN_SHAPES;

    public static final BooleanProperty OPEN = BooleanProperty.create("open");

    private static final VoxelShape CLOSE_DOWN_L_SOUTH = Stream.of(
            Block.box(0, 46, 2, 32, 48, 16),
            Block.box(0, 0, 2, 32, 2, 16),
            Block.box(28, 2, 2, 32, 46, 16),
            Block.box(0, 2, 2, 4, 46, 16),
            Block.box(4, 2, 4, 28, 46, 5)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape OPEN_DOWN_L_SOUTH = Stream.of(
            Block.box(0, 46, 2, 32, 48, 16),
            Block.box(0, 0, 2, 32, 2, 16),
            Block.box(28, 2, 2, 32, 46, 16),
            Block.box(0, 2, 2, 4, 46, 16)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape CLOSE_DOWN_R_SOUTH = Stream.of(
            Block.box(-16, 46, 2, 16, 48, 16),
            Block.box(-16, 0, 2, 16, 2, 16),
            Block.box(12, 2, 2, 16, 46, 16),
            Block.box(-16, 2, 2, -12, 46, 16),
            Block.box(-12, 2, 4, 12, 46, 5)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape OPEN_DOWN_R_SOUTH = Stream.of(
            Block.box(-16, 46, 2, 16, 48, 16),
            Block.box(-16, 0, 2, 16, 2, 16),
            Block.box(12, 2, 2, 16, 46, 16),
            Block.box(-16, 2, 2, -12, 46, 16)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape CLOSE_CORE_SOUTH = Stream.of(
            Block.box(0, 30, 2, 32, 32, 16),
            Block.box(0, -16, 2, 32, -14, 16),
            Block.box(28, -14, 2, 32, 30, 16),
            Block.box(0, -14, 2, 4, 30, 16),
            Block.box(4, -14, 4, 28, 30, 5)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape OPEN_CORE_SOUTH = Stream.of(
            Block.box(0, 30, 2, 32, 32, 16),
            Block.box(0, -16, 2, 32, -14, 16),
            Block.box(28, -14, 2, 32, 30, 16),
            Block.box(0, -14, 2, 4, 30, 16)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape CLOSE_MID_R_SOUTH = Stream.of(
            Block.box(-16, 30, 2, 16, 32, 16),
            Block.box(-16, -16, 2, 16, -14, 16),
            Block.box(12, -14, 2, 16, 30, 16),
            Block.box(-16, -14, 2, -12, 30, 16),
            Block.box(-12, -14, 4, 12, 30, 5)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape OPEN_MID_R_SOUTH = Stream.of(
            Block.box(-16, 30, 2, 16, 32, 16),
            Block.box(-16, -16, 2, 16, -14, 16),
            Block.box(12, -14, 2, 16, 30, 16),
            Block.box(-16, -14, 2, -12, 30, 16)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape CLOSE_TOP_L_SOUTH = Stream.of(
            Block.box(0, 14, 2, 32, 16, 16),
            Block.box(0, -32, 2, 32, -30, 16),
            Block.box(28, -30, 2, 32, 14, 16),
            Block.box(0, -30, 2, 4, 14, 16),
            Block.box(4, -30, 4, 28, 14, 5)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape OPEN_TOP_L_SOUTH = Stream.of(
            Block.box(0, 14, 2, 32, 16, 16),
            Block.box(0, -32, 2, 32, -30, 16),
            Block.box(28, -30, 2, 32, 14, 16),
            Block.box(0, -30, 2, 4, 14, 16)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape CLOSE_TOP_R_SOUTH = Stream.of(
            Block.box(-16, 14, 2, 16, 16, 16),
            Block.box(-16, -32, 2, 16, -30, 16),
            Block.box(12, -30, 2, 16, 14, 16),
            Block.box(-16, -30, 2, -12, 14, 16),
            Block.box(-12, -30, 4, 12, 14, 5)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape OPEN_TOP_R_SOUTH = Stream.of(
            Block.box(-16, 14, 2, 16, 16, 16),
            Block.box(-16, -32, 2, 16, -30, 16),
            Block.box(12, -30, 2, 16, 14, 16),
            Block.box(-16, -30, 2, -12, 14, 16)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    // NORTH is computed by rotating boxes 180° around (8,8,8):
    // x1' = 16 - x2, x2' = 16 - x1 ; z1' = 16 - z2, z2' = 16 - z1 ; y unchanged.
    private static final VoxelShape CLOSE_DOWN_L_NORTH = Stream.of(
            Block.box(-16, 46, 0, 16, 48, 14),
            Block.box(-16, 0, 0, 16, 2, 14),
            Block.box(-16, 2, 0, -12, 46, 14),
            Block.box(12, 2, 0, 16, 46, 14),
            Block.box(-12, 2, 11, 12, 46, 12)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape OPEN_DOWN_L_NORTH = Stream.of(
            Block.box(-16, 46, 0, 16, 48, 14),
            Block.box(-16, 0, 0, 16, 2, 14),
            Block.box(-16, 2, 0, -12, 46, 14),
            Block.box(12, 2, 0, 16, 46, 14)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape CLOSE_DOWN_R_NORTH = Stream.of(
            Block.box(0, 46, 0, 32, 48, 14),
            Block.box(0, 0, 0, 32, 2, 14),
            Block.box(0, 2, 0, 4, 46, 14),
            Block.box(28, 2, 0, 32, 46, 14),
            Block.box(4, 2, 11, 28, 46, 12)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape OPEN_DOWN_R_NORTH = Stream.of(
            Block.box(0, 46, 0, 32, 48, 14),
            Block.box(0, 0, 0, 32, 2, 14),
            Block.box(0, 2, 0, 4, 46, 14),
            Block.box(28, 2, 0, 32, 46, 14)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape CLOSE_CORE_NORTH = Stream.of(
            Block.box(-16, 30, 0, 16, 32, 14),
            Block.box(-16, -16, 0, 16, -14, 14),
            Block.box(-16, -14, 0, -12, 30, 14),
            Block.box(12, -14, 0, 16, 30, 14),
            Block.box(-12, -14, 11, 12, 30, 12)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape OPEN_CORE_NORTH = Stream.of(
            Block.box(-16, 30, 0, 16, 32, 14),
            Block.box(-16, -16, 0, 16, -14, 14),
            Block.box(-16, -14, 0, -12, 30, 14),
            Block.box(12, -14, 0, 16, 30, 14)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape CLOSE_TOP_L_NORTH = Stream.of(
            Block.box(-16, 14, 0, 16, 16, 14),
            Block.box(-16, -32, 0, 16, -30, 14),
            Block.box(-16, -30, 0, -12, 14, 14),
            Block.box(12, -30, 0, 16, 14, 14),
            Block.box(-12, -30, 11, 12, 14, 12)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape OPEN_TOP_L_NORTH = Stream.of(
            Block.box(-16, 14, 0, 16, 16, 14),
            Block.box(-16, -32, 0, 16, -30, 14),
            Block.box(-16, -30, 0, -12, 14, 14),
            Block.box(12, -30, 0, 16, 14, 14)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape CLOSE_TOP_R_NORTH = Stream.of(
            Block.box(0, 14, 0, 32, 16, 14),
            Block.box(0, -32, 0, 32, -30, 14),
            Block.box(0, -30, 0, 4, 14, 14),
            Block.box(28, -30, 0, 32, 14, 14),
            Block.box(4, -30, 11, 28, 14, 12)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape OPEN_TOP_R_NORTH = Stream.of(
            Block.box(0, 14, 0, 32, 16, 14),
            Block.box(0, -32, 0, 32, -30, 14),
            Block.box(0, -30, 0, 4, 14, 14),
            Block.box(28, -30, 0, 32, 14, 14)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    // EAST/WEST are computed by rotating boxes +/-90° around (8,8,8).
    // EAST (SOUTH -> EAST): x' = z, z' = 16 - x
    // WEST (SOUTH -> WEST): x' = 16 - z, z' = x
    private static final VoxelShape CLOSE_DOWN_L_EAST = Stream.of(
            Block.box(2, 46, -16, 16, 48, 16),
            Block.box(2, 0, -16, 16, 2, 16),
            Block.box(2, 2, -16, 16, 46, -12),
            Block.box(2, 2, 12, 16, 46, 16),
            Block.box(4, 2, -12, 5, 46, 12)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape OPEN_DOWN_L_EAST = Stream.of(
            Block.box(2, 46, -16, 16, 48, 16),
            Block.box(2, 0, -16, 16, 2, 16),
            Block.box(2, 2, -16, 16, 46, -12),
            Block.box(2, 2, 12, 16, 46, 16)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape CLOSE_DOWN_L_WEST = Stream.of(
            Block.box(0, 46, 0, 14, 48, 32),
            Block.box(0, 0, 0, 14, 2, 32),
            Block.box(0, 2, 28, 14, 46, 32),
            Block.box(0, 2, 0, 14, 46, 4),
            Block.box(11, 2, 4, 12, 46, 28)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape OPEN_DOWN_L_WEST = Stream.of(
            Block.box(0, 46, 0, 14, 48, 32),
            Block.box(0, 0, 0, 14, 2, 32),
            Block.box(0, 2, 28, 14, 46, 32),
            Block.box(0, 2, 0, 14, 46, 4)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape CLOSE_DOWN_R_EAST = Stream.of(
            Block.box(2, 46, 0, 16, 48, 32),
            Block.box(2, 0, 0, 16, 2, 32),
            Block.box(2, 2, 0, 16, 46, 4),
            Block.box(2, 2, 28, 16, 46, 32),
            Block.box(4, 2, 4, 5, 46, 28)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape OPEN_DOWN_R_EAST = Stream.of(
            Block.box(2, 46, 0, 16, 48, 32),
            Block.box(2, 0, 0, 16, 2, 32),
            Block.box(2, 2, 0, 16, 46, 4),
            Block.box(2, 2, 28, 16, 46, 32)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape CLOSE_DOWN_R_WEST = Stream.of(
            Block.box(0, 46, -16, 14, 48, 16),
            Block.box(0, 0, -16, 14, 2, 16),
            Block.box(0, 2, 12, 14, 46, 16),
            Block.box(0, 2, -16, 14, 46, -12),
            Block.box(11, 2, -12, 12, 46, 12)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape OPEN_DOWN_R_WEST = Stream.of(
            Block.box(0, 46, -16, 14, 48, 16),
            Block.box(0, 0, -16, 14, 2, 16),
            Block.box(0, 2, 12, 14, 46, 16),
            Block.box(0, 2, -16, 14, 46, -12)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape CLOSE_CORE_EAST = Stream.of(
            Block.box(2, 30, -16, 16, 32, 16),
            Block.box(2, -16, -16, 16, -14, 16),
            Block.box(2, -14, -16, 16, 30, -12),
            Block.box(2, -14, 12, 16, 30, 16),
            Block.box(4, -14, -12, 5, 30, 12)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape OPEN_CORE_EAST = Stream.of(
            Block.box(2, 30, -16, 16, 32, 16),
            Block.box(2, -16, -16, 16, -14, 16),
            Block.box(2, -14, -16, 16, 30, -12),
            Block.box(2, -14, 12, 16, 30, 16)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape CLOSE_CORE_WEST = Stream.of(
            Block.box(0, 30, 0, 14, 32, 32),
            Block.box(0, -16, 0, 14, -14, 32),
            Block.box(0, -14, 28, 14, 30, 32),
            Block.box(0, -14, 0, 14, 30, 4),
            Block.box(11, -14, 4, 12, 30, 28)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape OPEN_CORE_WEST = Stream.of(
            Block.box(0, 30, 0, 14, 32, 32),
            Block.box(0, -16, 0, 14, -14, 32),
            Block.box(0, -14, 28, 14, 30, 32),
            Block.box(0, -14, 0, 14, 30, 4)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape CLOSE_MID_R_EAST = Stream.of(
            Block.box(2, 30, 0, 16, 32, 32),
            Block.box(2, -16, 0, 16, -14, 32),
            Block.box(2, -14, 0, 16, 30, 4),
            Block.box(2, -14, 28, 16, 30, 32),
            Block.box(4, -14, 4, 5, 30, 28)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape OPEN_MID_R_EAST = Stream.of(
            Block.box(2, 30, 0, 16, 32, 32),
            Block.box(2, -16, 0, 16, -14, 32),
            Block.box(2, -14, 0, 16, 30, 4),
            Block.box(2, -14, 28, 16, 30, 32)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape CLOSE_MID_R_WEST = Stream.of(
            Block.box(0, 30, -16, 14, 32, 16),
            Block.box(0, -16, -16, 14, -14, 16),
            Block.box(0, -14, 12, 14, 30, 16),
            Block.box(0, -14, -16, 14, 30, -12),
            Block.box(11, -14, -12, 12, 30, 12)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape OPEN_MID_R_WEST = Stream.of(
            Block.box(0, 30, -16, 14, 32, 16),
            Block.box(0, -16, -16, 14, -14, 16),
            Block.box(0, -14, 12, 14, 30, 16),
            Block.box(0, -14, -16, 14, 30, -12)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape CLOSE_TOP_L_EAST = Stream.of(
            Block.box(2, 14, -16, 16, 16, 16),
            Block.box(2, -32, -16, 16, -30, 16),
            Block.box(2, -30, -16, 16, 14, -12),
            Block.box(2, -30, 12, 16, 14, 16),
            Block.box(4, -30, -12, 5, 14, 12)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape OPEN_TOP_L_EAST = Stream.of(
            Block.box(2, 14, -16, 16, 16, 16),
            Block.box(2, -32, -16, 16, -30, 16),
            Block.box(2, -30, -16, 16, 14, -12),
            Block.box(2, -30, 12, 16, 14, 16)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape CLOSE_TOP_L_WEST = Stream.of(
            Block.box(0, 14, 0, 14, 16, 32),
            Block.box(0, -32, 0, 14, -30, 32),
            Block.box(0, -30, 28, 14, 14, 32),
            Block.box(0, -30, 0, 14, 14, 4),
            Block.box(11, -30, 4, 12, 14, 28)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape OPEN_TOP_L_WEST = Stream.of(
            Block.box(0, 14, 0, 14, 16, 32),
            Block.box(0, -32, 0, 14, -30, 32),
            Block.box(0, -30, 28, 14, 14, 32),
            Block.box(0, -30, 0, 14, 14, 4)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape CLOSE_TOP_R_EAST = Stream.of(
            Block.box(2, 14, 0, 16, 16, 32),
            Block.box(2, -32, 0, 16, -30, 32),
            Block.box(2, -30, 0, 16, 14, 4),
            Block.box(2, -30, 28, 16, 14, 32),
            Block.box(4, -30, 4, 5, 14, 28)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape OPEN_TOP_R_EAST = Stream.of(
            Block.box(2, 14, 0, 16, 16, 32),
            Block.box(2, -32, 0, 16, -30, 32),
            Block.box(2, -30, 0, 16, 14, 4),
            Block.box(2, -30, 28, 16, 14, 32)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape CLOSE_TOP_R_WEST = Stream.of(
            Block.box(0, 14, -16, 14, 16, 16),
            Block.box(0, -32, -16, 14, -30, 16),
            Block.box(0, -30, 12, 14, 14, 16),
            Block.box(0, -30, -16, 14, 14, -12),
            Block.box(11, -30, -12, 12, 14, 12)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape OPEN_TOP_R_WEST = Stream.of(
            Block.box(0, 14, -16, 14, 16, 16),
            Block.box(0, -32, -16, 14, -30, 16),
            Block.box(0, -30, 12, 14, 14, 16),
            Block.box(0, -30, -16, 14, 14, -12)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape CLOSE_MID_R_NORTH = Stream.of(
            Block.box(0, 30, 0, 32, 32, 14),
            Block.box(0, -16, 0, 32, -14, 14),
            Block.box(28, -14, 0, 32, 30, 14),
            Block.box(0, -14, 0, 4, 30, 14),
            Block.box(4, -14, 11, 28, 30, 12)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape OPEN_MID_R_NORTH = Stream.of(
            Block.box(0, 30, 0, 32, 32, 14),
            Block.box(0, -16, 0, 32, -14, 14),
            Block.box(28, -14, 0, 32, 30, 14),
            Block.box(0, -14, 0, 4, 30, 14)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    static VoxelShape getShape(BlockState state) {
        if (state.getBlock().equals(EndoshellBlocks.TARDIS_DEFAULT_EXT_DOOR)) {
            DefaultExtDoorInteract.Pos pos = state.getValue(DefaultExtDoorInteract.POS);
            Direction facing = state.getValue(DefaultExtDoorInteract.FACING);
            if (facing == Direction.DOWN || facing == Direction.UP)
                return Block.box(0, 0, 0, 16, 16, 16);
            if (!state.getValue(OPEN))
                return CLOSE_SHAPES.get(new Vector2i(pos.x(), pos.y())).get(facing);
            else
                return OPEN_SHAPES.get(new Vector2i(pos.x(), pos.y())).get(facing);
        } else {
            Direction facing = state.getValue(DefaultExtDoorCore.FACING);
            if (!state.getValue(OPEN))
                return CLOSE_SHAPES.get(new Vector2i(0, 1)).get(facing);
            else
                return OPEN_SHAPES.get(new Vector2i(0, 1)).get(facing);
        }
    }

    static {
        CLOSE_SHAPES = Map.of(
                new Vector2i(0, 0), Map.of(
                        Direction.SOUTH, CLOSE_DOWN_L_SOUTH,
                        Direction.NORTH, CLOSE_DOWN_L_NORTH,
                        Direction.EAST, CLOSE_DOWN_L_EAST,
                        Direction.WEST, CLOSE_DOWN_L_WEST),
                new Vector2i(1, 0), Map.of(
                        Direction.SOUTH, CLOSE_DOWN_R_SOUTH,
                        Direction.NORTH, CLOSE_DOWN_R_NORTH,
                        Direction.EAST, CLOSE_DOWN_R_EAST,
                        Direction.WEST, CLOSE_DOWN_R_WEST),
                new Vector2i(0, 1), Map.of(
                        Direction.SOUTH, CLOSE_CORE_SOUTH,
                        Direction.NORTH, CLOSE_CORE_NORTH,
                        Direction.EAST, CLOSE_CORE_EAST,
                        Direction.WEST, CLOSE_CORE_WEST),
                new Vector2i(1, 1), Map.of(
                        Direction.SOUTH, CLOSE_MID_R_SOUTH,
                        Direction.NORTH, CLOSE_MID_R_NORTH,
                        Direction.EAST, CLOSE_MID_R_EAST,
                        Direction.WEST, CLOSE_MID_R_WEST),
                new Vector2i(0, 2), Map.of(
                        Direction.SOUTH, CLOSE_TOP_L_SOUTH,
                        Direction.NORTH, CLOSE_TOP_L_NORTH,
                        Direction.EAST, CLOSE_TOP_L_EAST,
                        Direction.WEST, CLOSE_TOP_L_WEST),
                new Vector2i(1, 2), Map.of(
                        Direction.SOUTH, CLOSE_TOP_R_SOUTH,
                        Direction.NORTH, CLOSE_TOP_R_NORTH,
                        Direction.EAST, CLOSE_TOP_R_EAST,
                        Direction.WEST, CLOSE_TOP_R_WEST));

        OPEN_SHAPES = Map.of(
                new Vector2i(0, 0), Map.of(
                        Direction.SOUTH, OPEN_DOWN_L_SOUTH,
                        Direction.NORTH, OPEN_DOWN_L_NORTH,
                        Direction.EAST, OPEN_DOWN_L_EAST,
                        Direction.WEST, OPEN_DOWN_L_WEST),
                new Vector2i(1, 0), Map.of(
                        Direction.SOUTH, OPEN_DOWN_R_SOUTH,
                        Direction.NORTH, OPEN_DOWN_R_NORTH,
                        Direction.EAST, OPEN_DOWN_R_EAST,
                        Direction.WEST, OPEN_DOWN_R_WEST),
                new Vector2i(0, 1), Map.of(
                        Direction.SOUTH, OPEN_CORE_SOUTH,
                        Direction.NORTH, OPEN_CORE_NORTH,
                        Direction.EAST, OPEN_CORE_EAST,
                        Direction.WEST, OPEN_CORE_WEST),
                new Vector2i(1, 1), Map.of(
                        Direction.SOUTH, OPEN_MID_R_SOUTH,
                        Direction.NORTH, OPEN_MID_R_NORTH,
                        Direction.EAST, OPEN_MID_R_EAST,
                        Direction.WEST, OPEN_MID_R_WEST),
                new Vector2i(0, 2), Map.of(
                        Direction.SOUTH, OPEN_TOP_L_SOUTH,
                        Direction.NORTH, OPEN_TOP_L_NORTH,
                        Direction.EAST, OPEN_TOP_L_EAST,
                        Direction.WEST, OPEN_TOP_L_WEST),
                new Vector2i(1, 2), Map.of(
                        Direction.SOUTH, OPEN_TOP_R_SOUTH,
                        Direction.NORTH, OPEN_TOP_R_NORTH,
                        Direction.EAST, OPEN_TOP_R_EAST,
                        Direction.WEST, OPEN_TOP_R_WEST));
    }
}
