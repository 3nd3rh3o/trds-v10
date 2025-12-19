package ender.dwmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ender.dwmod.block.BlockEntityInit;
import ender.dwmod.block.BlockInit;
import ender.dwmod.entities.EntityInit;
import ender.dwmod.tardis.TardisRegisties;


public class DwMod implements ModInitializer {
    public static final String MOD_ID = "dwmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        EntityInit.init();
        BlockInit.init();
        BlockEntityInit.registerBlockEntities();

        // On world load clear Tardis registries.
        ServerLifecycleEvents.SERVER_STARTED.register((server) -> TardisRegisties.onWorldJoin(server));

        // on world unload clear Tardis registries.
        ServerLifecycleEvents.SERVER_STOPPING.register((server) -> TardisRegisties.onWorldLeave(server));
        
        LOGGER.info("dwmod loaded.");
    }
}
