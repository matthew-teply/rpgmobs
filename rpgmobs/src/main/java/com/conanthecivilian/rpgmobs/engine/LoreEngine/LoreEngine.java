package com.conanthecivilian.rpgmobs.engine.LoreEngine;

import com.conanthecivilian.rpgmobs.engine.LoreEngine.template.EventType;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.util.RandomSource;

public class LoreEngine {
    public void generateLore(long seed, int targetYears) {
        RandomSource random = RandomSource.create(seed);

        EventType[] eventTypes = {};
    }

    private static FileToIdConverter loadLoreJson(String pathInsideResources) {
        return FileToIdConverter.json("event_types");
    }
}