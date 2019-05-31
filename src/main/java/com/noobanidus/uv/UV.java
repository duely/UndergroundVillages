package com.noobanidus.uv;

import com.google.common.collect.ImmutableList;
import com.noobanidus.uv.world.BiomeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod(modid = UV.MODID, name = UV.NAME, version = UV.VERSION)
public class UV {
    public static final String MODID = "uv";
    public static final String NAME = "Warm Stone";
    public static final String VERSION = "1.0.0";

    @Mod.Instance(MODID)
    public static UV instance;

    public static Logger LOG;

    @EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        LOG = event.getModLog();
        BiomeEvent handler = new BiomeEvent();
        //MinecraftForge.TERRAIN_GEN_BUS.register(handler);
        //MinecraftForge.ORE_GEN_BUS.register(handler);
    }

    @EventHandler
    public static void init(FMLInitializationEvent event) {
    }

    @EventHandler
    public static void postInit(FMLPostInitializationEvent event) {
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        World world = DimensionManager.getWorld(0);
        world.setSeaLevel(23);
    }

    @EventHandler
    public static void loadComplete(FMLLoadCompleteEvent event) {
    }

    @Config(modid=UV.MODID)
    public static class UVConfig {
        @Config.Comment("Minimum height to stop looking for village placement at")
        @Config.RangeInt(min=0, max=255)
        public static int MIN_CAVE_Y = 25;

        @Config.Comment("Maximum height to stop looking for village placement at")
        @Config.RangeInt(min=0, max=255)
        public static int MAX_CAVE_Y = 55;

        @Config.Comment("Whether or not to light up village chunks")
        public static boolean DO_LIGHT = true;

        @Config.Comment("Which block to use as a torch. Metadata not supported. Defaults to minecraft:torch if it can't find a valid block")
        public static String LIGHT_BLOCK = "minecraft:torch";

        @Config.Comment("Replace lava blocks around villages")
        public static boolean REMOVE_LAVA = true;

        @Config.Comment("Maximum average between the minimum height and the maximum height of blocks upon which village placement will bail. Set to 255 to disable.")
        public static int MAX_AVERAGE = 5;

        @Config.Ignore
        private static IBlockState LIGHT_REFERENCE = null;

        public static IBlockState getLightBlock () {
            if (LIGHT_REFERENCE == null) {
                String[] broken = LIGHT_BLOCK.split(":");
                if (broken.length == 2) {
                    Block block = Block.REGISTRY.getObject(new ResourceLocation(broken[0], broken[1]));
                    if (block != null) {
                        LIGHT_REFERENCE = block.getDefaultState();
                    }
                }
            }
            if (LIGHT_REFERENCE == null) {
                LIGHT_REFERENCE = Blocks.TORCH.getDefaultState();
            }

            return LIGHT_REFERENCE;
        }
    }
}
