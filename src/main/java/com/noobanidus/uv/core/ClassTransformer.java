package com.noobanidus.uv.core;

import com.noobanidus.uv.core.transformers.GroundTransformer;
import com.noobanidus.uv.core.transformers.PathTransformer;
import com.noobanidus.uv.core.transformers.SpawnTransformer;
import net.minecraft.launchwrapper.IClassTransformer;

import java.util.ArrayList;
import java.util.List;

public class ClassTransformer implements IClassTransformer {
    public List<IWarmTransformer> transformers = new ArrayList<>();

    public ClassTransformer() {
        //transformers.add(new GroundTransformer());
        //transformers.add(new PathTransformer());
        transformers.add(new SpawnTransformer());
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        for (IWarmTransformer t : transformers) {
            if (t.accepts(name, transformedName)) return t.transform(name, transformedName, basicClass);
        }
        return basicClass;
    }
}
