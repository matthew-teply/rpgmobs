package com.conanthecivilian.rpgmobs.manager.LoreManager;

import com.conanthecivilian.rpgmobs.manager.FactionManager.FactionManager;
import com.conanthecivilian.rpgmobs.manager.LoreManager.generator.FactionLoreGenerator.FactionLoreGenerator;
import com.conanthecivilian.rpgmobs.repository.LoreRepository;
import net.minecraft.util.RandomSource;

public class LoreManager {
    public LoreRepository loreRepository;
    public FactionManager factionManager;

    /*

    ~ Lore concepts ~

    1. World mood

    World mood is affected by major events like war, plague, prosperity boom, etc.

    It affects what kind of factions spawn, what kind of events spawn, what the most common traits are.

     */

    private final FactionLoreGenerator factionLoreGenerator;

    public LoreManager(
        LoreRepository loreRepository,
        FactionManager factionManager
    ) {
        this.loreRepository = loreRepository;
        this.factionManager = factionManager;

        this.factionLoreGenerator = new FactionLoreGenerator(this);
    }

    public void generateInitialLore(
        int timespan,
        long seed
    ) {
        if (!loreRepository.getAll().isEmpty()) {
            return;
        }

        RandomSource random = RandomSource.create(seed);

        for (int year = 0; year < timespan; year++) {
            this.factionLoreGenerator.generateFactions(random, year);
        }
    }
}