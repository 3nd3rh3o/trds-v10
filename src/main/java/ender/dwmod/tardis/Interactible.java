package ender.dwmod.tardis;

import ender.dwmod.utils.RayCastShape.Sphere;
import net.minecraft.world.phys.Vec3;

public class Interactible {
    private final Sphere raycast_target;
    private final int cooldown;
    private int cooldown_t;


    public Interactible(Sphere raycast_target, int cooldown) {
        this.raycast_target = raycast_target;
        this.cooldown = cooldown;
        this.cooldown_t = 0;
    }


    public boolean interacted(Vec3 rayOrigin, Vec3 rayDir)
    {
        if (cooldown_t == 0 && raycast_target.rayCast(rayOrigin, rayDir))
        {
            return true;
        }
        return false;
    }

    public void setOnCooldown()
    {
        this.cooldown_t = this.cooldown;
    }

    public void tick()
    {
        if (cooldown_t > 0)
        {
            cooldown_t--;
        }
    }


    public Vec3 getCenter() {
        return raycast_target.getCenter();
    }
}
