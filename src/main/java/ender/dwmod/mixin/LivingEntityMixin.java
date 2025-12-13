package ender.dwmod.mixin;

import java.util.List;
import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import ender.dwmod.entities.IMultiCollidable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "pushEntities", at = @At("HEAD"), cancellable = true)
    private void dwmod_preventPushEntities(CallbackInfo ci) {
        if ((Object) this instanceof IMultiCollidable) {
            ci.cancel();
        }
    }

    @Redirect(method = "pushEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"))
    private List<Entity> dwmod_filterPushEntities(Level level, Entity self, AABB bb, Predicate<? super Entity> predicate) {
        List<Entity> base = level.getEntities(self, bb, predicate);
        return base.stream().filter(e -> !(e instanceof IMultiCollidable)).toList();
    }
}
