package ender.dwmod.tardis.systems.consoleTerminal;

import ender.dwmod.tardis.systems.architecturalReconfiguration.ATardisScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class TerminalScreen extends ATardisScreen {

    public TerminalScreen() {
        super(Component.nullToEmpty("TARDIS Console Terminal"));
    }

    public TerminalScreen(ATardisScreen parentScreen) {
        super(Component.nullToEmpty("TARDIS Console Terminal"), parentScreen);
    }

    @Override
    protected void init() {
        super.init();

        float scalingFactorH = (height - 40f) / 417f;
        float scalingFactorW = (width - 40f) / 417f;
        float scalingFactor = Math.min(scalingFactorH, scalingFactorW);
        scalingFactor = Math.max(0.1f, scalingFactor);

        int screenSide = Math.round(377f * scalingFactor);

        int xStart = width / 2 - screenSide / 2;
        int yStart = height / 2 - screenSide / 2;
        int xEnd = xStart + screenSide;
        int yEnd = yStart + screenSide;
        TextZoneWidget textZone = new TextZoneWidget(xStart, yStart, xEnd, yEnd);
        this.addRenderableWidget(textZone);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderBackground(guiGraphics);
    }

    private static void renderBackground(GuiGraphics guiGraphics) {
        float scalingFactorH = (guiGraphics.guiHeight() - 40f) / 417f;
        float scalingFactorW = (guiGraphics.guiWidth() - 40f) / 417f;
        float scalingFactor = Math.min(scalingFactorH, scalingFactorW);
        scalingFactor = Math.max(0.1f, scalingFactor);

        int screenSide = Math.round(377f * scalingFactor);
        int borderWidth = Math.round(20f * scalingFactor);
        renderCenteredSquare(guiGraphics, screenSide, 0xFF7cbf97);
        renderCenteredSquare(guiGraphics, screenSide - (2 * borderWidth), 0xFF1f1f1f);
    }

    private static void renderCenteredSquare(GuiGraphics guiGraphics, int squareSize, int color) {
        int yStart = (guiGraphics.guiHeight() - squareSize) / 2;
        int yEnd = guiGraphics.guiHeight() - yStart;
        int xStart = (guiGraphics.guiWidth() / 2) - (squareSize / 2);
        int xEnd = (guiGraphics.guiWidth() / 2) + (squareSize / 2);
        guiGraphics.fill(xStart, yStart, xEnd, yEnd, color);
    }
}
