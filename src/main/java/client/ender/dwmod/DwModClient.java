package client.ender.dwmod;

import client.ender.dwmod.entities.tardisEntity.TardisEntityRenderer;
import ender.dwmod.DwMod;
import ender.dwmod.entities.EntityInit;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class DwModClient implements net.fabricmc.api.ClientModInitializer {
    public static final String MOD_ID = DwMod.MOD_ID;
    public static final org.slf4j.Logger LOGGER = DwMod.LOGGER;

    @Override
    public void onInitializeClient() {
        LOGGER.info("dwmod client loaded.");
        
        EntityRendererRegistry.register(EntityInit.TARDIS, (ctx) -> new TardisEntityRenderer(ctx));
    }
}