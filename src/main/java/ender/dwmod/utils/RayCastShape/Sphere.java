package ender.dwmod.utils.RayCastShape;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class Sphere {
    private final Vec3 center;
    private final float radius;



    public Sphere(Vec3 center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    public void render(WorldRenderContext context)
    {
        // TODO - render sphere gizmo at center with radius.
        Minecraft mc = Minecraft.getInstance();
        EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();

        if (!dispatcher.shouldRenderHitBoxes())
            return;

        Camera cam = context.camera();
        Vec3 camPos = cam.getPosition();

        PoseStack ps = context.matrixStack();

        MultiBufferSource buffs = mc.renderBuffers().bufferSource();
        VertexConsumer consumer = buffs.getBuffer(RenderType.debugQuads());

        ClientLevel level = mc.level;
        if (level != null) 
        {
            ps.pushPose();
            ps.translate(- camPos.x, - camPos.y,  - camPos.z);
            // TODO - render voxel sphere of res 2 at center with radius.
            
            ps.popPose();
        }

    }

	public boolean rayCast(Vec3 origin, Vec3 hitDir) {
		Vec3 L = center.subtract(origin);
        float tca = (float) L.dot(hitDir);
        float d2 = (float) L.dot(L) - tca * tca;
        if (d2 > radius * radius) return false;
        float thc = (float) Math.sqrt(radius * radius - d2);
        float t0 = tca - thc;
        float t1 = tca + thc;
        if (t0 < 0 && t1 < 0) return false;
        return true;
	}

    public Vec3 getCenter() {
        return center;
    }
}
