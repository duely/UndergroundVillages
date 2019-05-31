package com.noobanidus.uv.core.hooks;

import com.noobanidus.uv.UV;
import com.noobanidus.uv.Util;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureVillagePieces;

@SuppressWarnings("unused")
public class GroundHooks {
    public static int getAverageGroundLevel(StructureVillagePieces.Village village, World worldIn, StructureBoundingBox structurebb) {
        int i = 0;
        int j = 0;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        int min = 256;
        int max = 0;

        for (int k = village.boundingBox.minZ; k <= village.boundingBox.maxZ; ++k) {
            for (int l = village.boundingBox.minX; l <= village.boundingBox.maxX; ++l) {
                blockpos$mutableblockpos.setPos(l, 64, k);

                if (structurebb.isVecInside(blockpos$mutableblockpos)) {
                    int bottom = Util.getCaveY(worldIn, blockpos$mutableblockpos);
                    if (bottom < min) {
                        min = bottom;
                    }
                    if (bottom > max) {
                        max = bottom;
                    }
                    i += Math.max(bottom, worldIn.provider.getAverageGroundLevel() - 1);
                    ++j;
                }
            }
        }

        if (j <= 0 || max - min > UV.UVConfig.MAX_AVERAGE || min == -1) {
            return -1;
        } else {
            int avg = i / j;
            return avg;
        }
    }

    public static IBlockState getBiomeSpecificBlockState(StructureVillagePieces.Village village, IBlockState blockstateIn) {
        return blockstateIn;
    }
}
