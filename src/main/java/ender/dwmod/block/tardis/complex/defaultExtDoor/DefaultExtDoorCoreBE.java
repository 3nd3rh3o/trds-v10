package ender.dwmod.block.tardis.complex.defaultExtDoor;

import java.util.LinkedHashSet;
import java.util.Set;

import client.ender.dwmod.ClientTardisRegistries;
import ender.dwmod.block.BlockEntityInit;
import ender.dwmod.block.tardis.exoshell.TardisAnimatable;
import ender.dwmod.tardis.TardisRegistries;
import ender.dwmod.tardis.components.ExtDoor;
import ender.dwmod.tardis.components.IComponentListener;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.GeckoLibServices;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.network.packet.BlockEntityAnimTriggerPacket;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DefaultExtDoorCoreBE extends BlockEntity implements GeoBlockEntity, TardisAnimatable, TardisBooleanChangedNotify, IComponentListener {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected boolean isFirstTick = true; // only used on client side for light rendering.
    private int coolDown = 0;


    private static final RawAnimation OPEN = RawAnimation.begin().thenPlay("animation.ext_door.open");
    private static final RawAnimation CLOSE = RawAnimation.begin().thenPlay("animation.ext_door.close");
    private static final RawAnimation IDLE_OPEN = RawAnimation.begin().thenPlayAndHold("animation.ext_door.idle_open");
    private static final RawAnimation IDLE_CLOSED = RawAnimation.begin().thenPlayAndHold("animation.ext_door.idle_closed");


    public DefaultExtDoorCoreBE(BlockPos pos, BlockState state){
        super(BlockEntityInit.DEFAULT_EXT_DOOR_CORE_BE, pos, state);
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "door_state", 1, state -> {
            if (ClientTardisRegistries.get(this.worldPosition) != null)
                return ClientTardisRegistries.get(worldPosition).getComponentByName(ExtDoor.name()).getValue("door_state") ? state.setAndContinue(IDLE_OPEN) : state.setAndContinue(IDLE_CLOSED);
            return PlayState.STOP;
        })
        .triggerableAnim("set_on", OPEN)
        .triggerableAnim("set_off", CLOSE)
        );
    }

    public InteractionResult useItemOn()
    {
        if (!level.isClientSide)
        {
            if (TardisRegistries.get(worldPosition) != null && coolDown == 0 && !TardisRegistries.get(worldPosition).getComponentByName(ExtDoor.name()).getValue("door_locked"))
            {
                TardisRegistries.get(worldPosition).getComponentByName(ExtDoor.name()).toggleValue(level.getServer(), TardisRegistries.get(worldPosition).getID(), "door_state");
                coolDown = 20; // 1 second of cooldown between door toggles
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void triggerAnimBroad(String controllerName, String animName) {
        if (!(this.level instanceof ServerLevel sl))
            return;

        final String controller = controllerName == null ? "" : controllerName;

        // Rayon "large" (en blocs) pour attraper les joueurs pertinents même si tracking=0
        final double radius = 256.0d;

        Set<ServerPlayer> targets = new LinkedHashSet<>();

        // 1) Joueurs qui trackent le bloc/chunk (chemin normal)
        targets.addAll(PlayerLookup.tracking(sl, this.worldPosition));

        // 2) Joueurs proches (au cas où tracking bug)
        targets.addAll(PlayerLookup.around(sl, Vec3.atCenterOf(this.worldPosition), radius));

        // 3) Fallback "large": tous les joueurs de ce ServerLevel si toujours vide
        if (targets.isEmpty()) {
            targets.addAll(sl.players());
        }
        
        // 4) Ultime fallback: tous les joueurs du serveur si toujours vide
        if (targets.isEmpty()) {
            targets.addAll(PlayerLookup.all(sl.getServer()));
        }

        var pkt = new BlockEntityAnimTriggerPacket(this.worldPosition, controller, animName);

        for (ServerPlayer p : targets) {
            GeckoLibServices.NETWORK.sendToPlayer(pkt, p);
        }

    }

    public static void tick(Level world, BlockPos blockPos, BlockState blockState, DefaultExtDoorCoreBE entity) {
        if (!world.isClientSide() && entity.isFirstTick)
        {
            if (TardisRegistries.get(entity.worldPosition) == null)
            {
                entity.isFirstTick = false; // ignore when not inside a Tardis
                return;
            }
            entity.register();
            entity.isFirstTick = false;
        }
        if (!world.isClientSide())
        {
            if (entity.coolDown > 0)
                entity.coolDown--;
        }
    }
    
    @Override
    public void setRemoved() {
        if (!level.isClientSide)
        {
            if (TardisRegistries.get(worldPosition) != null)
            {
                unRegister();
            }
        }
        super.setRemoved();
    }

    @Override
    public void onTardisBooleanChanged(String key, boolean value) {
        if (key.equals("door_state"))
        {
            level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(DefaultExtDoorShapes.OPEN, value), Block.UPDATE_ALL);
        }
    }

    @Override
    public void register() {
        TardisRegistries.get(worldPosition).getComponentByName(ExtDoor.name()).registerListener(this, "door_state");
    }

    @Override
    public void unRegister() {
        TardisRegistries.get(worldPosition).getComponentByName(ExtDoor.name()).unRegisterListener(this, "door_state");
    }

    @Override
    public void onComponentValueChanged() {
        triggerAnimBroad("door_state", TardisRegistries.get(worldPosition).getComponentByName(ExtDoor.name()).getValue("door_state") ? "set_on" : "set_off");
        if (this.getBlockState().getValue(DefaultExtDoorShapes.OPEN) != TardisRegistries.get(worldPosition).getComponentByName(ExtDoor.name()).getValue("door_state"))
        {
            level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(DefaultExtDoorShapes.OPEN, TardisRegistries.get(worldPosition).getComponentByName(ExtDoor.name()).getValue("door_state")), Block.UPDATE_ALL);
        }
    }
}
