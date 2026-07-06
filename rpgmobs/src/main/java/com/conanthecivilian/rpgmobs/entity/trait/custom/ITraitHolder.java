package com.conanthecivilian.rpgmobs.entity.trait.custom;

import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface ITraitHolder {
    void registerTraits();

    List<ResourceLocation> getTraits();
}
