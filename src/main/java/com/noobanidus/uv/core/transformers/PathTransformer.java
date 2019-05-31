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

public class PathTransformer implements IWarmTransformer {
    @Override
    public boolean accepts(String name, String transformedName) {
        List<String> classes = Collections.singletonList("net.minecraft.world.gen.structure.StructureVillagePieces$Path");
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

        MethodNode component = UVCore.findMethod(classNode, UVCore.addComponentPartsFinder);
        if (component != null) {
            component.instructions.clear();
            component.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
            component.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
            component.instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
            component.instructions.add(new VarInsnNode(Opcodes.ALOAD, 3));
            component.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/noobanidus/uv/core/hooks/PathHooks", "addComponentParts", "(Lnet/minecraft/world/gen/structure/StructureVillagePieces$Path;Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/world/gen/structure/StructureBoundingBox;)Z", false));
            component.instructions.add(new InsnNode(Opcodes.IRETURN));

            CustomClassWriter writer = new CustomClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            classNode.accept(writer);
            return writer.toByteArray();
        }

        return basicClass;
    }
}
