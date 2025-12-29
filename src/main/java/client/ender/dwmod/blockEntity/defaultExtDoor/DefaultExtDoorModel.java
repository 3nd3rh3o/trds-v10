package client.ender.dwmod.blockEntity.defaultExtDoor;

import ender.dwmod.DwMod;
import ender.dwmod.block.tardis.complex.defaultExtDoor.DefaultExtDoorCoreBE;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DefaultExtDoorModel extends GeoModel<DefaultExtDoorCoreBE> {

    @Override
    public ResourceLocation getModelResource(DefaultExtDoorCoreBE animatable) {
        return ResourceLocation.fromNamespaceAndPath(DwMod.MOD_ID, "geo/tardis/ext_door/default.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DefaultExtDoorCoreBE animatable) {
        return ResourceLocation.fromNamespaceAndPath(DwMod.MOD_ID, "textures/tardis/ext_door/default.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DefaultExtDoorCoreBE animatable) {
        return ResourceLocation.fromNamespaceAndPath(DwMod.MOD_ID, "animations/tardis/ext_door/default.animation.json");
    }
    
}
