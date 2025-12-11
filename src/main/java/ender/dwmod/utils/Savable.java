package ender.dwmod.utils;

import net.minecraft.nbt.CompoundTag;

public interface Savable {
    public CompoundTag toNbt();
    public void fromNbt(CompoundTag nbt);
}
