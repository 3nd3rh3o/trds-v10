package ender.dwmod;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Minimal bootstrap for the custom mod id "dwmod". Extend this class with your
 * blocks/items/registries as you build the mod. Keep it lightweight so it can be
 * jar-in-jar bundled alongside Immersive Portals and other runtime deps.
 */
public class DwMod implements ModInitializer {
    public static final String MOD_ID = "dwmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("dwmod loaded.");
    }
}
