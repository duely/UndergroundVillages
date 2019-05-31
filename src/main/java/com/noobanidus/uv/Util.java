package com.noobanidus.uv;

import com.noobanidus.uv.init.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.Constants;

public class Util {
    public static int getCaveY(World world, BlockPos pos) {
        int airCount = 0;
        int solid = 0;

        for (int y = UV.UVConfig.MIN_CAVE_Y - 5; y <= UV.UVConfig.MAX_CAVE_Y; y++) {
            BlockPos nPos = new BlockPos(pos.getX(), y, pos.getZ());

            IBlockState state = world.getBlockState(nPos);
            if (state.getBlock() == Blocks.AIR) {
                if (solid != 0) {
                    airCount++;
                    if (airCount >= 5) {
                        return solid + 1;
                    }
                }
            } else {
                solid = y;
            }
        }

        return -1;
    }
}
