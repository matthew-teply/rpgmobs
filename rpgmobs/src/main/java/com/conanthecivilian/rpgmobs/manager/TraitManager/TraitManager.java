package com.conanthecivilian.rpgmobs.manager.TraitManager;

import com.conanthecivilian.rpgmobs.entity.trait.Trait;
import net.minecraft.util.RandomSource;

import javax.annotation.Nullable;
import java.util.List;

public class TraitManager {

    public static @Nullable Trait determineTraitByWeight(RandomSource random, List<Trait> traits) {
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

}
