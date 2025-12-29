package client.ender.dwmod;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import client.ender.dwmod.blockEntity.console.DefaultConsoleRenderer;
import client.ender.dwmod.blockEntity.defaultExtDoor.DefaultExtDoorRenderer;
import client.ender.dwmod.entities.tardisEntity.TardisEntityRenderer;
import ender.dwmod.DwMod;
import ender.dwmod.block.BlockEntityInit;
import ender.dwmod.block.BlockInit;
import ender.dwmod.entities.EntityInit;
import ender.dwmod.entities.IMultiCollidable;
import ender.dwmod.entities.tardis.exoshell.TardisEntity;
import ender.dwmod.tardis.TardisNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class DwModClient implements net.fabricmc.api.ClientModInitializer {
    public static final String MOD_ID = DwMod.MOD_ID;
    public static final org.slf4j.Logger LOGGER = DwMod.LOGGER;

    @Override
    public void onInitializeClient() {
        LOGGER.info("dwmod client loaded.");
        
        EntityRendererRegistry.register(EntityInit.TARDIS, TardisEntityRenderer::new);
        BlockEntityRenderers.register(BlockEntityInit.DEFAULT_CONSOLE_BE, DefaultConsoleRenderer::new);
        BlockEntityRenderers.register(BlockEntityInit.DEFAULT_EXT_DOOR_CORE_BE, DefaultExtDoorRenderer::new);

        TardisNetworking.registerClientReceivers();

        WorldRenderEvents.BLOCK_OUTLINE.register((worldRenderContext, blockOutlineContext) -> {
            BlockState state = blockOutlineContext.blockState();

            
            if (state.is(BlockInit.DEFAULT_CONSOLE) || state.is(BlockInit.DEFAULT_CONSOLE_INTERACT)) {
                return false; 
            }

            return true;
        });

        // Render multiAABB
        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(context -> 
            {
                Minecraft mc = Minecraft.getInstance();
                EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();

                if (!dispatcher.shouldRenderHitBoxes())
                    return;

                Camera cam = context.camera();
                Vec3 camPos = cam.getPosition();

                PoseStack ps = context.matrixStack();

                MultiBufferSource buffs = mc.renderBuffers().bufferSource();
                VertexConsumer consumer = buffs.getBuffer(RenderType.lines());

                ClientLevel level = mc.level;
                if (level != null) 
                {
                    for (TardisEntity tardis : level.getEntitiesOfClass(TardisEntity.class, AABB.ofSize(camPos, 100, 100, 100), e -> true)) 
                    {
                        if (!(tardis instanceof IMultiCollidable mColl))
                            continue;
                        List<AABB> aabbs = mColl.getColliders();
                        for (AABB aabb : aabbs) 
                        {
                            ps.pushPose();
                            ps.translate(tardis.position().x - camPos.x, tardis.position().y - camPos.y, tardis.position().z - camPos.z);
                            LevelRenderer.renderLineBox(
                                ps,
                                consumer,
                                aabb.minX, aabb.minY, aabb.minZ,
                                aabb.maxX, aabb.maxY, aabb.maxZ,
                                0f, 1f, 1f, 1f
                            );
                            ps.popPose();
                        }
                    }
                }
            }
        );
    }
}