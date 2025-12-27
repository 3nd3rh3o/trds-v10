package ender.dwmod.dimensions;


import java.util.OptionalLong;

import ender.dwmod.DwMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

public class DimensionRegistry {
    public static final ResourceKey<Level> VORTEX_DIMENSION_KEY = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(DwMod.MOD_ID, "vortex"));
    public static final ResourceKey<DimensionType> VORTEX_DIMENSION_TYPE_KEY = ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.fromNamespaceAndPath(DwMod.MOD_ID, "vortex_type"));
    public static final ResourceKey<LevelStem> VORTEX_DIMENSION_OPTIONS_KEY = ResourceKey.create(Registries.LEVEL_STEM, ResourceLocation.fromNamespaceAndPath(DwMod.MOD_ID, "vortex"));

    public static void bootStrapType(BootstrapContext<DimensionType> context)
    {
        context.register(VORTEX_DIMENSION_TYPE_KEY, new DimensionType(
            OptionalLong.of(12000), // day length
            false, // has skylight
            false, // has ceiling
            false, // ultrawarm
            true, //natural
            1.0, // coordinate scale
            true, // bedworks
            false, // respawn anchor works
            0, // min y
            256, // height
            256, // logical height
            BlockTags.INFINIBURN_OVERWORLD, // infiniburn
            BuiltinDimensionTypes.OVERWORLD_EFFECTS, // effects
            0.0f, // ambient light
            new DimensionType.MonsterSettings(
                false, // piglins safe
                false, // has raid
                UniformInt.of(0, 0), // monster spawn light test
                0 // spawn light level limit
            )
        )
    );
    }
}