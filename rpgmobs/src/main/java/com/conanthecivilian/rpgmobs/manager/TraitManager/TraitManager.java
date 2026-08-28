package com.conanthecivilian.rpgmobs.manager.TraitManager;

import com.conanthecivilian.rpgmobs.entity.trait.Trait;
import com.conanthecivilian.rpgmobs.entity.trait.TraitDisposition;
import com.conanthecivilian.rpgmobs.repository.TraitRepository;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TraitManager {

    public static boolean areTraitsConflicting(ResourceLocation trait1Id, ResourceLocation trait2Id) {
        Trait trait1 = TraitRepository.get(trait1Id);
        Trait trait2 = TraitRepository.get(trait2Id);

        if (trait1 == null || trait2 == null) {
            return false;
        }

        return trait1.conflicts().contains(trait2.id())
            || trait2.conflicts().contains(trait1.id());
    }

    public static boolean isTraitListConflicting(List<Trait> traits) {
        List<ResourceLocation> traitIds = new ArrayList<>();
        List<ResourceLocation> conflicts = new ArrayList<>();

        for (Trait trait : traits) {
            traitIds.add(trait.id());
            conflicts.addAll(trait.conflicts());
        }

        for (ResourceLocation traitId : traitIds) {
            if (conflicts.contains(traitId)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get the disposition score of trait1 towards trait2
     */
    public static int getTraitDispositionScore(ResourceLocation trait1Id, ResourceLocation trait2Id) {
        Trait trait1 = TraitRepository.get(trait1Id);
        Trait trait2 = TraitRepository.get(trait2Id);

        if (trait1 == null || trait2 == null) {
            return 0;
        }

        if (trait1.disposition().isEmpty()) {
            return 0;
        }

        TraitDisposition traitDisposition = trait1.disposition().get();

        int dispositionScore = 0;

        if (trait1.id().equals(trait2.id())) {
            dispositionScore += traitDisposition.same();
        }

        if (areTraitsConflicting(trait1.id(), trait2.id())) {
            dispositionScore += traitDisposition.conflicting();
        }

        if (traitDisposition.overrides().isPresent()) {
            Map<ResourceLocation, Integer> traitDispositionOverrides = traitDisposition.overrides().get();

            if (traitDispositionOverrides.containsKey(trait2.id())) {
                dispositionScore += traitDispositionOverrides.get(trait2.id());
            }
        }

        return dispositionScore;
    }
}
