package ender.dwmod.tardis;

import client.ender.dwmod.ClientTardisRegistries;
import ender.dwmod.tardis.networking.TardisDataSyncS2C;
import ender.dwmod.tardis.networking.TardisScreenOpeningS2C;
import ender.dwmod.tardis.networking.TardisUpdateValueC2S;
import ender.dwmod.tardis.networking.TardisUpdateValueS2C;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class TardisNetworking {

        public static void registerPackets() {
            // Registration logic packets

            PayloadTypeRegistry.playC2S().register(TardisUpdateValueC2S.ID, TardisUpdateValueC2S.CODEC);

            PayloadTypeRegistry.playS2C().register(TardisUpdateValueS2C.ID, TardisUpdateValueS2C.CODEC);
            PayloadTypeRegistry.playS2C().register(TardisDataSyncS2C.ID, TardisDataSyncS2C.CODEC);
            PayloadTypeRegistry.playS2C().register(TardisScreenOpeningS2C.ID, TardisScreenOpeningS2C.CODEC);

        }

        public static void registerClientReceivers()
        {
            // sync one data in one tardis on changed on server
            ClientPlayNetworking.registerGlobalReceiver(TardisUpdateValueS2C.ID, ClientTardisRegistries::handleUpdateValuePacket);

            // sync all tardis data on joined
            ClientPlayNetworking.registerGlobalReceiver(TardisDataSyncS2C.ID, ClientTardisRegistries::handleSyncPacket);

            // used to open Tardis GUI screens
            ClientPlayNetworking.registerGlobalReceiver(TardisScreenOpeningS2C.ID, TardisScreenRegistry::handleScreenOpeningPacket);
        }

        public static void registerServerReceivers()
        {
            // update internal Tardis data => then sync with all clients
            ServerPlayNetworking.registerGlobalReceiver(TardisUpdateValueC2S.ID, TardisRegistries::handleUpdateValuePacket);
        }
}
