package com.noobanidus.uv.core.hooks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenVillage;

import java.util.Random;

public class SpawnHooks {
    public static BlockPos getNearestStructurePos(MapGenVillage village, World worldIn, BlockPos pos, boolean findUnexplored)
    {
        village.world = worldIn;
        return village.findNearestStructurePosBySpacing(worldIn, village, pos, village.distance, 4, 10387312, false, 100, findUnexplored);
    }

    public static boolean canSpawnStructureAtCoords(MapGenVillage village, int chunkX, int chunkZ)
    {
        // Hopefully force villages to spawn more frequently
        village.distance = 8;

        int i = chunkX;
        int j = chunkZ;

        if (chunkX < 0)
        {
            chunkX -= village.distance - 1;
        }

        if (chunkZ < 0)
        {
            chunkZ -= village.distance - 1;
        }

        int k = chunkX / village.distance;
        int l = chunkZ / village.distance;
        Random random = village.world.setRandomSeed(k, l, 10387312);
        k = k * village.distance;
        l = l * village.distance;
        k = k + random.nextInt(village.distance - 4);
        l = l + random.nextInt(village.distance - 4);

        if (i == k && j == l)
        {
            return true;
        }

        return false;
    }
}
