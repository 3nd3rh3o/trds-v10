package ender.dwmod.tardis;


import com.google.common.primitives.UnsignedInteger;

import ender.dwmod.entities.tardis.exoshell.TardisEntity;
import ender.dwmod.tardis.networking.EncodingHelpers;
import ender.dwmod.tardis.systems.ArchitecturalReconfiguration;
import ender.dwmod.tardis.systems.architecturalReconfiguration.Rooms;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Tardis {
    private UnsignedInteger id;
    private Vec3 position;
    private ResourceKey<Level> dimension;
    private ArchitecturalReconfiguration ars;

    private Tardis() {

    }


    // Used for NBT loading -> No events triggered.
    public Tardis(CompoundTag compound) {
        this.id = UnsignedInteger.fromIntBits(compound.getInt("id"));
        this.position = new Vec3(
                compound.getDouble("posX"),
                compound.getDouble("posY"),
                compound.getDouble("posZ")
        );
        this.dimension = ResourceKey.create(
                Registries.DIMENSION,
                ResourceLocation.fromNamespaceAndPath(
                        compound.getString("dimNamespace"),
                        compound.getString("dimPath")
                )
        );
        this.ars = new ArchitecturalReconfiguration(id.intValue());
        this.ars.readNBT(compound.getCompound("ars"));
    }

    // Used for NBT saving -> No events triggered.
    public CompoundTag toNBT() {
        CompoundTag compound = new CompoundTag();
        compound.putInt("id", id.intValue());
        compound.putDouble("posX", position.x);
        compound.putDouble("posY", position.y);
        compound.putDouble("posZ", position.z);
        compound.putString("dimNamespace", dimension.location().getNamespace());
        compound.putString("dimPath", dimension.location().getPath());
        compound.put("ars", ars.toNBT());
        return compound;
    }

    // Used for creating a new Tardis -> Will trigger events.
    public static Tardis create(UnsignedInteger id, Vec3 position, ResourceKey<Level> dimension) {
         Tardis t = new Tardis();
         t.id = id;
         t.position = position;
         t.dimension = dimension;
         t.ars = new ArchitecturalReconfiguration(id.intValue());
         return t;
    }

    // Return this Tardis's ID
    public UnsignedInteger id() {
        return id;
    }

    // only called by entity, when warping, use setPosition instead !
    public void updatePosition(TardisEntity tardisEntity) {
        this.position = tardisEntity.position();
        for (ServerPlayer player : PlayerLookup.all(tardisEntity.getServer()))
        {
            ServerPlayNetworking.send(player, TardisRegistries.createValueNotifyPacket(id, "exoshell", "position", EncodingHelpers.fromVec3(tardisEntity.position())));
        }
    }



    // ONLY ON CLIENT SIDE

    public void clientSyncPosition(Vec3 vec3) {
        this.position = vec3;
    }


    public void firstSpawn() {
        // generate the default console room

            ars.createRoom(
                Rooms.CONSOLE_ROOM.DEFAULT.NAMESPACE,
                Rooms.getPathArray(
                    Rooms.CONSOLE_ROOM.DEFAULT.PATH, 
                    Rooms.CONSOLE_ROOM.DEFAULT.SIZE),
                Rooms.CONSOLE_ROOM.DEFAULT.SIZE,
                new BlockPos(4, 4, 4),
                Rooms.CONSOLE_ROOM.DEFAULT.NAME,
                Rooms.CONSOLE_ROOM.DEFAULT.CONNECTIVITY,
                true,
                true
            );

    }

    public void tick(MinecraftServer server)
    {
        ars.tick(server);
    }

}
