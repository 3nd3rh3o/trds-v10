package ender.dwmod.tardis.systems;

import java.util.ArrayList;
import java.util.List;
import ender.dwmod.DwMod;
import ender.dwmod.dimensions.DimensionRegistry;
import ender.dwmod.tardis.systems.architecturalReconfiguration.Room;
import ender.dwmod.utils.StructurePlacer;
import ender.dwmod.utils.StructureTask;
import ender.dwmod.utils.Vec2IToInt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;

public class ArchitecturalReconfiguration {
    public static final String CATEGORY = "architectural_reconfiguration";
    private final int id;
    private int consoleRoomIndex = -1;
    private final List<Room> rooms = new ArrayList<>();
    // virtual space is [0 -> 8]^3
    // one volume is 32x32x32 blocks
    // the tardis interior 16 x 8 x 16 volumes. => 512 x 256 x 512 blocks
    private static final Vec3i MAX_VOLUME = new Vec3i(16, 8, 16);
    private static final int VOLUME_BLOCKS = 32;

    private static final Vec3i MAX_VOLUME_BLOCKS = MAX_VOLUME.multiply(VOLUME_BLOCKS);


    private final List<Room> activeRooms = new ArrayList<>();
    private final List<Room> standByRooms = new ArrayList<>();
    private final List<Room> roomPostGen = new ArrayList<>();



    public ArchitecturalReconfiguration(int id) {
        this.id = id;
    }

    public void readNBT(CompoundTag compound) {
        rooms.clear();
        for (String key : compound.getAllKeys()) {
            if (key.equals("console_room_index"))
            {
                consoleRoomIndex = compound.getInt("console_room_index");
                continue;
            }
            CompoundTag roomTag = compound.getCompound(key);
            
            Room room = Room.fromNBT(roomTag);
            rooms.add(room);
        }
        if (consoleRoomIndex >= 0 && consoleRoomIndex < rooms.size())
        {
            rooms.get(consoleRoomIndex).setAsConsoleRoom();
            standByRooms.add(rooms.get(consoleRoomIndex));
        }
    }

