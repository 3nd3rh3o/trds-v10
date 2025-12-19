package client.ender.dwmod.blockEntity.console;

import ender.dwmod.DwMod;
import ender.dwmod.block.tardis.exoshell.DefaultConsoleBE;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DefaultConsoleModel extends GeoModel<DefaultConsoleBE> {

    @Override
    public ResourceLocation getModelResource(DefaultConsoleBE animatable) {
        return ResourceLocation.fromNamespaceAndPath(DwMod.MOD_ID, "geo/tardis/console/default_console.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DefaultConsoleBE animatable) {
        return ResourceLocation.fromNamespaceAndPath(DwMod.MOD_ID, "textures/tardis/console/default_console.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DefaultConsoleBE animatable) {
        return ResourceLocation.fromNamespaceAndPath(DwMod.MOD_ID, "animations/tardis/console/default_console.animation.json");
    }
    
}
