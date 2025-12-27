package ender.dwmod.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.MinecraftServer;

public final class StructurePlacer {
    private static final List<StructureTask> tasks = new ArrayList<>();


    public static void addTask(StructureTask task) {
        tasks.add(task);
    }

    public static void tick(MinecraftServer server)
    {
        if (tasks.isEmpty())
            return;
        if (tasks.get(0).execute(server))
            tasks.remove(0);
    }
}
