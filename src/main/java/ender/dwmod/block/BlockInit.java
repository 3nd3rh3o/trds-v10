package ender.dwmod.block;

import ender.dwmod.DwMod;
import ender.dwmod.block.tardis.exoshell.DefaultConsole;
import ender.dwmod.block.tardis.exoshell.DefaultConsoleInteract;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public abstract class BlockInit {
    
    public static final Block DEFAULT_CONSOLE_INTERACT = new DefaultConsoleInteract(Properties.ofFullCopy(Blocks.BEDROCK).noOcclusion().isViewBlocking(Blocks::never));
    public static final Block DEFAULT_CONSOLE = new DefaultConsole(Properties.ofFullCopy(Blocks.BEDROCK).noOcclusion().isViewBlocking(Blocks::never));

    public static void init() 
    {
        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(DwMod.MOD_ID, "default_console_interact"), DEFAULT_CONSOLE_INTERACT);
        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(DwMod.MOD_ID, "default_console"), DEFAULT_CONSOLE);
    }
}
