package client.ender.dwmod.blockEntity.defaultExtDoor;

import client.ender.dwmod.ClientTardisRegistries;
import ender.dwmod.block.tardis.complex.defaultExtDoor.DefaultExtDoorCoreBE;
import ender.dwmod.tardis.Tardis;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LightLayer;
import qouteall.imm_ptl.core.ClientWorldLoader;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class DefaultExtDoorRenderer extends GeoBlockRenderer<DefaultExtDoorCoreBE> {
    public DefaultExtDoorRenderer(BlockEntityRendererProvider.Context ctx) {
        super(new DefaultExtDoorModel());
    }

    @Override
    public void render(DefaultExtDoorCoreBE animatable, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        int overriddenPackedLight = getRemotePackedLight(animatable, packedLight);
        super.render(animatable, partialTick, poseStack, bufferSource, overriddenPackedLight, packedOverlay);
    }

    private static int getRemotePackedLight(DefaultExtDoorCoreBE animatable, int fallbackPackedLight) {
        if (animatable == null) {
            return fallbackPackedLight;
        }

        Tardis tardis = ClientTardisRegistries.get(animatable.getBlockPos());
        if (tardis == null || tardis.getPosition() == null || tardis.getDimension() == null) {
            return fallbackPackedLight;
        }

        ClientLevel remoteLevel = ClientWorldLoader.getWorld(tardis.getDimension());
        if (remoteLevel == null) {
            return fallbackPackedLight;
        }

        BlockPos remotePos = BlockPos.containing(tardis.getPosition());
        int block = remoteLevel.getBrightness(LightLayer.BLOCK, remotePos);
        int sky = remoteLevel.getBrightness(LightLayer.SKY, remotePos);
        return LightTexture.pack(block, sky);
    }
}
