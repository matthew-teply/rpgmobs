package com.conanthecivilian.rpgmobs.repository;

import com.conanthecivilian.rpgmobs.entity.npc.template.NPCTemplate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import java.util.HashMap;

public class NPCTemplateRepository {
    public static final String NPC_TEMPLATES_LOCATION = "npc";

    public static final HashMap<ResourceLocation, NPCTemplate> TEMPLATES = new HashMap<>();

    public static HashMap<ResourceLocation, NPCTemplate> getAll() {
        return TEMPLATES;
    }

    public static NPCTemplate get(ResourceLocation id) {
        return TEMPLATES.get(id);
    }

    public static NPCTemplate getRandom(RandomSource random) {
        int randomIndex = random.nextIntBetweenInclusive(0, TEMPLATES.size() - 1);
        ResourceLocation randomKey = (ResourceLocation) TEMPLATES.keySet().toArray()[randomIndex];

        return TEMPLATES.get(randomKey);
    }
}
