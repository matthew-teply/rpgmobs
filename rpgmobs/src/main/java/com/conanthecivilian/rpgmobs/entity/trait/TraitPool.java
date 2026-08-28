package com.conanthecivilian.rpgmobs.entity.trait;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.entity.utility.WeightedSelectionPool;
import com.conanthecivilian.rpgmobs.repository.TraitRepository;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TraitPool extends WeightedSelectionPool<ResourceLocation> {
    public static final Codec<TraitPool> CODEC = WeightedSelectionPool.codec(ResourceLocation.CODEC)
        .xmap(
            pool -> new TraitPool(pool.getValues(), pool.getMin(), pool.getMax()),
            traitPool -> new WeightedSelectionPool<>(traitPool.getValues(), traitPool.getMin(), traitPool.getMax())
        );

    public TraitPool(Map<ResourceLocation, Integer> values, Optional<Integer> min, Optional<Integer> max) {
        super(values, min, max);
    }

    @Override
    public List<ResourceLocation> getWeightedValues(RandomSource random) {
        List<ResourceLocation> selectedTraits = new ArrayList<>();

        ArrayList<ResourceLocation> availableTraits = new ArrayList<>(this.values.keySet());

        if (this.min.isPresent() && this.min.get() > this.values.size()) {
            RPGMobs.LOGGER.error("Trait pool does not satisfy minimum requested size");
            return List.of();
        }

        int numTraits = random.nextIntBetweenInclusive(
            min.orElse(1),
            max.orElse(this.values.size())
        );

        while (numTraits != selectedTraits.size() && !availableTraits.isEmpty()) {
            ResourceLocation randomTraitId = this.determineRandomValueByWeight(random, availableTraits);

            Trait randomTrait = TraitRepository.get(randomTraitId);

            if (randomTrait != null) {
                availableTraits.remove(randomTrait.id());
                availableTraits.removeAll(randomTrait.conflicts());

                selectedTraits.add(randomTraitId);
            }
        }

        return selectedTraits;
    }
}