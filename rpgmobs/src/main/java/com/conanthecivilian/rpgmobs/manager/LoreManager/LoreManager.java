package com.conanthecivilian.rpgmobs.manager.LoreManager;

import com.conanthecivilian.rpgmobs.entity.faction.Faction;
import com.conanthecivilian.rpgmobs.manager.FactionManager.FactionManager;
import com.conanthecivilian.rpgmobs.repository.FactionTemplateRepository;
import com.conanthecivilian.rpgmobs.repository.LoreRepository;
import net.minecraft.util.RandomSource;

import java.util.List;
import java.util.UUID;

public class LoreManager {
    public LoreRepository loreRepository;
    public FactionManager factionManager;

    public LoreManager(
        LoreRepository loreRepository,
        FactionManager factionManager
    ) {
        this.loreRepository = loreRepository;
        this.factionManager = factionManager;
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
            this.generateFactionsLore(random, year);
        }
    }

    private void generateFactionsLore(
        RandomSource random,
        int year
    ) {
        float factionCreationChance = 0.1F;
        float factionDestructionChance = 0.1F;

        List<UUID> activeFactions = this.factionManager.factionRepository.getActiveFactions();

        if (random.nextFloat() < factionCreationChance) {
            Faction faction = this.factionManager.createFaction(
                FactionTemplateRepository.getRandomFactionTemplate(random),
                year
            );

            this.loreRepository.addEvent(new LoreEvent(year, "Faction " + faction.getName() + " was created."));
        }

        if (random.nextFloat() < factionDestructionChance && !activeFactions.isEmpty()) {
            int randomActiveFactionIndex = random.nextInt(activeFactions.size());

            Faction randomActiveFaction = this.factionManager.factionRepository.getFaction(
                activeFactions.get(randomActiveFactionIndex)
            );

            this.factionManager.destroyFaction(randomActiveFaction, year);

            this.loreRepository.addEvent(new LoreEvent(year, "Faction " + randomActiveFaction.getName() + " was destroyed."));
        }
    }
}