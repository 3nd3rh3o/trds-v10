package ender.dwmod.tardis.networking;

import net.minecraft.world.phys.Vec3;

public final class EncodingHelpers {
    public static String fromVec3(Vec3 vec3)
    {
        return vec3.x + ";" + vec3.y + ";" + vec3.z;
    }

    public static Vec3 toVec3(String str)
    {
        String[] parts = str.split(";");
        return new Vec3(
            Double.parseDouble(parts[0]),
            Double.parseDouble(parts[1]),
            Double.parseDouble(parts[2])
        );
    }
}
