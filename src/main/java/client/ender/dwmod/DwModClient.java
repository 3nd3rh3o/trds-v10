package client.ender.dwmod;

import ender.dwmod.DwMod;

public class DwModClient implements net.fabricmc.api.ClientModInitializer {
    public static final String MOD_ID = DwMod.MOD_ID;
    public static final org.slf4j.Logger LOGGER = DwMod.LOGGER;

    @Override
    public void onInitializeClient() {
        LOGGER.info("dwmod client loaded.");
    }
}