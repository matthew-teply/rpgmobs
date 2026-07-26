package com.conanthecivilian.rpgmobs.manager.FactionManager;

import com.conanthecivilian.rpgmobs.entity.faction.Faction;
import com.conanthecivilian.rpgmobs.entity.faction.FactionDiplomacy;
import com.conanthecivilian.rpgmobs.entity.faction.FactionLoreData;
import com.conanthecivilian.rpgmobs.entity.faction.template.FactionTemplate;
import com.conanthecivilian.rpgmobs.entity.trait.Trait;
import com.conanthecivilian.rpgmobs.manager.TraitManager.TraitManager;
import com.conanthecivilian.rpgmobs.repository.FactionRepository;
import com.conanthecivilian.rpgmobs.repository.TraitRepository;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FactionManager {
    private static final int DIPLOMACY_THRESHOLD_ALLY = 20;
    private static final int DIPLOMACY_THRESHOLD_ENEMY = -20;

    public final FactionRepository factionRepository;

    public FactionManager(FactionRepository factionRepository) {
        this.factionRepository = factionRepository;
    }

    public Faction createFaction(
        FactionTemplate template,
        int yearCreated
    ) {
        RandomSource random = RandomSource.create();

        List<Trait> templateTraits = new ArrayList<>();

        for (ResourceLocation traitId : template.getTraitPool()) {
            templateTraits.add(TraitRepository.get(traitId));
        }

        UUID factionId = UUID.randomUUID();

        List<Trait> factionTraits = TraitManager.getRandomTraits(random, templateTraits, 3);

        Faction faction = new Faction(
            factionId,
            template.getId(),
            template.getRandomName(),
            template.getRandomColor(),
            factionTraits,
            new FactionLoreData(yearCreated),
            createFactionDiplomacy(factionId, factionTraits)
        );

        this.factionRepository.set(faction);

        return faction;
    }

    public void destroyFaction(
        Faction faction,
        int yearDestroyed
    ) {
        this.factionRepository.setInactive(faction);

        faction.setLoreData(new FactionLoreData(
            faction.getLoreData().yearFounded(),
            Optional.of(yearDestroyed)
        ));
    }

    private FactionDiplomacy createFactionDiplomacy(UUID factionId, List<Trait> traits) {
        List<UUID> activeFactionIds = this.factionRepository.getActiveFactions();

        if (traits.isEmpty()) {
            return new FactionDiplomacy(List.of(), activeFactionIds, List.of());
        }

        List<UUID> allies = new ArrayList<>();
        List<UUID> neutral = new ArrayList<>();
        List<UUID> enemies = new ArrayList<>();

        for (UUID activeFactionId : activeFactionIds) {
            Faction activeFaction = this.factionRepository.getFaction(activeFactionId);

            int factionScore = 0;
            int activeFactionScore = 0;

            for (Trait factionTrait : traits) {
                for (Trait activeFactionTrait : activeFaction.getTraits()) {
                    factionScore += TraitManager.getTraitDispositionScore(factionTrait, activeFactionTrait);
                    activeFactionScore += TraitManager.getTraitDispositionScore(activeFactionTrait, activeFactionTrait);
                }
            }

            if (factionScore >= DIPLOMACY_THRESHOLD_ALLY || activeFactionScore >= DIPLOMACY_THRESHOLD_ALLY) {
                allies.add(activeFactionId);
                activeFaction.getDiplomacy().allies().add(factionId);
                continue;
            }

            if (factionScore <= DIPLOMACY_THRESHOLD_ENEMY || activeFactionScore <= DIPLOMACY_THRESHOLD_ENEMY) {
                enemies.add(activeFactionId);
                activeFaction.getDiplomacy().enemies().add(factionId);
                continue;
            }

            neutral.add(activeFactionId);
            activeFaction.getDiplomacy().neutral().add(factionId);
        }

        return new FactionDiplomacy(allies, neutral, enemies);
    }
}
