package ender.dwmod.tardis.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.primitives.UnsignedInteger;

import ender.dwmod.DwMod;
import ender.dwmod.tardis.Tardis;
import ender.dwmod.tardis.TardisRegistries;
import ender.dwmod.tardis.networking.EncodingHelpers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;

public class AbstractComponentCategory implements IComponent {
    protected Map<String, Boolean> boolVars = new HashMap<>();
    protected Map<String, String> stringVars = new HashMap<>();
    private final Map<String, List<IComponentListener>> listeners = new HashMap<>();


    @Override
    public void install() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'install'");
    }

    @Override
    public void uninstall() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'uninstall'");
    }

    @Override
    public boolean isPresent() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isPresent'");
    }

    @Override
    public boolean isPowered() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isPowered'");
    }

    @Override
    public void setPowered(boolean powered) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setPowered'");
    }

    @Override
    public void toggleValue(MinecraftServer server, UnsignedInteger id, String name) {
        if (boolVars.containsKey(name)) {
            boolean currentValue = boolVars.get(name);
            boolVars.replace(name, !currentValue);
            Tardis.broadcast(TardisRegistries.createValueNotifyPacket(id, getName(), name, EncodingHelpers.fromBoolean(!currentValue)), server);
            if (listeners.containsKey(name)) {
                for (IComponentListener listener : listeners.get(name)) {
                    listener.onComponentValueChanged(name);
                }
            }
        } 
        else {
            DwMod.LOGGER.warn("Attempted to toggle unknown value: " + name);
        }
    }

    @Override
    public void setValue(MinecraftServer server, UnsignedInteger id, String name, boolean value) {
        if (boolVars.containsKey(name)) {
            boolVars.replace(name, value);
            Tardis.broadcast(TardisRegistries.createValueNotifyPacket(id, getName(), name, EncodingHelpers.fromBoolean(value)), server);
            if (listeners.containsKey(name)) {
                for (IComponentListener listener : listeners.get(name)) {
                    listener.onComponentValueChanged(name);
                }
            }
        }
        else {
            DwMod.LOGGER.warn("Attempted to set unknown value: " + name);
        }
    }

    @Override
    public void setValue(MinecraftServer server, UnsignedInteger id, String name, String value) {
        if (stringVars.containsKey(name)) {
            stringVars.replace(name, value);
            Tardis.broadcast(TardisRegistries.createValueNotifyPacket(id, getName(), name, value), server);
            if (listeners.containsKey(name)) {
                for (IComponentListener listener : listeners.get(name)) {
                    listener.onComponentValueChanged(name);
                }
            }
        }
        else {
            DwMod.LOGGER.warn("Attempted to set unknown value: " + name);
        }
    }



    @Override
    public void clientSyncValue(String name, String value) {
        if (boolVars.containsKey(name)) {
            boolVars.replace(name, EncodingHelpers.toBoolean(value));
        } else if (stringVars.containsKey(name)) {
            stringVars.replace(name, value);
        } else {
            DwMod.LOGGER.warn("Attempted to client sync unknown value: " + name);
        }
    }

    @Override
    public void registerListener(IComponentListener listener, String valueName) {
        if (!listeners.containsKey(valueName)) {
            listeners.put(valueName, new java.util.ArrayList<>());
        }
        listeners.get(valueName).add(listener);
    }

    @Override
    public void unRegisterListener(IComponentListener listener, String valueName) {
        if (listeners.containsKey(valueName)) {
            listeners.get(valueName).remove(listener);
        }
    }

    @Override
    public boolean getValue(String name) {
        if (boolVars.containsKey(name)) {
            return boolVars.get(name);
        } else {
            DwMod.LOGGER.warn("Attempted to get unknown value: " + name);
            return false;
        }
    }

    @Override
    public String getStringValue(String name) {
        if (stringVars.containsKey(name)) {
            return stringVars.get(name);
        } else {
            DwMod.LOGGER.warn("Attempted to get unknown value: " + name);
            return "";
        }
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getName'");
    }

    @Override
    public CompoundTag writeNBT() {
        CompoundTag compound = new CompoundTag();
        for (Map.Entry<String, Boolean> entry : boolVars.entrySet()) {
            compound.putBoolean(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, String> entry : stringVars.entrySet()) {
            compound.putString(entry.getKey(), entry.getValue());
        }
        return compound;
    }

    @Override
    public void readNBT(CompoundTag compound) {
        for (String key : boolVars.keySet()) {
            if (compound.contains(key)) {
                boolVars.replace(key, compound.getBoolean(key));
            }
        }
        for (String key : stringVars.keySet()) {
            if (compound.contains(key)) {
                stringVars.replace(key, compound.getString(key));
            }
        }
    }
    
}
