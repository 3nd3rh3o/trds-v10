package ender.dwmod.entities;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import ender.dwmod.DwMod;
import ender.dwmod.entities.tardis.exoshell.TardisEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class EntityInit {
    public static final EntityType<TardisEntity> TARDIS = Registry.register(
        BuiltInRegistries.ENTITY_TYPE,
        ResourceLocation.fromNamespaceAndPath(DwMod.MOD_ID, "tardis"), 
        EntityType.Builder.of(TardisEntity::new, MobCategory.MISC).sized(2, 3).build("tardis")
    );



    public static void init()
    {
        FabricDefaultAttributeRegistry.register(TARDIS, TardisEntity.createLivingAttributes());
    }
}
