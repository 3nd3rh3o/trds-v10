package ender.dwmod.tardis.systems.architecturalReconfiguration;

import ender.dwmod.DwMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;

public class Rooms {


    public static class CONSOLE_ROOM {
        public static class DEFAULT {
            public static final String NAMESPACE = DwMod.MOD_ID;
            public static final String PATH = "tardis-console_room-default";
            public static final Vec3i SIZE = new Vec3i(1, 1, 1);
            public static final String NAME = "console_room_default";
            public static final byte[] CONNECTIVITY = new byte[] {0, 0, 0, 0, 0, 0};
        }
    }


















    public static String[] getPathArray(String path, Vec3i size) {
        String[] pathArray = new String[size.getX() * size.getY() * size.getZ()];
        for (int x = 0; x < size.getX(); x++)
        {
            for (int y = 0; y < size.getY(); y++)
            {
                for (int z = 0; z < size.getZ(); z++)
                {
                    pathArray[x + y * size.getX() + z * size.getX() * size.getY()] =
                        
                            path + "_" + x + "_" + y + "_" + z;
                }
            }
        }
        return pathArray;
    }


















    public static Vec3 getFeaturePos(String structureName, String featureName, BlockPos worldPosition) {
        return switch (structureName) {
            case CONSOLE_ROOM.DEFAULT.NAME -> switch (featureName) {
                case "console_room_entrance" -> new Vec3(
                    worldPosition.getX() + 14,
                    worldPosition.getY() + 17.5,
                    worldPosition.getZ() + 1.5
                );
                default -> null;
            };
            default -> null;
        };
    }
}
