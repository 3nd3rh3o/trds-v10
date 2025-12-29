package ender.dwmod.block;

import ender.dwmod.DwMod;
import ender.dwmod.block.tardis.complex.defaultExtDoor.DefaultExtDoorCoreBE;
import ender.dwmod.block.tardis.exoshell.DefaultConsoleBE;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockEntityInit {
    public static BlockEntityType<DefaultConsoleBE> DEFAULT_CONSOLE_BE;
    public static BlockEntityType<DefaultExtDoorCoreBE> DEFAULT_EXT_DOOR_CORE_BE;


    public static void registerBlockEntities() {
        DEFAULT_CONSOLE_BE = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE, 
            ResourceLocation.fromNamespaceAndPath(DwMod.MOD_ID, "default_console"), 
            BlockEntityType.Builder.of(DefaultConsoleBE::new, BlockInit.DEFAULT_CONSOLE).build()
        );
        DEFAULT_EXT_DOOR_CORE_BE = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE, 
            ResourceLocation.fromNamespaceAndPath(DwMod.MOD_ID, "default_ext_door_core"), 
            BlockEntityType.Builder.of(DefaultExtDoorCoreBE::new, BlockInit.DEFAULT_EXT_DOOR_CORE).build()
        );
        
    }
    
}
