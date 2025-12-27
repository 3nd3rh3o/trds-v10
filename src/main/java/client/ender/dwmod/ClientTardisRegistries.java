package client.ender.dwmod;

import java.util.ArrayList;
import java.util.List;

import com.google.common.primitives.UnsignedInteger;

import ender.dwmod.tardis.Tardis;
import ender.dwmod.tardis.TardisPersistentState;
import ender.dwmod.tardis.networking.EncodingHelpers;
import ender.dwmod.tardis.networking.TardisDataSyncS2C;
import ender.dwmod.tardis.networking.TardisUpdateValueS2C;
import ender.dwmod.tardis.systems.architecturalReconfiguration.Room;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.BlockPos;

public final class ClientTardisRegistries {
    private static final List<Tardis> tardis = new ArrayList<>();
    public static List<Tardis> get()
    {
        return tardis;
    }

    public static Tardis getTardis(UnsignedInteger id)
    {
        for (Tardis t : tardis)
        {
            if (t.id().equals(id))
            {
                return t;
            }
        }
        return null;
    }

    public static void handleSyncPacket(TardisDataSyncS2C packet, ClientPlayNetworking.Context context)
    {
        tardis.clear();
        tardis.addAll(TardisPersistentState.fromNBT(packet.tag()));
        DwModClient.LOGGER.info("Synchronized " + tardis.size() + " Tardis data entries from server.");
    }

    public static void handleUpdateValuePacket(TardisUpdateValueS2C packet, ClientPlayNetworking.Context context)
    {
        for (Tardis t : tardis)
        {
            if (t.id().intValue() == packet.tardisID())
            {
                switch (packet.category())
                {
                    case "exoshell" -> {
                        switch (packet.key())
                        {
                            case "position" -> {
                                t.clientSyncPosition(EncodingHelpers.toVec3(packet.value()));
                            }
                            case "door_state" -> {
                                t.clientSyncDoorState(EncodingHelpers.toBoolean(packet.value()));
                            }
                            case "internal_light" -> {
                                t.clientSyncInternalLight(EncodingHelpers.toBoolean(packet.value()));
                            }
                        }
                    }
                }
            }
        }
    }

    public static Tardis get(BlockPos worldPosition) {
        return getTardis(Room.worldPosToInstanceID(worldPosition));
    }    
}
