package ender.dwmod.tardis.systems.architecturalReconfiguration;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ArsScreen extends ATardisScreen {
    
    public ArsScreen()
    {
        super(Component.nullToEmpty("Architectural Reconfiguration System"));
    }

    public ArsScreen(Screen parentScreen)
    {
        super(Component.nullToEmpty("Architectural Reconfiguration System"), parentScreen);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}
