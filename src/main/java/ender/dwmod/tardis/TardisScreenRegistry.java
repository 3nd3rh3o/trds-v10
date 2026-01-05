package ender.dwmod.tardis;

import java.util.List;

import ender.dwmod.tardis.networking.TardisScreenOpeningS2C;
import ender.dwmod.tardis.systems.architecturalReconfiguration.ArsScreen;
import ender.dwmod.tardis.systems.consoleTerminal.TerminalScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public final class TardisScreenRegistry {
    public static final List<Class<? extends Screen>> SCREENS_ID = List.of(
        ArsScreen.class,
        TerminalScreen.class
    );


    public static int getScreenId(Class<? extends Screen> screenClass) {
        return SCREENS_ID.indexOf(screenClass);
    }


    public static Screen createScreenById(int id) {
        if (id < 0 || id >= SCREENS_ID.size()) {
            return null;
        }
        Class<? extends Screen> screenClass = SCREENS_ID.get(id);
        try {
            return screenClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Screen createScreenById(int id, Screen screen) {
        if (id < 0 || id >= SCREENS_ID.size()) {
            return null;
        }
        Class<? extends Screen> screenClass = SCREENS_ID.get(id);
        try {
            return screenClass.getDeclaredConstructor(Screen.class).newInstance(screen);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void handleScreenOpeningPacket(TardisScreenOpeningS2C buf, ClientPlayNetworking.Context context) {
        int screenID = buf.screenID();
        Screen screen = Minecraft.getInstance().screen != null ? 
            createScreenById(screenID, Minecraft.getInstance().screen) 
            : createScreenById(screenID);
    
        if (screen != null) {
            Minecraft.getInstance().setScreen(screen);
        }
    }
}
