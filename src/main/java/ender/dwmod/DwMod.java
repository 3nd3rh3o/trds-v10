package ender.dwmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ender.dwmod.block.BlockEntityInit;
import ender.dwmod.block.BlockInit;
import ender.dwmod.entities.EntityInit;
import ender.dwmod.tardis.TardisNetworking;
import ender.dwmod.tardis.TardisRegistries;
import ender.dwmod.utils.StructurePlacer;


public class DwMod implements ModInitializer {
    public static final String MOD_ID = "dwmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        EntityInit.init();
        BlockInit.init();
        BlockEntityInit.registerBlockEntities();
        TardisNetworking.registerPackets();
        TardisNetworking.registerServerReceivers();

        // On world load clear Tardis registries.
        ServerLifecycleEvents.SERVER_STARTED.register((server) -> TardisRegistries.onWorldJoin(server));
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer player = handler.getPlayer();
            ServerPlayNetworking.send(player, TardisRegistries.createSyncPacket());
        });


        ServerTickEvents.END_SERVER_TICK.register((server) -> {
            StructurePlacer.tick(server);
            TardisRegistries.tick(server);
        });

        // on world unload clear Tardis registries.
        ServerLifecycleEvents.SERVER_STOPPING.register((server) -> TardisRegistries.onWorldLeave(server));
        
        LOGGER.info("dwmod loaded.");
    }
}
