package ender.dwmod.tardis;

import java.util.ArrayList;
import java.util.List;

import com.google.common.primitives.UnsignedInteger;

import ender.dwmod.DwMod;
import ender.dwmod.entities.tardis.exoshell.TardisEntity;
import net.minecraft.server.MinecraftServer;

public final class TardisRegisties {
    
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
        final Tardis t = Tardis.create(freeID());
        tardis.add(t);
        DwMod.LOGGER.info("Triggered gen!");
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

    public static void deleteTardis(UnsignedInteger iD) { // Trigger cleanup of internal dimension etc....
        tardis.removeIf(t -> t.id().equals(iD));
        DwMod.LOGGER.info("Tardis with ID " + iD + " deleted from registries.");
    }
}
