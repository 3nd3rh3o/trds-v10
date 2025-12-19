package ender.dwmod.block.tardis.exoshell;

import java.util.ArrayList;
import java.util.List;

import ender.dwmod.DwMod;
import ender.dwmod.block.BlockEntityInit;
import ender.dwmod.utils.RayCastShape.Sphere;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DefaultConsoleBE extends BlockEntity implements GeoBlockEntity {
    private static final boolean debugRayCast = true; // log in chat !


    public static final List<Sphere> INTERACTION_SHAPES = new ArrayList<>(
        List.of(
            new Sphere(new Vec3(0.0, 0.55, -0.975), 0.1f) // door switch
        )
    );

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public DefaultConsoleBE(BlockPos pos, BlockState blockState) {
        super(BlockEntityInit.DEFAULT_CONSOLE_BE, pos, blockState);
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        return;
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
        for (int i = 0; i < INTERACTION_SHAPES.size(); i++) {
            Sphere shape = INTERACTION_SHAPES.get(i);
            if (shape.rayCast(player.getEyePosition().subtract(this.worldPosition.getCenter()), hitDir)) {
                if (pH != -1)
                    pH = INTERACTION_SHAPES.get(pH).getCenter().distanceToSqr(player.getEyePosition().subtract(this.worldPosition.getCenter())) < shape.getCenter().distanceToSqr(player.getEyePosition().subtract(this.worldPosition.getCenter())) ? i : pH;
                else
                    pH = i; 
                
            }
        }
        if (pH != -1)
        {
            if (!level.isClientSide && debugRayCast)
                DwMod.LOGGER.info("DefaultConsoleBE interacted ray info : hit shape index : {}", pH);
            return InteractionResult.SUCCESS;
        }
        if (!level.isClientSide && debugRayCast)
            DwMod.LOGGER.info("DefaultConsoleBE interacted ray info : dir : {}, origin : {}", hitDir, player.getEyePosition().subtract(this.worldPosition.getCenter()));
        return InteractionResult.FAIL;
    }
    
}
