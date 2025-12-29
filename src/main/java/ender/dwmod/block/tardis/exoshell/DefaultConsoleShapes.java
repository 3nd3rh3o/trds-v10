package ender.dwmod.block.tardis.exoshell;

import java.util.Map;
import java.util.stream.Stream;

import ender.dwmod.block.tardis.exoshell.DefaultConsoleInteract.Pos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DefaultConsoleShapes {
    static Map<Pos, VoxelShape> SHAPES_DOWN = Map.of(
        Pos.N, Block.box(0, 0, 0, 16, 16, 11),
        Pos.NE, Stream.of(
            Block.box(13, 0, 0, 16, 16, 11),
            Block.box(12, 0, 2, 13, 16, 9),
            Block.box(10, 0, 2, 12, 16, 6),
            Block.box(9, 0, 4, 10, 16, 5),
            Block.box(11, 0, 6, 12, 16, 7),
            Block.box(8, 0, 2, 10, 16, 4),
            Block.box(7, 0, 0, 13, 16, 2)
        ).reduce(Shapes::or).get(),
        Pos.NW, Stream.of(
            Block.box(0, 0, 0, 3, 16, 11),
            Block.box(3, 0, 2, 4, 16, 9),
            Block.box(4, 0, 2, 6, 16, 6),
            Block.box(6, 0, 4, 7, 16, 5),
            Block.box(4, 0, 6, 5, 16, 7),
            Block.box(6, 0, 2, 8, 16, 4),
            Block.box(3, 0, 0, 9, 16, 2)
        ).reduce(Shapes::or).get(),
        Pos.S, Block.box(0, 0, 5, 16, 16, 16),
        Pos.SE, Stream.of(
            Block.box(13, 0, 5, 16, 16, 16),
            Block.box(12, 0, 7, 13, 16, 14),
            Block.box(10, 0, 10, 12, 16, 14),
            Block.box(9, 0, 11, 10, 16, 12),
            Block.box(11, 0, 9, 12, 16, 10),
            Block.box(8, 0, 12, 10, 16, 14),
            Block.box(7, 0, 14, 13, 16, 16)
        ).reduce((v1, v2) -> Shapes.or(v1, v2)).get(),
        Pos.SW, Stream.of(
            Block.box(0, 0, 5, 3, 16, 16),
            Block.box(3, 0, 7, 4, 16, 14),
            Block.box(4, 0, 10, 6, 16, 14),
            Block.box(6, 0, 11, 7, 16, 12),
            Block.box(4, 0, 9, 5, 16, 10),
            Block.box(6, 0, 12, 8, 16, 14),
            Block.box(3, 0, 14, 9, 16, 16)
        ).reduce(Shapes::or).get(),
        Pos.E, Stream.of(
            Block.box(7, 0, 0, 16, 16, 16),
            Block.box(5, 0, 3, 7, 16, 13),
            Block.box(4, 0, 4, 5, 16, 5),
            Block.box(4, 0, 11, 5, 16, 12),
            Block.box(6, 0, 2, 7, 16, 3),
            Block.box(6, 0, 13, 7, 16, 14),
            Block.box(3, 0, 5, 5, 16, 11),
            Block.box(2, 0, 7, 3, 16, 9)
        ).reduce((v1, v2) -> Shapes.or(v1, v2)).get(),
        Pos.W, Stream.of(
            Block.box(0, 0, 0, 9, 16, 16),
            Block.box(9, 0, 3, 11, 16, 13),
            Block.box(11, 0, 4, 12, 16, 5),
            Block.box(11, 0, 11, 12, 16, 12),
            Block.box(9, 0, 2, 10, 16, 3),
            Block.box(9, 0, 13, 10, 16, 14),
            Block.box(11, 0, 5, 13, 16, 11),
            Block.box(13, 0, 7, 14, 16, 9)
        ).reduce(Shapes::or).get(),
        Pos.NONE, Block.box(0, 0, 0, 16, 16, 16),
        Pos.C, Block.box(0, 0, 0, 16, 16, 16)
    );

    static Map<Pos, VoxelShape> SHAPES_UP = Map.of(
        Pos.N, Block.box(0, 0, 0, 16, 4, 11),
        Pos.NE, Stream.of(
            Block.box(13, 0, 0, 16, 4, 11),
            Block.box(12, 0, 2, 13, 4, 9),
            Block.box(10, 0, 2, 12, 4, 6),
            Block.box(9, 0, 4, 10, 4, 5),
            Block.box(11, 0, 6, 12, 4, 7),
            Block.box(8, 0, 2, 10, 4, 4),
            Block.box(7, 0, 0, 13, 4, 2)
        ).reduce(Shapes::or).get(),
        Pos.NW, Stream.of(
            Block.box(0, 0, 0, 3, 4, 11),
            Block.box(3, 0, 2, 4, 4, 9),
            Block.box(4, 0, 2, 6, 4, 6),
            Block.box(6, 0, 4, 7, 4, 5),
            Block.box(4, 0, 6, 5, 4, 7),
            Block.box(6, 0, 2, 8, 4, 4),
            Block.box(3, 0, 0, 9, 4, 2)
        ).reduce(Shapes::or).get(),
        Pos.S, Block.box(0, 0, 5, 16, 4, 16),
        Pos.SE, Stream.of(
            Block.box(13, 0, 5, 16, 4, 16),
            Block.box(12, 0, 7, 13, 4, 14),
            Block.box(10, 0, 10, 12, 4, 14),
            Block.box(9, 0, 11, 10, 4, 12),
            Block.box(11, 0, 9, 12, 4, 10),
            Block.box(8, 0, 12, 10, 4, 14),
            Block.box(7, 0, 14, 13, 4, 16)
        ).reduce((v1, v2) -> Shapes.or(v1, v2)).get(),
        Pos.SW, Stream.of(
            Block.box(0, 0, 5, 3, 4, 16),
            Block.box(3, 0, 7, 4, 4, 14),
            Block.box(4, 0, 10, 6, 4, 14),
            Block.box(6, 0, 11, 7, 4, 12),
            Block.box(4, 0, 9, 5, 4, 10),
            Block.box(6, 0, 12, 8, 4, 14),
            Block.box(3, 0, 14, 9, 4, 16)
        ).reduce(Shapes::or).get(),
        Pos.E, Stream.of(
            Block.box(7, 0, 0, 16, 4, 16),
            Block.box(5, 0, 3, 7, 4, 13),
            Block.box(4, 0, 4, 5, 4, 5),
            Block.box(4, 0, 11, 5, 4, 12),
            Block.box(6, 0, 2, 7, 4, 3),
            Block.box(6, 0, 13, 7, 4, 14),
            Block.box(3, 0, 5, 5, 4, 11),
            Block.box(2, 0, 7, 3, 4, 9)
        ).reduce((v1, v2) -> Shapes.or(v1, v2)).get(),
        Pos.W, Stream.of(
            Block.box(0, 0, 0, 9, 4, 16),
            Block.box(9, 0, 3, 11, 4, 13),
            Block.box(11, 0, 4, 12, 4, 5),
            Block.box(11, 0, 11, 12, 4, 12),
            Block.box(9, 0, 2, 10, 4, 3),
            Block.box(9, 0, 13, 10, 4, 14),
            Block.box(11, 0, 5, 13, 4, 11),
            Block.box(13, 0, 7, 14, 4, 9)
        ).reduce(Shapes::or).get(),
        Pos.NONE, Block.box(0, 0, 0, 16, 4, 16),
        Pos.C, Block.box(0, 0, 0, 16, 4, 16)
    );

}
