package ender.dwmod.tardis;


import com.google.common.primitives.UnsignedInteger;

import ender.dwmod.entities.tardis.exoshell.TardisEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Tardis {
    private UnsignedInteger id;
    private Vec3 position;
    private ResourceKey<Level> dimension;

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
        return compound;
    }

    // Used for creating a new Tardis -> Will trigger events.
    public static Tardis create(UnsignedInteger id, Vec3 position, ResourceKey<Level> dimension) {
         Tardis t = new Tardis();
         t.id = id;
         t.position = position;
         t.dimension = dimension;
         return t;
    }

    // Return this Tardis's ID
    public UnsignedInteger id() {
        return id;
    }

    // only called by entity, when warping, use setPosition instead !
    public void updatePosition(TardisEntity tardisEntity) {
        this.position = tardisEntity.position();
    }

}
