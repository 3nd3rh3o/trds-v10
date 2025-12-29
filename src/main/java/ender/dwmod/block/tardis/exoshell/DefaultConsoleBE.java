package ender.dwmod.block.tardis.exoshell;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import client.ender.dwmod.ClientTardisRegistries;
import ender.dwmod.DwMod;
import ender.dwmod.block.BlockEntityInit;
import ender.dwmod.dimensions.DimensionRegistry;
import ender.dwmod.tardis.Interactible;
import ender.dwmod.tardis.TardisRegistries;
import ender.dwmod.tardis.components.ExtDoor;
import ender.dwmod.tardis.components.IComponentListener;
import ender.dwmod.utils.RayCastShape.Sphere;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.GeckoLibServices;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.network.packet.BlockEntityAnimTriggerPacket;
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DefaultConsoleBE extends BlockEntity implements GeoBlockEntity, TardisAnimatable, IComponentListener {
    private static final boolean debugRayCast = true; // log in chat !
    // door switch
    private static final RawAnimation DOOR_SWITCH_SET_ON = RawAnimation.begin().thenPlay("animation.door_switch.set_on");
    private static final RawAnimation DOOR_SWITCH_SET_OFF = RawAnimation.begin().thenPlay("animation.door_switch.set_off");
    private static final RawAnimation DOOR_SWITCH_IDLE_OFF = RawAnimation.begin().thenPlayAndHold("animation.door_switch.idle_off");
    private static final RawAnimation DOOR_SWITCH_IDLE_ON = RawAnimation.begin().thenPlayAndHold("animation.door_switch.idle_on");
    // light switch
    private static final RawAnimation LIGHT_SWITCH_SET_ON = RawAnimation.begin().thenPlay("animation.light_switch.set_on");
    private static final RawAnimation LIGHT_SWITCH_SET_OFF = RawAnimation.begin().thenPlay("animation.light_switch.set_off");
    private static final RawAnimation LIGHT_SWITCH_IDLE_OFF = RawAnimation.begin().thenPlayAndHold("animation.light_switch.idle_off");
    private static final RawAnimation LIGHT_SWITCH_IDLE_ON = RawAnimation.begin().thenPlayAndHold("animation.light_switch.idle_on");

    public boolean isFirstTick = true;


    private final List<Interactible> INTERACTIBLES = List.of( // use blockbench coords / 16 - (0, 0.5, 0)
        new Interactible(new Sphere(new Vec3(0.0, 0.55, -0.975), 0.1f), 20), // door switch
        new Interactible(new Sphere(new Vec3(-0.1875, 0.6588125, -0.62820625), 0.1f), 10) // light switch
    );


    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public DefaultConsoleBE(BlockPos pos, BlockState blockState) {
        super(BlockEntityInit.DEFAULT_CONSOLE_BE, pos, blockState);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }



    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        if (!level.dimension().equals(DimensionRegistry.VORTEX_DIMENSION_KEY))
            return; // only animate in Tardis dimension
        controllers.add(
            new AnimationController<>(this, "door_switch", 1, state -> {
                if (ClientTardisRegistries.get(worldPosition) == null)
                    return PlayState.STOP;
                else
                    return ClientTardisRegistries.get(worldPosition).getComponentByName(ExtDoor.name()).getValue("door_state") ? state.setAndContinue(DOOR_SWITCH_IDLE_ON) : state.setAndContinue(DOOR_SWITCH_IDLE_OFF);
            })
            .triggerableAnim("set_on", DOOR_SWITCH_SET_ON)
            .triggerableAnim("set_off", DOOR_SWITCH_SET_OFF)
        );
        controllers.add(
            new AnimationController<>(this, "light_switch", 1, state -> {
                
                    if (ClientTardisRegistries.get(worldPosition) == null)
                        return PlayState.STOP;
                    else
                        return ClientTardisRegistries.get(worldPosition).getInternalLight() ? state.setAndContinue(LIGHT_SWITCH_IDLE_ON) : state.setAndContinue(LIGHT_SWITCH_IDLE_OFF);
            })
            .triggerableAnim("set_on", LIGHT_SWITCH_SET_ON)
            .triggerableAnim("set_off", LIGHT_SWITCH_SET_OFF)
        );        
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    

    // Should be only called on server side.
    public InteractionResult interact(Player player, BlockHitResult hitResult) 
    {
        Vec3 hitDir = hitResult.getLocation().subtract(player.getEyePosition()).normalize();
        int pH = -1;
        for (int i = 0; i < INTERACTIBLES.size(); i++) {
            Interactible shape = INTERACTIBLES.get(i);
            if (shape.interacted(player.getEyePosition().subtract(this.worldPosition.getCenter()), hitDir)) {
                if (pH != -1)
                    pH = INTERACTIBLES.get(pH).getCenter().distanceToSqr(player.getEyePosition().subtract(this.worldPosition.getCenter())) < shape.getCenter().distanceToSqr(player.getEyePosition().subtract(this.worldPosition.getCenter())) ? i : pH;
                else
                    pH = i; 
                
            }
        }
        if (level.dimension().equals(DimensionRegistry.VORTEX_DIMENSION_KEY)) // only work in Tardis dimension
        {
            switch (pH)
            {
                case 0 -> {
                    if (!level.isClientSide && debugRayCast)
                        DwMod.LOGGER.info("DefaultConsoleBE interacted : door switch");
                    
                    if (!level.isClientSide)
                    {
                        TardisRegistries.get(worldPosition).getComponentByName(ExtDoor.name()).toggleValue(level.getServer(), TardisRegistries.get(worldPosition).getID(), "door_state");
                    }
                }
                case 1 -> {
                    if (!level.isClientSide && debugRayCast)
                        DwMod.LOGGER.info("DefaultConsoleBE interacted : light switch");
                    
                    if (!level.isClientSide)
                    {
                        TardisRegistries.get(worldPosition).toggleInternalLight(level.getServer());
                    }
                }
                default -> {
                    // no interaction
                }

            }
        }
        if (pH != -1)
        {
            INTERACTIBLES.get(pH).setOnCooldown();  
            if (!level.isClientSide && debugRayCast)
                DwMod.LOGGER.info("DefaultConsoleBE interacted ray info : hit shape index : {}", pH);
            return InteractionResult.SUCCESS;
        }
        if (!level.isClientSide && debugRayCast)
            DwMod.LOGGER.info("DefaultConsoleBE interacted ray info : dir : {}, origin : {}", hitDir, player.getEyePosition().subtract(this.worldPosition.getCenter()));
        return InteractionResult.FAIL;
    }

    public static void tick(Level world, BlockPos blockPos, BlockState blockState, DefaultConsoleBE entity) {
        for (Interactible interactible : entity.INTERACTIBLES) {
            interactible.tick();
        }
        if (!world.isClientSide() && entity.isFirstTick)
        {
            if (TardisRegistries.get(entity.worldPosition) == null)
            {
                entity.isFirstTick = false; // ignore when not inside a Tardis
                return;
            }
            TardisRegistries.get(entity.worldPosition).internal_light_dependants.add(entity);
            entity.register();
            entity.isFirstTick = false;
        }
        
    }

    @Override
    public void triggerAnimBroad(@Nullable String controllerName, String animName) {
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

    @Override
    public void setRemoved() {
        if (!level.isClientSide)
        {
            if (TardisRegistries.get(worldPosition) != null)
            {
                TardisRegistries.get(worldPosition).internal_light_dependants.remove(this);
                unRegister();
        }
        super.setRemoved();
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
        triggerAnimBroad("door_switch", TardisRegistries.get(worldPosition).getComponentByName(ExtDoor.name()).getValue("door_state") ? "set_on" : "set_off");
    }
}
