package com.noobanidus.uv.core.transformers;

import com.noobanidus.uv.core.CustomClassWriter;
import com.noobanidus.uv.core.IWarmTransformer;
import com.noobanidus.uv.core.UVCore;
import net.minecraft.world.gen.structure.MapGenVillage;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Collections;
import java.util.List;

public class SpawnTransformer implements IWarmTransformer {
    @Override
    public boolean accepts(String name, String transformedName) {
        List<String> classes = Collections.singletonList("net.minecraft.world.gen.structure.MapGenVillage");
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

        MethodNode structure = UVCore.findMethod(classNode, UVCore.canSpawnStructureAtCoordsFinder);
        if (structure != null) {
            structure.instructions.clear();
            structure.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
            structure.instructions.add(new VarInsnNode(Opcodes.ILOAD, 1));
            structure.instructions.add(new VarInsnNode(Opcodes.ILOAD, 2));
            structure.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/noobanidus/uv/core/hooks/SpawnHooks", "canSpawnStructureAtCoords", "(Lnet/minecraft/world/gen/structure/MapGenVillage;II)Z", false));
            structure.instructions.add(new InsnNode(Opcodes.IRETURN));

            CustomClassWriter writer = new CustomClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            classNode.accept(writer);
            return writer.toByteArray();
        }

        return basicClass;
    }
}
