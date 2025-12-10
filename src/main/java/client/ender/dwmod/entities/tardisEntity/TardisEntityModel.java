package client.ender.dwmod.entities.tardisEntity;

import ender.dwmod.entities.tardis.exoshell.TardisEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class TardisEntityModel extends GeoModel<TardisEntity> {

    @Override
    public ResourceLocation getModelResource(TardisEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath("dwmod", "geo/tardis/exoshell/exoshell_default.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TardisEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath("dwmod", "textures/tardis/exoshell/exoshell.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TardisEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath("dwmod", "animations/tardis/exoshell/exoshell_default.animation.json");
    }
    
}
