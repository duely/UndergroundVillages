package com.noobanidus.uv.world;

import com.noobanidus.uv.Util;
import com.noobanidus.uv.UV;
import com.noobanidus.uv.init.Reference;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;
import java.util.Random;

@Mod.EventBusSubscriber(modid = UV.MODID)
@SuppressWarnings("unused")
public class PopulatePost {
    private static Random random = new Random();
    private static Map<ChunkPos, Long> villageChunks = new Object2LongOpenHashMap<>();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPopulatePost(PopulateChunkEvent.Post event) {
        int chunkx = event.getChunkX();
        int chunkz = event.getChunkZ();
        World world = event.getWorld();

        Chunk chunk = world.getChunk(chunkx, chunkz);
        BlockPos bChunk = new BlockPos(chunkx * 16, 0, chunkz * 16);

        boolean village = event.isHasVillageGenerated();
        if (village) {
            if (UV.UVConfig.REMOVE_LAVA) {
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        for (int y = UV.UVConfig.MIN_CAVE_Y - 5; y <= UV.UVConfig.MAX_CAVE_Y; y++) {
                            IBlockState state = world.getBlockState(bChunk.add(x, y, z));
                            if (state.getMaterial().isLiquid() && (state.getBlock() == Blocks.LAVA || state.getBlock() == Blocks.FLOWING_LAVA)) {
                                world.setBlockState(bChunk.add(x, y, z), Blocks.STONE.getDefaultState(), Constants.BlockFlags.NO_OBSERVERS | Constants.BlockFlags.SEND_TO_CLIENTS);
                            }
                        }
                    }
                }
            }

            if (UV.UVConfig.DO_LIGHT) {
                for (int x = 2; x < 16; x += 6) {
                    for (int z = 2; z < 16; z += 6) {
                        int y = Util.getCaveY(world, bChunk.add(x, 0, z));
                        int x2;
                        if (random.nextBoolean()) {
                            x2 = x + random.nextInt(2);
                        } else {
                            x2 = x - random.nextInt(2);
                        }
                        int z2;
                        if (random.nextBoolean()) {
                            z2 = z + random.nextInt(2);
                        } else {
                            z2 = z - random.nextInt(2);
                        }

                        tryPlacingTorch(bChunk.add(x2, y, z2), world);
                    }
                }
            }
        }
    }

    private static void tryPlacingTorch(BlockPos pos, World world) {
        IBlockState state = world.getBlockState(pos.down());
        if (state.getBlock().canPlaceTorchOnTop(state, world, pos.down())) {
            // TODO: Make configurable
            world.setBlockState(pos, UV.UVConfig.getLightBlock(), Constants.BlockFlags.NO_OBSERVERS | Constants.BlockFlags.SEND_TO_CLIENTS);
        }
    }
}
