package ender.dwmod.tardis.components;

import com.google.common.primitives.UnsignedInteger;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;

public interface IComponent {
    public void install();
    public void uninstall();
    public boolean isPresent();
    public boolean isPowered();
    public void setPowered(boolean powered);
    public void toggleValue(MinecraftServer server, UnsignedInteger id, String name);
    public void setValue(MinecraftServer server, UnsignedInteger id, String name, boolean value);
    public void setValue(MinecraftServer server, UnsignedInteger id, String name, String value);
    public void clientSyncValue(String name, String value);
    public void registerListener(IComponentListener listener, String valueName);
    public void unRegisterListener(IComponentListener listener, String valueName);
    public boolean getValue(String name);
    public String getStringValue(String name);
    public String getName();
    public CompoundTag writeNBT();
    public void readNBT(CompoundTag compound);
}
