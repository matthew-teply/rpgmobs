package com.conanthecivilian.rpgmobs.repository;

import com.conanthecivilian.rpgmobs.entity.faction.template.FactionTemplate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import java.util.HashMap;

public class FactionTemplateRepository {
    public static final String FACTION_TEMPLATES_LOCATION = "faction";

    public static final HashMap<ResourceLocation, FactionTemplate> FACTION_TEMPLATES = new HashMap<>();

    public static FactionTemplate get(ResourceLocation id) {
        return FACTION_TEMPLATES.get(id);
    }

    public static FactionTemplate getRandomFactionTemplate(RandomSource random) {
        return FACTION_TEMPLATES
            .values()
            .stream()
            .toList()
            .get(random.nextInt(FACTION_TEMPLATES.size()));
    }
}
