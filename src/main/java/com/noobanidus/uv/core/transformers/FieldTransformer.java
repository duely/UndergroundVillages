package com.noobanidus.uv.core.transformers;

import com.noobanidus.uv.core.CustomClassWriter;
import com.noobanidus.uv.core.IWarmTransformer;
import com.noobanidus.uv.core.UVCore;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Collections;
import java.util.List;

public class FieldTransformer implements IWarmTransformer {
    @Override
    public boolean accepts(String name, String transformedName) {
        List<String> classes = Collections.singletonList("net.minecraft.world.gen.structure.StructureVillagePieces$Village");
        for (String clazz : classes) {
            if (clazz.equals(transformedName)) return true;
        }

        return false;
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);

        MethodNode average = UVCore.findMethod(classNode, UVCore.averageGroundLevelFinder);
        if (average != null) {
            average.instructions.clear();
            average.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
            average.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
            average.instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
            average.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/noobanidus/uv/core/hooks/GroundHooks", "getAverageGroundLevel", "(Lnet/minecraft/world/gen/structure/StructureVillagePieces$Village;Lnet/minecraft/world/World;Lnet/minecraft/world/gen/structure/StructureBoundingBox;)I", false));
            average.instructions.add(new InsnNode(Opcodes.IRETURN));
        }

        MethodNode biome = UVCore.findMethod(classNode, UVCore.getBiomeSpecificBlockStateFinder);
        if (biome != null) {
            biome.instructions.clear();
            biome.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
            biome.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
            biome.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/noobanidus/uv/core/hooks/GroundHooks", "getBiomeSpecificBlockState", "(Lnet/minecraft/world/gen/structure/StructureVillagePieces$Village;Lnet/minecraft/block/state/IBlockState;)Lnet/minecraft/block/state/IBlockState;", false));
            biome.instructions.add(new InsnNode(Opcodes.ARETURN));
        }

        CustomClassWriter writer = new CustomClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }
}
