package ender.dwmod.tardis.systems.architecturalReconfiguration;

import java.util.ArrayList;
import java.util.List;

import com.google.common.primitives.UnsignedInteger;

import ender.dwmod.DwMod;
import ender.dwmod.block.tardis.lamps.TardisLamp;
import ender.dwmod.dimensions.DimensionRegistry;
import ender.dwmod.tardis.TardisRegistries;
import ender.dwmod.tardis.systems.ArchitecturalReconfiguration;
import ender.dwmod.utils.Vec2IToInt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class Room {
    private final BlockPos virtualPosition;
    private final BlockPos worldPosition;
    private final Vec3i size;
    private final String structureName;

    private final boolean northConnected;
    private final boolean southConnected;
    private final boolean eastConnected;
    private final boolean westConnected;
    private final boolean upConnected;
    private final boolean downConnected;

    private boolean consoleRoom = false;

    private Room(BlockPos virtualPosition, BlockPos worldPosition, Vec3i size, String structureName,
            boolean northConnected, boolean southConnected, boolean eastConnected,
            boolean westConnected, boolean upConnected, boolean downConnected) {
        this.virtualPosition = virtualPosition;
        this.worldPosition = worldPosition;
        this.size = size;
        this.structureName = structureName;
        this.northConnected = northConnected;
        this.southConnected = southConnected;
        this.eastConnected = eastConnected;
        this.westConnected = westConnected;
        this.upConnected = upConnected;
        this.downConnected = downConnected;
    }

    public CompoundTag toNBT() {
        CompoundTag roomTag = new CompoundTag();
        roomTag.putIntArray("virtual_pos", new int[]{virtualPosition.getX(), virtualPosition.getY(), virtualPosition.getZ()});
        roomTag.putIntArray("world_pos", new int[]{worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()});
        roomTag.putIntArray("size", new int[]{size.getX(), size.getY(), size.getZ()});
        roomTag.putString("structure_name", structureName);
        roomTag.putByteArray("connectivity", new byte[]{
            (byte) (northConnected ? 1 : 0),
            (byte) (southConnected ? 1 : 0),
            (byte) (eastConnected ? 1 : 0),
            (byte) (westConnected ? 1 : 0),
            (byte) (upConnected ? 1 : 0),
            (byte) (downConnected ? 1 : 0)
        });

        return roomTag;
    }

    public static Room fromNBT(CompoundTag roomTag) {
        int[] virtualPosArray = roomTag.getIntArray("virtual_pos");
        BlockPos virtualPosition = new BlockPos(virtualPosArray[0], virtualPosArray[1], virtualPosArray[2]);

        int[] worldPosArray = roomTag.getIntArray("world_pos");
        BlockPos worldPosition = new BlockPos(worldPosArray[0], worldPosArray[1], worldPosArray[2]);

        int[] sizeArray = roomTag.getIntArray("size");
        Vec3i size = new Vec3i(sizeArray[0], sizeArray[1], sizeArray[2]);

        String structureName = roomTag.getString("structure_name");

        byte[] connectivityArray = roomTag.getByteArray("connectivity");
        boolean northConnected = connectivityArray[0] != 0;
        boolean southConnected = connectivityArray[1] != 0;
        boolean eastConnected = connectivityArray[2] != 0;
        boolean westConnected = connectivityArray[3] != 0;
        boolean upConnected = connectivityArray[4] != 0;
        boolean downConnected = connectivityArray[5] != 0;

        return new Room(virtualPosition, worldPosition, size, structureName,
                northConnected, southConnected, eastConnected,
                westConnected, upConnected, downConnected);
    }

    public BlockPos getVirtualPosition() {
        return virtualPosition;
    }

    public Vec3i getSize() {
        return size;
    }

    public BlockPos getWorldPosition() {
        return worldPosition;
    }

    public static Room createRoom(BlockPos virtualPosition2, BlockPos worldPosition2, Vec3i size2,
            String name, byte[] connectivity) {
        return new Room(virtualPosition2, worldPosition2, size2, name,
                connectivity[0] != 0,
                connectivity[1] != 0,
                connectivity[2] != 0,
                connectivity[3] != 0,
                connectivity[4] != 0,
                connectivity[5] != 0
            );
    }

    public void tick(MinecraftServer server) {
        updateLighting(server);
    }

    public boolean shouldDeactivate(MinecraftServer server, List<Room> rooms) {
        AABB roomAABB = new AABB(
            worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(),
            worldPosition.getX() + (size.getX() * 32), worldPosition.getY() + (size.getY() * 32), worldPosition.getZ() + (size.getZ() * 32)
        );
        List<ServerPlayer> players = server.getLevel(DimensionRegistry.VORTEX_DIMENSION_KEY).getPlayers(p -> roomAABB.contains(p.position()));
        if (players.size() == 0) {
            return true;
        }
        return false;
    }

    public boolean shouldActivate(MinecraftServer server, List<Room> rooms) {
        // is there a player in the room? if yes, activate
        AABB roomAABB = new AABB(
            worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(),
            worldPosition.getX() + (size.getX() * 32), worldPosition.getY() + (size.getY() * 32), worldPosition.getZ() + (size.getZ() * 32)
        );
        List<ServerPlayer> players = server.getLevel(DimensionRegistry.VORTEX_DIMENSION_KEY).getPlayers(p -> roomAABB.contains(p.position()));
        if (players.size() > 0) {
            // run activation here!
            DwMod.LOGGER.info("Activating room " + structureName + " at virtual position " + virtualPosition);
            return true;
        }
        return false;
    }

    public void postGeneration(MinecraftServer server, List<Room> rooms) {
        
        // if console room, obligatory forceload
        if (isConsoleRoom()) {
            setForceLoad(server, worldPosition, size, true);
        }
        // else, if adjacent is active, forceload too
        //TODO

        // on finish, collect adjacent rooms if present, and their registries
        updateLighting(server);
    }

    public List<Room> roomsToSleep() {
        // rooms to sleep when this room deactivates, ignore control room since it's connected to the exterior.
        return new ArrayList<>();
    }

    public boolean isConsoleRoom() {
        return consoleRoom;
    }

    public void setAsConsoleRoom() {
        this.consoleRoom = true;
    }

    public String getStructureName() {
        return structureName;
    }

    public static UnsignedInteger worldPosToInstanceID(BlockPos worldPosition) {
        long volumeX = Math.floorDiv(worldPosition.getX(), ArchitecturalReconfiguration.MAX_VOLUME_BLOCKS.getX());
        long volumeZ = Math.floorDiv(worldPosition.getZ(), ArchitecturalReconfiguration.MAX_VOLUME_BLOCKS.getZ());
        return UnsignedInteger.valueOf(Vec2IToInt.z2ToN(volumeX, volumeZ));
    }

    public void setInactive(MinecraftServer server) {
        setForceLoad(server, worldPosition, size, false);
    }




    private void updateLighting(MinecraftServer server)
    {
        boolean lit = TardisRegistries.getTardis(worldPosToInstanceID(worldPosition)).getInternalLight();
        for (int x = 0; x < size.getX() * ArchitecturalReconfiguration.VOLUME_BLOCKS; x++)
        {
            for (int y = 0; y < size.getY() * ArchitecturalReconfiguration.VOLUME_BLOCKS; y++)
            {
                for (int z = 0; z < size.getZ() * ArchitecturalReconfiguration.VOLUME_BLOCKS; z++)
                {
                    BlockPos pos = worldPosition.offset(x, y, z);
                    BlockState b = server.getLevel(DimensionRegistry.VORTEX_DIMENSION_KEY).getBlockState(pos);
                    if (b.getBlock() instanceof TardisLamp)
                    {
                        server.getLevel(DimensionRegistry.VORTEX_DIMENSION_KEY).setBlock(pos, b.setValue(TardisLamp.LIT, lit), Block.UPDATE_ALL);
                    }
                }
            }
        }
        
    }

    private static void setForceLoad(MinecraftServer server, BlockPos worldPosition, Vec3i size, boolean forceload)
    {
        // Compute range of chunks to forceload from dimX and dimZ
        int chunkXStart = Math.floorDiv(worldPosition.getX(), 16);
        int chunkZStart = Math.floorDiv(worldPosition.getZ(), 16);
        int chunkXEnd = Math.floorDiv(worldPosition.getX() + size.getX() * ArchitecturalReconfiguration.VOLUME_BLOCKS - 1, 16);
        int chunkZEnd = Math.floorDiv(worldPosition.getZ() + size.getZ() * ArchitecturalReconfiguration.VOLUME_BLOCKS - 1, 16);

        // Apply forceload or un-forceload
        for (int cx = chunkXStart; cx <= chunkXEnd; cx++) {
            for (int cz = chunkZStart; cz <= chunkZEnd; cz++) 
            {
                server.getLevel(DimensionRegistry.VORTEX_DIMENSION_KEY).setChunkForced(cx, cz, forceload);
            }
        }
    }
}
