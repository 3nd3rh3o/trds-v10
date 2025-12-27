package ender.dwmod.block.tardis.lamps;

import ender.dwmod.DwMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;

public final class TardisLampsRegistry {
    
    public static final Default TARDIS_DEFAULT_WALL_LAMP = new Default();
    public static final BlockItem TARDIS_DEFAULT_WALL_LAMP_ITEM = new BlockItem(TARDIS_DEFAULT_WALL_LAMP, new BlockItem.Properties());


    public static void register()
    {
        registerBlockItem("tardis_default_wall_lamp", TARDIS_DEFAULT_WALL_LAMP, TARDIS_DEFAULT_WALL_LAMP_ITEM);
    }


    private static void registerBlockItem(String name, TardisLamp block, BlockItem blockItem)
    {
        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(DwMod.MOD_ID, name), block);
        Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(DwMod.MOD_ID, name), blockItem);
    }
}
