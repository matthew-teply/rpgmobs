package com.conanthecivilian.rpgmobs.manager.LoreManager.generator.FactionLoreGenerator;

import com.conanthecivilian.rpgmobs.entity.faction.Faction;
import com.conanthecivilian.rpgmobs.manager.LoreManager.LoreEvent;
import com.conanthecivilian.rpgmobs.manager.LoreManager.LoreManager;
import com.conanthecivilian.rpgmobs.repository.FactionTemplateRepository;
import net.minecraft.util.RandomSource;

import java.util.List;
import java.util.UUID;

public class FactionLoreGenerator {
    private final static int STARTING_FACTIONS_COUNT = 10;

    LoreManager loreManager;

    public FactionLoreGenerator(LoreManager loreManager) {
        this.loreManager = loreManager;
    }

    public void generateFactions(
        RandomSource random,
        int year
    ) {
        float factionCreationChance = 0.1F;
        float factionDestructionChance = 0.1F;

        List<UUID> activeFactions = this.loreManager.factionManager.factionRepository.getActiveFactions();

        if (random.nextFloat() < factionCreationChance) {
            Faction faction = this.loreManager.factionManager.createFaction(
                FactionTemplateRepository.getRandomFactionTemplate(random),
                year
            );

            this.loreManager.loreRepository.addEvent(new LoreEvent(year, "Faction " + faction.getName() + " was created."));
        }

        if (random.nextFloat() < factionDestructionChance && !activeFactions.isEmpty()) {
            int randomActiveFactionIndex = random.nextInt(activeFactions.size());

            Faction randomActiveFaction = this.loreManager.factionManager.factionRepository.getFaction(
                activeFactions.get(randomActiveFactionIndex)
            );

            this.loreManager.factionManager.destroyFaction(randomActiveFaction, year);

            this.loreManager.loreRepository.addEvent(new LoreEvent(year, "Faction " + randomActiveFaction.getName() + " was destroyed."));
        }
    }
}
