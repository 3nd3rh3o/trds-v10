package ender.dwmod.datagen;

import com.google.gson.JsonObject;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

public class BuildingBlockItemModelProvider implements DataProvider {

    private final PackOutput.PathProvider itemModelPathProvider;

    public BuildingBlockItemModelProvider(FabricDataOutput output) {
        this.itemModelPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/item");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        Block[] blocks = DwModModelProvider.BUILDING_BLOCKS;
        CompletableFuture<?>[] futures = new CompletableFuture<?>[blocks.length];

        for (int i = 0; i < blocks.length; i++) {
            Block block = blocks[i];
            Item item = block.asItem();

            if (item == Items.AIR) {
                throw new IllegalStateException("Building block has no item: " + BuiltInRegistries.BLOCK.getKey(block));
            }

            ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
            ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item);

            JsonObject json = new JsonObject();
            json.addProperty("parent", blockId.getNamespace() + ":block/" + blockId.getPath());

            Path outPath = itemModelPathProvider.json(itemId);
            futures[i] = DataProvider.saveStable(cachedOutput, json, outPath);
        }

        return CompletableFuture.allOf(futures);
    }

    @Override
    public String getName() {
        return "DwMod Building Block Item Models";
    }
}
