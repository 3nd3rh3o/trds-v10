package ender.dwmod.tardis.systems.architecturalReconfiguration;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ArsScreen extends Screen {
    public ArsScreen(Component title)
    {
        super(title);
    }

    @Override
    protected void init() {
        Button exitButton = Button.builder(Component.nullToEmpty("Done"), btn ->
        {
            this.onClose();
        }).bounds(this.width / 2 - 50, this.height - 30, 100, 20).build();
        this.addRenderableWidget(exitButton);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}
