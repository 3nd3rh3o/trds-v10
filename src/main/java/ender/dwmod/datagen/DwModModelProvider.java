package ender.dwmod.datagen;

import ender.dwmod.DwMod;
import ender.dwmod.block.BlockInit;
import ender.dwmod.block.tardis.EndoshellBlocks;
import com.google.gson.JsonObject;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

/**
 * Generates only the blockstate/model JSON we actually want to autogenerate.
 *
 * Important: Vanilla's {@code net.minecraft.data.models.ModelProvider} performs a strict
 * completeness check and will crash if ANY registered block is missing a blockstate definition,
 * even if you provide that blockstate as hand-written JSON in resources.
 *
 * To allow hand-authored blockstates, we avoid using {@code FabricModelProvider} entirely and
 * write the required JSON files ourselves.
 */
public class DwModModelProvider implements DataProvider {

    private static final ResourceLocation EMPTY_MODEL = ResourceLocation.fromNamespaceAndPath(DwMod.MOD_ID, "block/empty");

    private final PackOutput.PathProvider blockStatePathProvider;
    private final PackOutput.PathProvider blockModelPathProvider;

    /**
     * Single source of truth for all simple "building blocks".
     * Add new blocks here once; both blockstate/model and item model generation will follow.
     */
    public static final Block[] BUILDING_BLOCKS = {
        EndoshellBlocks.TARDIS_DEFAULT_FLOOR,
        EndoshellBlocks.TARDIS_DEFAULT_WALL
    };

    public DwModModelProvider(FabricDataOutput output) {
        this.blockStatePathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
        this.blockModelPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/block");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        // Barrier-like / invisible blocks: only emit the blockstate, model is a hand-authored file.
        CompletableFuture<?> f1 = saveSimpleBlockState(cachedOutput, BlockInit.DEFAULT_CONSOLE_INTERACT, EMPTY_MODEL);
        CompletableFuture<?> f2 = saveSimpleBlockState(cachedOutput, BlockInit.DEFAULT_CONSOLE, EMPTY_MODEL);

        // Building blocks: cube_all model + trivial blockstate.
        CompletableFuture<?>[] building = new CompletableFuture<?>[BUILDING_BLOCKS.length * 2];
        for (int i = 0; i < BUILDING_BLOCKS.length; i++) {
            Block block = BUILDING_BLOCKS[i];
            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
            if (!DwMod.MOD_ID.equals(id.getNamespace())) {
                throw new IllegalStateException("BUILDING_BLOCKS contains non-" + DwMod.MOD_ID + " block: " + id);
            }

            ResourceLocation modelId = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "block/" + id.getPath());
            ResourceLocation textureId = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "block/" + id.getPath());

            building[i * 2] = saveCubeAllBlockModel(cachedOutput, id, textureId);
            building[i * 2 + 1] = saveSimpleBlockState(cachedOutput, block, modelId);
        }

        return CompletableFuture.allOf(f1, f2, CompletableFuture.allOf(building));
    }

    private CompletableFuture<?> saveSimpleBlockState(CachedOutput cachedOutput, Block block, ResourceLocation modelId) {
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
        JsonObject root = new JsonObject();
        JsonObject variants = new JsonObject();
        JsonObject variant = new JsonObject();
        variant.addProperty("model", modelId.getNamespace() + ":" + modelId.getPath());
        variants.add("", variant);
        root.add("variants", variants);

        Path outPath = blockStatePathProvider.json(blockId);
        return DataProvider.saveStable(cachedOutput, root, outPath);
    }

    private CompletableFuture<?> saveCubeAllBlockModel(CachedOutput cachedOutput, ResourceLocation blockId, ResourceLocation textureAll) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:block/cube_all");

        JsonObject textures = new JsonObject();
        textures.addProperty("all", textureAll.getNamespace() + ":" + textureAll.getPath());
        root.add("textures", textures);

        Path outPath = blockModelPathProvider.json(blockId);
        return DataProvider.saveStable(cachedOutput, root, outPath);
    }

    @Override
    public String getName() {
        return "DwMod Blockstates + Block Models";
    }
}
