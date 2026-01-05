package client.ender.dwmod.blockEntity.console;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import client.ender.dwmod.ClientTardisRegistries;
import ender.dwmod.block.tardis.exoshell.DefaultConsoleBE;
import ender.dwmod.dimensions.DimensionRegistry;
import ender.dwmod.tardis.components.Terminal;
import ender.dwmod.utils.StringToComponentParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.network.chat.Component;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class DefaultConsoleRenderer extends GeoBlockRenderer<DefaultConsoleBE> {
    public DefaultConsoleRenderer(BlockEntityRendererProvider.Context ctx) {
        super(new DefaultConsoleModel());
    }

    @Override
    public void renderFinal(PoseStack poseStack, DefaultConsoleBE animatable, BakedGeoModel model,
            MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight,
            int packedOverlay, int colour) {
        super.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, colour);
        if (!animatable.getLevel().dimension().equals(DimensionRegistry.VORTEX_DIMENSION_KEY))
            return;
        renderTerminalText(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, colour);
    }

    public void renderTerminalText(PoseStack poseStack, DefaultConsoleBE animatable, BakedGeoModel model,
            MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight,
            int packedOverlay, int colour) {
        poseStack.pushPose();
        Minecraft mc = Minecraft.getInstance();
        poseStack.translate(-12.2092f/16f + 0.5f, 18f/16f, 2.2342f/16f + 0.5f);
        poseStack.mulPose(new Quaternionf().rotateY(-60f * ((float)Math.PI / 180f)).rotateX(-70f * ((float)Math.PI / 180f)));
        float scale = 0.0005f;
        poseStack.scale(scale, -scale, scale);
        Matrix4f matrix = poseStack.last().pose();
        Font font = mc.font;

        String text = ClientTardisRegistries.get(animatable.getBlockPos()).getComponentByName(Terminal.name()).getStringValue("terminal_text");
        List<Component> components = StringToComponentParser.parseStringToComponents(text);

        float startX = 0f;
        int backgroundColor = 0;
        int textColor = -1;
        for (int i = 0; i < components.size(); i++) {
            font.drawInBatch(components.get(i), startX, i*font.lineHeight, textColor, false, matrix, bufferSource, DisplayMode.NORMAL, backgroundColor, packedLight);   
        }
        poseStack.popPose();
    }
}
