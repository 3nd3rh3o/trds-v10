package ender.dwmod.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import ender.dwmod.DwMod;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ChunkResult;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

public class StructureTask {
    private final String structurePath;
    private final String structureNameSpace;
    private final BlockPos target;
    private final ResourceKey<Level> dimension;
    private final boolean shouldUnloadOnFinish;
    private final Runnable onFinish;
    private boolean completed = false;
    private boolean started = false;

    public StructureTask(String structurePath, BlockPos target, ResourceKey<Level> dimension)
    {
        this(structurePath, target, dimension, true, null);
    }

    public StructureTask(String structurePath, BlockPos target, ResourceKey<Level> dimension, boolean shouldUnloadOnFinish, Runnable onFinish)
    {
        this(DwMod.MOD_ID, structurePath, target, dimension, shouldUnloadOnFinish, onFinish);
    }

    public StructureTask(String structureNameSpace, String structurePath, BlockPos target, ResourceKey<Level> dimension, boolean shouldUnloadOnFinish, Runnable onFinish)
    {
        this.structurePath = structurePath;
        this.structureNameSpace = structureNameSpace;
        this.target = target;
        this.dimension = dimension;
        this.shouldUnloadOnFinish = shouldUnloadOnFinish;
        this.onFinish = onFinish;
    }


    public boolean execute(MinecraftServer server)
    {
        if (started)
            return completed;
        // ensure loaded
        ServerLevel level = server.getLevel(dimension);
        if (level == null)
        {
            DwMod.LOGGER.error("Could not find dimension {} for structure placement!", dimension.location());
            completed = true;
            started = true;
            return completed;
        }
        ChunkPos cPos = new ChunkPos(target);

        // compute necessary chunks
        server.getStructureManager().get(ResourceLocation.fromNamespaceAndPath(structureNameSpace, structurePath))
            .ifPresentOrElse((structTemplate) ->
                {
                    int dimX = Math.floorDiv(structTemplate.getSize().getX() + target.getX() - 1, 16) - Math.floorDiv(target.getX(), 16) + 1;
                    int dimZ = Math.floorDiv(structTemplate.getSize().getZ() + target.getZ() - 1, 16) - Math.floorDiv(target.getZ(), 16) + 1;
                    List<CompletableFuture<ChunkResult<ChunkAccess>>> futures = new ArrayList<>();
                    for (int x = 0; x < dimX; x++)
                    {
                        for (int z = 0; z < dimZ; z++)
                        {
                            ChunkPos pos = new ChunkPos(cPos.x + x, cPos.z + z);
                            futures.add(level.getChunkSource().getChunkFuture(pos.x, pos.z, ChunkStatus.FULL, true));
                        }
                    }

                    CompletableFuture<Void> all = CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));

                    
                    all.handleAsync((ignored, ex) -> {
                        if (ex != null) {
                            DwMod.LOGGER.error("Error loading chunks for structure placement", ex);
                            completed = true;
                            return null;
                        }

                        for (int i = 0; i < futures.size(); i++)
                        {
                            ChunkResult<ChunkAccess> r = futures.get(i).join();
                            if (!r.isSuccess())
                            {
                                DwMod.LOGGER.warn("Chunk loading failed for {} {} : {}", cPos.x + (i / dimZ), cPos.z + (i % dimZ), r.getError());
                                completed = true;
                                return null;
                            }
                        }

                        // mark chunks as forceloaded
                        for (int x = 0; x < dimX; x++)
                        {
                            for (int z = 0; z < dimZ; z++)
                            {
                                ChunkPos pos = new ChunkPos(cPos.x + x, cPos.z + z);
                                level.getChunkSource().updateChunkForced(pos, true);
                            }
                        }


                        StructurePlaceSettings settings = new StructurePlaceSettings();
                        try { // in case of error, this prevents forceload leak
                            structTemplate.placeInWorld(level, target, target, settings, level.getRandom(), Block.UPDATE_ALL);
                            // execute onFinish if specified
                            if (onFinish != null)
                                onFinish.run();
                        }
                        finally 
                        {
                            // remove forceload if specified
                            if (shouldUnloadOnFinish)
                            {
                                for (int x = 0; x < dimX; x++)
                                {
                                    for (int z = 0; z < dimZ; z++)
                                    {
                                        ChunkPos pos = new ChunkPos(cPos.x + x, cPos.z + z);
                                        level.getChunkSource().updateChunkForced(pos, false);
                                    }
                                }
                            }
                            completed = true;
                        }
                        return null;                        
                    }, server);

                }, () -> {
                    DwMod.LOGGER.error("Could not find structure {}:{} !", structureNameSpace, structurePath);
                    completed = true;
                });
        started = true;
        return completed;
    }
}
