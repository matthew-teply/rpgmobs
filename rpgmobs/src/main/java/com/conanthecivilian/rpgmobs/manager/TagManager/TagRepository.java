package com.conanthecivilian.rpgmobs.manager.TagManager;

import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TagRepository {
    private static final HashMap<ResourceLocation, HashMap<TagType, ArrayList<ResourceLocation>>> TAGS = new HashMap<>();

    public static HashMap<ResourceLocation, HashMap<TagType, ArrayList<ResourceLocation>>> getAll() {
        return TAGS;
    }

    public static HashMap<TagType, ArrayList<ResourceLocation>> get(ResourceLocation tag) {
        return TAGS.get(tag);
    }

    public static List<ResourceLocation> get(ResourceLocation tag, TagType tagType) {
        return TAGS.get(tag).get(tagType);
    }

    private static void set(ResourceLocation tag) {
        if (!TAGS.containsKey(tag)) {
            HashMap<TagType, ArrayList<ResourceLocation>> tagTypeMap = new HashMap<>();

            TAGS.put(tag, tagTypeMap);
        }
    }

    public static void set(ResourceLocation tag, TagType tagType, ResourceLocation id) {
        set(tag);

        if (!TAGS.get(tag).containsKey(tagType)) {
            TAGS.get(tag).put(tagType, new ArrayList<>());
        }

        if (!TAGS.get(tag).get(tagType).contains(id)) {
            TAGS.get(tag).get(tagType).add(id);
        }
    }

    public static void setAll(List<ResourceLocation> tags, TagType tagType, ResourceLocation id) {
        for (ResourceLocation tag : tags) {
            set(tag, tagType, id);
        }
    }
}
