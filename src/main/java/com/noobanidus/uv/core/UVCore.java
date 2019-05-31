package com.noobanidus.uv.core;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Predicate;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.SortingIndex(1001)
public class UVCore implements IFMLLoadingPlugin {
    public static String getAverageGroundLevel;
    public static String addComponentParts;
    public static String canSpawnStructureAtCoords;
    public static String getBiomeSpecificBlockState;


    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"com.noobanidus.uv.core.ClassTransformer"};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        boolean dev = !(Boolean) data.get("runtimeDeobfuscationEnabled");
        getAverageGroundLevel = dev ? "getAverageGroundLevel" : "func_74889_b";
        addComponentParts = dev ? "addComponentParts" : "func_74875_a";
        canSpawnStructureAtCoords = dev ? "canSpawnStructureAtCoords" : "func_75047_a";
        getBiomeSpecificBlockState = dev ? "getBiomeSpecificBlockState" : "func_175847_a";
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    public static MethodNode findMethod(ClassNode node, Predicate<MethodNode> finder) {
        for (MethodNode m : node.methods) {
            if (finder.test(m)) return m;
        }

        return null;
    }

    public static Predicate<MethodNode> averageGroundLevelFinder = methodNode -> methodNode.name.equals(UVCore.getAverageGroundLevel);
    public static Predicate<MethodNode> addComponentPartsFinder = methodNode -> methodNode.name.equals(UVCore.addComponentParts);
    public static Predicate<MethodNode> canSpawnStructureAtCoordsFinder = methodNode -> methodNode.name.equals(UVCore.canSpawnStructureAtCoords);
    public static Predicate<MethodNode> getBiomeSpecificBlockStateFinder = methodNode -> methodNode.name.equals(UVCore.getBiomeSpecificBlockState);

}
