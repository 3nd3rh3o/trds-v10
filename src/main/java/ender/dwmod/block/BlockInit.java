package ender.dwmod.block;

import ender.dwmod.DwMod;
import ender.dwmod.block.tardis.exoshell.ExoshellBB;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public abstract class BlockInit {
    
    public static final Block EXOSHELL_BB = new ExoshellBB(Properties.ofFullCopy(Blocks.BEDROCK));

    public static void init() 
    {
        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(DwMod.MOD_ID, "exoshell_bb"), EXOSHELL_BB);
    }
}
