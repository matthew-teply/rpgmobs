package com.conanthecivilian.rpgmobs.manager.TraitManager;

import com.conanthecivilian.rpgmobs.entity.trait.custom.TraitEntity;
import net.minecraft.util.RandomSource;

import javax.annotation.Nullable;
import java.util.List;

public class TraitManager {

    public static @Nullable TraitEntity determineTraitByWeight(RandomSource random, List<TraitEntity> traits) {
        if (traits.isEmpty()) {
            return null;
        }

        int weightPool = traits
            .stream()
            .map(TraitEntity::weight)
            .reduce(0, Integer::sum);

        if (weightPool <= 0) {
            return null;
        }

        int randomRoll = random.nextInt(weightPool);
        int weightPoolSubtotal = 0;

        for (TraitEntity trait : traits) {
            weightPoolSubtotal += trait.weight();

            if (weightPoolSubtotal > randomRoll) {
                return trait;
            }
        }

        return null;
    }

}
