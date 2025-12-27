package ender.dwmod.block.tardis.exoshell;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import ender.dwmod.DwMod;
import ender.dwmod.block.BlockEntityInit;
import ender.dwmod.tardis.Interactible;
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
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.network.packet.BlockEntityAnimTriggerPacket;
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DefaultConsoleBE extends BlockEntity implements GeoBlockEntity {
    private static final boolean debugRayCast = true; // log in chat !

    // animations
    private static final RawAnimation DOOR_SWITCH_SET_ON = RawAnimation.begin().thenPlay("animation.door_switch.set_on");
    private static final RawAnimation DOOR_SWITCH_SET_OFF = RawAnimation.begin().thenPlay("animation.door_switch.set_off");
    private static final RawAnimation DOOR_SWITCH_IDLE_OFF = RawAnimation.begin().thenPlayAndHold("animation.door_switch.idle_off");
    private static final RawAnimation DOOR_SWITCH_IDLE_ON = RawAnimation.begin().thenPlayAndHold("animation.door_switch.idle_on");


    @Deprecated //"will be replaced by read/write of a tardis object"
    private boolean door_switch = true; // TODO - Should be synced with client and only defined on server!

    private final List<Interactible> INTERACTIBLES = List.of(
        new Interactible(new Sphere(new Vec3(0.0, 0.55, -0.975), 0.1f), 20) // door switch
    );


    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public DefaultConsoleBE(BlockPos pos, BlockState blockState) {
        super(BlockEntityInit.DEFAULT_CONSOLE_BE, pos, blockState);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(
            new AnimationController<>(this, "door_switch", 0, state -> {
                return door_switch ? state.setAndContinue(DOOR_SWITCH_IDLE_ON) : state.setAndContinue(DOOR_SWITCH_IDLE_OFF);
            })
            .triggerableAnim("set_on", DOOR_SWITCH_SET_ON)
            .triggerableAnim("set_off", DOOR_SWITCH_SET_OFF)
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
        switch (pH)
        {
            case 0 -> {
                if (!level.isClientSide && debugRayCast)
                    DwMod.LOGGER.info("DefaultConsoleBE interacted : door switch");
                
                if (!level.isClientSide)
                    triggerAnimBroad("door_switch", this.door_switch ? "set_off" : "set_on");
                this.door_switch = !this.door_switch; // will change the var on both sides
                // TODO - set data on Tardis, it will send a packet to all clients, and they will update their values. {@link DefaultConsoleBE.door_switch} will be removed, the tardis object will be read instead.
            }
            default -> {
                // no interaction
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
    }


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

        var pkt = new BlockEntityAnimTriggerPacket(this.worldPosition, controller, animName);

        for (ServerPlayer p : targets) {
            GeckoLibServices.NETWORK.sendToPlayer(pkt, p);
        }
    }

}
