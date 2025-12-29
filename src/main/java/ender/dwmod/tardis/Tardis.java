package ender.dwmod.tardis;


import java.util.ArrayList;
import java.util.List;

import com.google.common.primitives.UnsignedInteger;

import ender.dwmod.DwMod;
import ender.dwmod.block.tardis.complex.defaultExtDoor.TardisBooleanChangedNotify;
import ender.dwmod.block.tardis.exoshell.TardisAnimatable;
import ender.dwmod.dimensions.DimensionRegistry;
import ender.dwmod.entities.tardis.exoshell.TardisEntity;
import ender.dwmod.tardis.components.ExtDoor;
import ender.dwmod.tardis.components.IComponent;
import ender.dwmod.tardis.networking.EncodingHelpers;
import ender.dwmod.tardis.systems.ArchitecturalReconfiguration;
import ender.dwmod.tardis.systems.architecturalReconfiguration.Rooms;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.q_misc_util.my_util.DQuaternion;

public class Tardis {
    private UnsignedInteger id;
    private Vec3 position;
    private ResourceKey<Level> dimension;
    private ArchitecturalReconfiguration ars;
    private boolean internalLight = false;
    public List<TardisAnimatable> internal_light_dependants = new ArrayList<>();
    private Portal exoshellPortal = null;

    // used to store components (data holders, but some can be removed/added dynamically)
    private final List<IComponent> components = new ArrayList<>();


    


    private static final Vec3 TEMP_VEC3 = new Vec3(0, 1.5, 0.875);
    private static final double TEMP_W = 1.75;
    private static final double TEMP_H = 3;

    private Tardis() {
        components.add(new ExtDoor());
    }


    // Used for NBT loading -> No events triggered.
    public Tardis(CompoundTag compound) {
        this();



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
        this.internalLight = compound.getBoolean("internal_light");
        for (IComponent component : components) {
            component.readNBT(compound.getCompound(component.getName()));
        }
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
        compound.putBoolean("internal_light", internalLight);
        for (IComponent component : components) {
            compound.put(component.getName(), component.writeNBT());
        }
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
        if (exoshellPortal == null)
        {
            List<Portal> p = server.getLevel(dimension).getEntitiesOfClass(Portal.class, AABB.ofSize(position, 2, 2, 2), e->true);
            if (p.size() > 0)
            {
                exoshellPortal = p.getFirst();
            }
        }
        if (exoshellPortal != null && (position.add(TEMP_VEC3).subtract(exoshellPortal.getOriginPos())).lengthSqr() > 1e-6)
        {
            exoshellPortal.setOriginPos(position.add(TEMP_VEC3));
            exoshellPortal.reloadAndSyncToClient();
            ars.updateConsoleRoomExit(position);
        }
        
    }


    public boolean getInternalLight() {
        return internalLight;
    }


    public void toggleInternalLight(MinecraftServer server) {
        internalLight = !internalLight;
        broadcast(TardisRegistries.createValueNotifyPacket(id, "exoshell", "internal_light", EncodingHelpers.fromBoolean(internalLight)), server);
        for (int i = 0; i < internal_light_dependants.size(); i++)
        {
            internal_light_dependants.get(i).
                        triggerAnimBroad("light_switch", internalLight ? "set_on" : "set_off");
            if (internal_light_dependants.get(i) instanceof TardisBooleanChangedNotify notify)
                notify.onTardisBooleanChanged("internal_light", internalLight);
        }
    }

    public static void broadcast(CustomPacketPayload packet, MinecraftServer server) {
        for (ServerPlayer player : PlayerLookup.all(server))
        {
            ServerPlayNetworking.send(player, packet);
        }
    }



    // ONLY ON CLIENT SIDE

    public void clientSyncPosition(Vec3 vec3) {
        this.position = vec3;
    }

    public void clientSyncInternalLight(boolean boolean1) {
        internalLight = boolean1;
    }


    public void delete(MinecraftServer server) {
        ars.deleteAllRooms(server);
        if (exoshellPortal != null)
        {
            exoshellPortal.kill();
            exoshellPortal.reloadAndSyncToClient();
            exoshellPortal = null;
        }
    }


    public void spawnExoshellPortal() {
        if (exoshellPortal != null)
            exoshellPortal.kill();
        ServerLevel originLevel = TardisRegistries.server.getLevel(dimension);
        if (originLevel == null) {
            DwMod.LOGGER.error("Cannot spawn exoshell portal: origin dimension {} is not loaded", dimension.location());
            exoshellPortal = null;
            return;
        }

        Portal portal = Portal.ENTITY_TYPE.create(originLevel);
        if (portal == null) {
            DwMod.LOGGER.error("Cannot spawn exoshell portal: Portal entity type returned null");
            exoshellPortal = null;
            return;
        }

        // IMPORTANT: Portal orientation (axisW/axisH) must be initialized.
        // setRotation() is the transformation, not the portal plane orientation.
        portal.setOrientationRotation(DQuaternion.fromEulerAngle(Vec3.ZERO));
        portal.setWidth(TEMP_W);
        portal.setHeight(TEMP_H);

        portal.setOriginPos(position.add(TEMP_VEC3));
        portal.setDestinationDimension(DimensionRegistry.VORTEX_DIMENSION_KEY);
        portal.setDestination(ars.getConsoleRoomEntrance().add(0, 0, -0.375));
        portal.setRotation(DQuaternion.fromEulerAngle(new Vec3(0, 180, 0)));

        originLevel.addFreshEntity(portal);
        exoshellPortal = portal;
    }


	public Vec3 getPosition() {
        return position;
	}


	public ResourceKey<Level> getDimension() {
		return dimension;
	}

    public IComponent getComponentByName(String name) {
        for (IComponent component : components) {
            if (component.getName().equals(name)) {
                return component;
            }
        }
        return null;
    }


    public UnsignedInteger getID() {
        return id;
    }
}
