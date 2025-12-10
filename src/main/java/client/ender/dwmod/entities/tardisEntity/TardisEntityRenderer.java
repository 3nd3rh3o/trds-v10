package client.ender.dwmod.entities.tardisEntity;

import ender.dwmod.entities.tardis.exoshell.TardisEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TardisEntityRenderer extends GeoEntityRenderer<TardisEntity> {
    public TardisEntityRenderer(Context renderManagerContext) {
        super(renderManagerContext, new TardisEntityModel());
    }
}
