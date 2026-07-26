package com.conanthecivilian.rpgmobs.manager.TraitManager;

import com.conanthecivilian.rpgmobs.entity.trait.Trait;
import com.conanthecivilian.rpgmobs.entity.trait.TraitDisposition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TraitManager {

    public static boolean areTraitsConflicting(Trait trait1, Trait trait2) {
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

    public static List<Trait> getRandomTraits(
        RandomSource random,
        List<Trait> traits,
        int numTraits
    ) {
        if (!isTraitListConflicting(traits) && traits.size() <= numTraits) {
            return traits;
        }

        List<Trait> selectedTraits = new ArrayList<>();

        Trait randomTrait = determineRandomTraitByWeight(random, traits);

        selectedTraits.add(randomTrait);
        traits.remove(randomTrait);

        traits.removeIf(trait -> {
            assert randomTrait != null;
            return areTraitsConflicting(randomTrait, trait);
        });

        return getRandomTraits(random, traits, numTraits, selectedTraits);
    }

    public static List<Trait> getRandomTraits(
        RandomSource random,
        List<Trait> traits,
        int numTraits,
        List<Trait> selectedTraits
    ) {
        if (numTraits == selectedTraits.size() || traits.isEmpty()) {
            return selectedTraits;
        }

        Trait randomTrait = determineRandomTraitByWeight(random, traits);

        selectedTraits.add(randomTrait);
        traits.remove(randomTrait);

        traits.removeIf(trait -> {
            assert randomTrait != null;
            return areTraitsConflicting(randomTrait, trait);
        });

        return getRandomTraits(random, traits, numTraits, selectedTraits);
    }

    public static @Nullable Trait determineRandomTraitByWeight(RandomSource random, List<Trait> traits) {
        if (traits.isEmpty()) {
            return null;
        }

        int weightPool = traits
            .stream()
            .map(Trait::weight)
            .reduce(0, Integer::sum);

        if (weightPool <= 0) {
            return null;
        }

        int randomRoll = random.nextInt(weightPool);
        int weightPoolSubtotal = 0;

        for (Trait trait : traits) {
            weightPoolSubtotal += trait.weight();

            if (weightPoolSubtotal > randomRoll) {
                return trait;
            }
        }

        return null;
    }

    /**
     * Get the disposition score of trait1 towards trait2
     */
    public static int getTraitDispositionScore(Trait trait1, Trait trait2) {
        if (trait1.disposition().isEmpty()) {
            return 0;
        }

        TraitDisposition traitDisposition = trait1.disposition().get();

        int dispositionScore = 0;

        if (trait1.id().equals(trait2.id())) {
            dispositionScore += traitDisposition.same();
        }

        if (areTraitsConflicting(trait1, trait2)) {
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
