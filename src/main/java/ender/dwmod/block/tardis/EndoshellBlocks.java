package ender.dwmod.block.tardis;

import ender.dwmod.DwMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class EndoshellBlocks {

    public static final Block TARDIS_DEFAULT_FLOOR = new Block(Properties.ofFullCopy(Blocks.BEDROCK));
    public static final BlockItem TARDIS_DEFAULT_FLOOR_ITEM = new BlockItem(TARDIS_DEFAULT_FLOOR, new BlockItem.Properties());
    public static final Block TARDIS_DEFAULT_WALL = new Block(Properties.ofFullCopy(Blocks.BEDROCK));
    public static final BlockItem TARDIS_DEFAULT_WALL_ITEM = new BlockItem(TARDIS_DEFAULT_WALL, new BlockItem.Properties());


    public static void register() {
        registerBuildBlock("tardis_default_floor", TARDIS_DEFAULT_FLOOR, TARDIS_DEFAULT_FLOOR_ITEM);
        registerBuildBlock("tardis_default_wall", TARDIS_DEFAULT_WALL, TARDIS_DEFAULT_WALL_ITEM);
    }


    private static void registerBuildBlock(String name, Block block, BlockItem blockItem) {
        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(DwMod.MOD_ID, name), block);
        Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(DwMod.MOD_ID, name), blockItem);
    }
    
}
