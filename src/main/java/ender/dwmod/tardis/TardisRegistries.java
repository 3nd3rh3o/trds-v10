package ender.dwmod.tardis;

import java.util.ArrayList;
import java.util.List;

import com.google.common.primitives.UnsignedInteger;
import ender.dwmod.DwMod;
import ender.dwmod.entities.tardis.exoshell.TardisEntity;
import ender.dwmod.tardis.networking.TardisDataSyncS2C;
import ender.dwmod.tardis.networking.TardisUpdateValueS2C;
import ender.dwmod.tardis.systems.architecturalReconfiguration.Room;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public final class TardisRegistries {
    
    private static final List<Tardis> tardis = new ArrayList<>();
    public static List<Tardis> get()
    {
        return tardis;
    }

    public static void onWorldJoin(MinecraftServer server)
    {
        tardis.clear();
        TardisPersistentState state = TardisPersistentState.get(server);
        List<Tardis> trdss = state.getAllTardisData();
        tardis.addAll(trdss);
    }

    public static void onWorldLeave(MinecraftServer server)
    {
        TardisPersistentState state = TardisPersistentState.get(server);
        state.pushAllTardisData(tardis);
        tardis.clear();
    }


    public static Tardis createTardis(TardisEntity entity)
    {
        final Tardis t = Tardis.create(freeID(), entity.position(), entity.level().dimension());
        tardis.add(t);
        
        t.firstSpawn();
        

        
        for (ServerPlayer player : PlayerLookup.all(entity.getServer()))
        {
            ServerPlayNetworking.send(player, TardisRegistries.createSyncPacket());
        }
        return t;
    }

    public static Tardis getTardis(UnsignedInteger id)
    {
        for(Tardis t : tardis)
        {
            if(t.id().equals(id))
            {
                return t;
            }
        }
        throw new NullPointerException();
    }

    private static UnsignedInteger freeID()
    {
        UnsignedInteger id = UnsignedInteger.ZERO;
        boolean found = false;
        while(!found)
        {
            final UnsignedInteger checkID = id;
            if(tardis.stream().noneMatch(t -> t.id().equals(checkID)))
            {
                found = true;
            }
            else
            {
                id = id.plus(UnsignedInteger.ONE);
            }
        }
        return id;
    }

    public static void tick(MinecraftServer server)
    {
        for (Tardis t : tardis)
        {
            t.tick(server);
        }
    }


    public static void deleteTardis(UnsignedInteger iD, MinecraftServer server) {
        // Trigger cleanup of internal dimension etc....
        getTardis(iD).delete(server);
        tardis.removeIf(t -> t.id().equals(iD));
        DwMod.LOGGER.info("Tardis with ID " + iD + " deleted from registries.");
    }
    // Create a sync packet containing all {@link Tardis}, to let the client use them for visual effects.
    public static TardisDataSyncS2C createSyncPacket() {
        CompoundTag tag = TardisPersistentState.toNBT(tardis);
        return new TardisDataSyncS2C(tag);
    }

    public static CustomPacketPayload createValueNotifyPacket(UnsignedInteger id, String category, String name, String value) {
        return new TardisUpdateValueS2C(id.intValue(), category, name, value);
    }

    public static Tardis get(BlockPos worldPosition) {
        return getTardis(Room.worldPosToInstanceID(worldPosition));
    }
}
