package ender.dwmod.block;

import ender.dwmod.DwMod;
import ender.dwmod.block.tardis.exoshell.DefaultConsoleBE;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockEntityInit {
    public static BlockEntityType<DefaultConsoleBE> DEFAULT_CONSOLE_BE;


    public static void registerBlockEntities() {
        DEFAULT_CONSOLE_BE = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE, 
            ResourceLocation.fromNamespaceAndPath(DwMod.MOD_ID, "default_console"), 
            BlockEntityType.Builder.of(DefaultConsoleBE::new, BlockInit.DEFAULT_CONSOLE).build()
        );
        
    }
    
}
