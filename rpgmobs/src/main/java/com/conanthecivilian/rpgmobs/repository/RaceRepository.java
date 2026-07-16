package com.conanthecivilian.rpgmobs.repository;

import com.conanthecivilian.rpgmobs.entity.npc.data.NPCRace;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class RaceRepository {
    public static final String RACE_TEMPLATES_LOCATION = "race";

    public static final HashMap<ResourceLocation, NPCRace> RACES = new HashMap<>();

    public static NPCRace get(ResourceLocation id) {
        return RACES.get(id);
    }
}
