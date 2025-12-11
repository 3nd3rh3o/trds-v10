package ender.dwmod.tardis;


import com.google.common.primitives.UnsignedInteger;
import net.minecraft.nbt.CompoundTag;

public class Tardis {
    private UnsignedInteger id;

    private Tardis() {

    }


    // Used for NBT loading -> No events triggered.
    public Tardis(CompoundTag compound) {
        this.id = UnsignedInteger.fromIntBits(compound.getInt("id"));
    }

    // Used for NBT saving -> No events triggered.
    public CompoundTag toNBT() {
        CompoundTag compound = new CompoundTag();
        compound.putInt("id", id.intValue());
        return compound;
    }

    // Used for creating a new Tardis -> Will trigger events.
    public static Tardis create(UnsignedInteger id) {
        Tardis t = new Tardis();
        t.id = id;
        return t;
    }

    // Return this Tardis's ID
    public UnsignedInteger id() {
        return id;
    }

}