    public Tag toNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("console_room_index", consoleRoomIndex);
        for (int i = 0; i < rooms.size(); i++)
        {
            nbt.put(String.valueOf(i), rooms.get(i).toNBT());
        }
        return nbt;
    }
    // add room to registries, and generate it in the tardis world
    public void createRoom(String nameSpace, String[] path, Vec3i size, BlockPos virtualPosition, String name, byte[] connectivity, boolean keepForceloaded, boolean isFirstRoom) {
        // checks
        if (virtualPosition.getX() < 0 || virtualPosition.getY() < 0 || virtualPosition.getZ() < 0
           || virtualPosition.getX() > 8 || virtualPosition.getY() > 8 || virtualPosition.getZ() > 8) {
            DwMod.LOGGER.error("Room topologycal position out of bounds!");
            return;
        }

        if (path.length != size.getX() * size.getY() * size.getZ()) {
            DwMod.LOGGER.error("Room structure path array size does not match room size!");
            return;
        }

        for (Room r : rooms) {
            if (r.getVirtualPosition().equals(virtualPosition)) {
                DwMod.LOGGER.error("Room topologycal position already occupied!");
                return;
            }
        }


        boolean[][][] occupied = new boolean[MAX_VOLUME.getX()][MAX_VOLUME.getY()][MAX_VOLUME.getZ()];
        for (Room r : rooms) {
            for (int x = 0; x < r.getSize().getX(); x++) {
                for (int y = 0; y < r.getSize().getY(); y++) {
                    for (int z = 0; z < r.getSize().getZ(); z++) {
                        BlockPos pos = toVolumePos(r.getWorldPosition()).offset(x, y, z);
                        occupied[pos.getX()][pos.getY()][pos.getZ()] = true;
                    }
                }
            }
        }

        for (int x = 0; x <= MAX_VOLUME.getX() - size.getX(); x++) {
            for (int y = 0; y <= MAX_VOLUME.getY() - size.getY(); y++) {
                for (int z = 0; z <= MAX_VOLUME.getZ() - size.getZ(); z++) {
                    BlockPos candidate = new BlockPos(x, y, z);
                    boolean fits = true;
                    for (int dx = 0; dx < size.getX(); dx++) {
                        for (int dy = 0; dy < size.getY(); dy++) {
                            for (int dz = 0; dz < size.getZ(); dz++) {
                                if (occupied[candidate.getX() + dx][candidate.getY() + dy][candidate.getZ() + dz]) {
                                    fits = false;
                                    break;
                                }
                            }
                            if (!fits) break;
                        }
                        if (!fits) break;
                    }
                    if (fits) {
                        // place room (it use a chain of callbacks, then commit to rooms list when done)
                        BlockPos worldPosition = fromVolumePos(candidate);

                        Runnable onFinish = () -> {
                            Room r = Room.createRoom(virtualPosition, worldPosition, size, name, connectivity);
                            if (isFirstRoom) {
                                consoleRoomIndex = rooms.size();
                                r.setAsConsoleRoom();
                            }
                            roomPostGen.add(r);
                            rooms.add(r);
                            DwMod.LOGGER.info("Room " + name + " placed at world position " + worldPosition + " in Tardis ID " + id);
                        };
                        for (int xP = 0; xP < size.getX(); xP++)
                        {
                            for (int yP = 0; yP < size.getY(); yP++)
                            {
                                for (int zP = 0; zP < size.getZ(); zP++)
                                {
                                    // theise are for the current part
                                    BlockPos pos = new BlockPos(
                                        worldPosition.getX() + (xP * 32),
                                        worldPosition.getY() + (yP * 32),
                                        worldPosition.getZ() + (zP * 32)
                                    );
                                    String structurePath = path[xP + yP * size.getX() + zP * size.getX() * size.getY()];
                                    final Runnable next = onFinish;
                                    onFinish = () -> {
                                        StructurePlacer.addTask(new StructureTask(
                                            nameSpace,
                                            structurePath,
                                            pos,
                                            DimensionRegistry.VORTEX_DIMENSION_KEY,
                                            !keepForceloaded,
                                            next
                                        ));
                                    };
                                }
                            }
                        }
                        onFinish.run();
                        DwMod.LOGGER.info("Sheduled room placement at virtual position " + virtualPosition + " in Tardis ID " + id);
                        return;
                    }
                }
            }
        }

        DwMod.LOGGER.error("No space available to place room!");        
    }

    private BlockPos toVolumePos(BlockPos worldPosition) {
        long[] p = Vec2IToInt.nToZ2(id);
        int offsetX = (int) p[0] * MAX_VOLUME_BLOCKS.getX();
        int offsetZ = (int) p[1] * MAX_VOLUME_BLOCKS.getZ();

        return new BlockPos(
            (worldPosition.getX() - offsetX) / VOLUME_BLOCKS,
            worldPosition.getY() / VOLUME_BLOCKS,
            (worldPosition.getZ() - offsetZ) / VOLUME_BLOCKS
        );
    } 

    private BlockPos fromVolumePos(BlockPos volumePosition) {
        long[] p = Vec2IToInt.nToZ2(id);
        int offsetX = (int) p[0] * MAX_VOLUME_BLOCKS.getX();
        int offsetZ = (int) p[1] * MAX_VOLUME_BLOCKS.getZ();

        return new BlockPos(
            volumePosition.getX() * VOLUME_BLOCKS + offsetX,
            volumePosition.getY() * VOLUME_BLOCKS,
            volumePosition.getZ() * VOLUME_BLOCKS + offsetZ
        );
    }


    public void tick(MinecraftServer server) {
        for (int i = 0; i < activeRooms.size(); i++)
        {
            Room room = activeRooms.get(i);
            room.tick(server);
            if (room.shouldDeactivate(server, rooms))
            {
                DwMod.LOGGER.info("Deactivating room " + room.getStructureName() + " at virtual position " + room.getVirtualPosition());
                standByRooms.removeAll(room.roomsToSleep());
                standByRooms.add(room);
                activeRooms.remove(i);
            }
        }

        for (int i = 0; i < standByRooms.size(); i++)
        {
            Room room = standByRooms.get(i);
            if (room.shouldActivate(server, rooms)) {
                DwMod.LOGGER.info("Activating room " + room.getStructureName() + " at virtual position " + room.getVirtualPosition());
                activeRooms.add(room);
                standByRooms.remove(i);
            }
        }

        for (int i = 0; i < roomPostGen.size(); i++)
        {
            Room room = roomPostGen.get(i);
            room.postGeneration(server, rooms);
            if (room.isConsoleRoom())
            {
                standByRooms.add(room);
            }
            roomPostGen.remove(room);
        }
    }
}
