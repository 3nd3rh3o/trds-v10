package ender.dwmod.block;

import ender.dwmod.DwMod;
import ender.dwmod.block.tardis.EndoshellBlocks;
import ender.dwmod.block.tardis.complex.defaultExtDoor.DefaultExtDoorCore;
import ender.dwmod.block.tardis.exoshell.DefaultConsole;
import ender.dwmod.block.tardis.exoshell.DefaultConsoleInteract;
import ender.dwmod.block.tardis.lamps.TardisLampsRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public abstract class BlockInit {
    
    public static final Block DEFAULT_CONSOLE_INTERACT = new DefaultConsoleInteract(Properties.ofFullCopy(Blocks.BEDROCK).noOcclusion().isViewBlocking(Blocks::never).dynamicShape());
    public static final Block DEFAULT_CONSOLE = new DefaultConsole(Properties.ofFullCopy(Blocks.BEDROCK).noOcclusion().isViewBlocking(Blocks::never).dynamicShape());
    public static final Block DEFAULT_EXT_DOOR_CORE = new DefaultExtDoorCore(Properties.ofFullCopy(Blocks.BEDROCK).noOcclusion().isViewBlocking(Blocks::never).dynamicShape());


    public static void init() 
    {
        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(DwMod.MOD_ID, "default_console_interact"), DEFAULT_CONSOLE_INTERACT);
        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(DwMod.MOD_ID, "default_console"), DEFAULT_CONSOLE);
        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(DwMod.MOD_ID, "default_ext_door_core"), DEFAULT_EXT_DOOR_CORE);
        EndoshellBlocks.register();
        TardisLampsRegistry.register();
        
    }
}
