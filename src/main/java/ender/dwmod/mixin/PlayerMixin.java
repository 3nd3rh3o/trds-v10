package ender.dwmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import ender.dwmod.entities.tardis.exoshell.TardisEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder.Living;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity{
    // Empêche le push si la Tardis est au sol
    
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void push(Entity entity) {
        if (entity instanceof TardisEntity tardis) {
            if (tardis.onGround()) {
                return;
            }
        }
        super.push(entity);
    }
}
