package com.conanthecivilian.rpgmobs.manager.TagManager;

import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface ITagHolder {
    void registerTags();

    List<ResourceLocation> getEntityTags();
}
