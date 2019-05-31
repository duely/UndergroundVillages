package com.noobanidus.uv.core;

import net.minecraft.launchwrapper.IClassTransformer;

public interface IWarmTransformer extends IClassTransformer {
    boolean accepts(String name, String transformedName);
}
