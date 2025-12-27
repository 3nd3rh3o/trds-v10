package ender.dwmod.mixin;

import com.google.common.collect.ImmutableList;
import ender.dwmod.entities.IMultiCollidable;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EntityGetter.class)
public interface EntityGetterMixin {




    @Overwrite
    default boolean isUnobstructed(@Nullable Entity entity, VoxelShape shape) {
        if (shape.isEmpty()) {
            return true;
        }

        EntityGetter self = (EntityGetter) (Object) this;
        for (Entity entity2 : self.getEntities(entity, shape.bounds())) {

            if (entity2.isRemoved()) {
                continue;
            }
            if (!entity2.blocksBuilding) {
                continue;
            }
            if (entity != null && entity2.isPassengerOfSameVehicle(entity)) {
                continue;
            }

            if (entity2 instanceof IMultiCollidable mCol) {
                boolean obstructed = false;

                for (AABB local : mCol.getColliders()) {

                    AABB worldAabb = local.move(entity2.getX(), entity2.getY(), entity2.getZ());

                    if (Shapes.joinIsNotEmpty(
                            shape,
                            Shapes.create(worldAabb),
                            BooleanOp.AND
                        )) {
                        obstructed = true;
                        break;
                    }
                }

                if (obstructed) {
                    return false;
                }
                continue;
            }

            if (Shapes.joinIsNotEmpty(
                    shape,
                    Shapes.create(entity2.getBoundingBox()),
                    BooleanOp.AND
            )) {
                return false;
            }
        }

        return true;
   }
    

    @Overwrite
    default List<VoxelShape> getEntityCollisions(@Nullable Entity entity, AABB collisionBox) {
        if (collisionBox.getSize() < 1.0E-7) {
            return List.of();
        }
            
        // === Partie vanilla inchangée ===
        Predicate<Entity> predicate;
        if (entity == null) {
            predicate = EntitySelector.CAN_BE_COLLIDED_WITH;
        } else {
            predicate = EntitySelector.NO_SPECTATORS;
            Objects.requireNonNull(entity);
            predicate = predicate.and(entity::canCollideWith);
        }

        EntityGetter self = (EntityGetter)(Object)this;

        List<Entity> list = self.getEntities(entity, collisionBox.inflate(1.0E-7), predicate);
        if (list.isEmpty()) {
            return List.of();
        } else {
            ImmutableList.Builder<VoxelShape> builder = ImmutableList.builderWithExpectedSize(list.size());
            Iterator<Entity> var6 = list.iterator();

            while(var6.hasNext()) {
                Entity entity2 = (Entity)var6.next();
                if (entity2 instanceof IMultiCollidable mCol)
                {
                     for (AABB aabb : mCol.getColliders())
                          builder.add(Shapes.create(aabb.move(entity2.getX(), entity2.getY(), entity2.getZ())));
                }
                else
                {
                    builder.add(Shapes.create(entity2.getBoundingBox()));
                }
            }

            return builder.build();
        }
    }

}
