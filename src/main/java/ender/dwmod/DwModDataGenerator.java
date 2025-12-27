package ender.dwmod;

import ender.dwmod.datagen.BuildingBlockItemModelProvider;
import ender.dwmod.datagen.DwModModelProvider;
import ender.dwmod.dimensions.DimensionRegistry;
import ender.dwmod.dimensions.WorldGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class DwModDataGenerator implements DataGeneratorEntrypoint {




    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(WorldGenerator::new);
        pack.addProvider(DwModModelProvider::new);
        pack.addProvider(BuildingBlockItemModelProvider::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(Registries.DIMENSION_TYPE, DimensionRegistry::bootStrapType);
    }


    
    
}
