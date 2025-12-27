package ender.dwmod.utils.RayCastShape;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
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
        // TODO - render a sphere at center with radius

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
