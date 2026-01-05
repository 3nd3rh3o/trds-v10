package ender.dwmod.tardis.systems.consoleTerminal;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.google.common.primitives.UnsignedInteger;

import client.ender.dwmod.ClientTardisRegistries;
import ender.dwmod.tardis.components.Terminal;
import ender.dwmod.tardis.networking.TardisUpdateValueC2S;
import ender.dwmod.utils.StringToComponentParser;

public class TextZoneWidget extends AbstractWidget {

    private UnsignedInteger tardisID;

    public TextZoneWidget(int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty());
        Minecraft mc = Minecraft.getInstance();
        BlockPos pos = mc.player.blockPosition();
        this.tardisID = ClientTardisRegistries.get(pos).getID();
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        float scalingFactorH = (guiGraphics.guiHeight() - 40f) / 417f;
        float scalingFactorW = (guiGraphics.guiWidth() - 40f) / 417f;
        float scalingFactor = Math.min(scalingFactorH, scalingFactorW);
        scalingFactor = Math.max(0.1f, scalingFactor);

        if (scalingFactor <= 0f) {
            return;
        }

        int innerSide = Math.round((377f - 40f) * scalingFactor);
        int padding = Math.round(10f * scalingFactor);
        int xDesired = (guiGraphics.guiWidth() - innerSide) / 2 + padding;
        int yDesired = (guiGraphics.guiHeight() - innerSide) / 2 + padding;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scalingFactor, scalingFactor, 1f);
        int xStart = Math.round(xDesired / scalingFactor);
        int yStart = Math.round(yDesired / scalingFactor);
        String shown = ClientTardisRegistries.getTardis(tardisID).getComponentByName(Terminal.name()).getStringValue("terminal_text");
        List<Component> strs = StringToComponentParser.parseStringToComponents(shown);
        for (int i = 0; i < strs.size(); i++) {
            if (isFocused() && i == strs.size() - 1)
                strs.set(i, Component.literal(strs.get(i).getString().concat("_")).withStyle(strs.get(i).getStyle()));
            guiGraphics.drawString(Minecraft.getInstance().font, strs.get(i), xStart, yStart + (i * Minecraft.getInstance().font.lineHeight), 0xFFFFFFFF);
        }
        guiGraphics.pose().popPose();
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.isFocused()) {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }

        if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            ClientPlayNetworking.send(new TardisUpdateValueC2S(tardisID.intValue(), "terminal", "terminal_text", "BACKSPACE"));
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            ClientPlayNetworking.send(new TardisUpdateValueC2S(tardisID.intValue(), "terminal", "terminal_text", "ENTER"));
            return true;
        }
        
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (!this.isFocused()) {
            return super.charTyped(codePoint, modifiers);
        }

        
        if (!Character.isISOControl(codePoint)) {
            String charStr = Character.toString(codePoint);
            ClientPlayNetworking.send(new TardisUpdateValueC2S(tardisID.intValue(), "terminal", "terminal_text", charStr));
            return true;
        }

        return super.charTyped(codePoint, modifiers);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        return;
    }
}
