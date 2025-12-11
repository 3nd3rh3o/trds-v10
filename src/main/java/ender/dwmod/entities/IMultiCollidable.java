package ender.dwmod.entities;

import java.util.List;

import net.minecraft.world.phys.AABB;

public interface IMultiCollidable {
    List<AABB> getColliders();
}