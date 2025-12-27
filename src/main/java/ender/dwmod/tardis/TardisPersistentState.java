package ender.dwmod.tardis;

import java.util.ArrayList;
import java.util.List;

import ender.dwmod.DwMod;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class TardisPersistentState extends SavedData {

    public static final String ID = "dwmod_tardis_nbt";

    private CompoundTag tardisData;

    public TardisPersistentState() {
        tardisData = new CompoundTag();
    }

    public TardisPersistentState(CompoundTag compound) {
        tardisData = compound.copy();
    }

    public static TardisPersistentState create()
    {
        return new TardisPersistentState();
    }

    public static TardisPersistentState load(CompoundTag tag, Provider registries)
    {
        final TardisPersistentState state = new TardisPersistentState(tag.getCompound("tardis_data"));
        return state;
    }


    @Override
    public CompoundTag save(CompoundTag tag, Provider registries) {
        tag.put("tardis_data", tardisData);
        return tag;
    }
    

    private static final SavedData.Factory<TardisPersistentState> FACTORY = new SavedData.Factory<TardisPersistentState>(TardisPersistentState::create, TardisPersistentState::load, null);

    public static TardisPersistentState get(ServerLevel level)
    {
        DimensionDataStorage storage = level.getDataStorage();
        return storage.computeIfAbsent(FACTORY, ID);
    }

    public static TardisPersistentState get(MinecraftServer server)
    {
        ServerLevel overworld = server.getLevel(Level.OVERWORLD);
        if (overworld == null) throw new IllegalStateException("Overworld is null ?");
        return get(overworld);
    }

    public static CompoundTag toNBT(List<Tardis> tardis)
    {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("tardis_count", tardis.size());
        for(int i = 0; i < tardis.size(); i++)
        {
            final Tardis t = tardis.get(i);
            nbt.put("tardis" + i, t.toNBT());
        }
        return nbt;
    }

    public static List<Tardis> fromNBT(CompoundTag nbt)
    {
        int count = nbt.getInt("tardis_count");
        List<Tardis> tardisList = new ArrayList<>(count);
        for (int i = 0; i < count; i++)
        {
            Tardis t = new Tardis(nbt.getCompound("tardis" + i));
            tardisList.add(t);
        }
        return tardisList;
    }

    public void pushAllTardisData(List<Tardis> tardis) {
        tardisData = toNBT(tardis);
        DwMod.LOGGER.info("Saved " + tardis.size() + " Tardis data entries.");
        setDirty();
    }

    public List<Tardis> getAllTardisData() {
        List<Tardis> tardisList = fromNBT(tardisData);
        DwMod.LOGGER.info("Loaded " + tardisList.size() + " Tardis data entries.");
        return tardisList;
    }
}
