package com.conanthecivilian.rpgmobs.entity.utility;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class WeightedSelectionPool<T> {
    public static <T> Codec<WeightedSelectionPool<T>> codec(Codec<T> keyCodec) {
        return RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(keyCodec, Codec.INT).fieldOf("values").forGetter(WeightedSelectionPool::getValues),
            Codec.INT.optionalFieldOf("min").forGetter(WeightedSelectionPool::getMin),
            Codec.INT.optionalFieldOf("max").forGetter(WeightedSelectionPool::getMax)
        ).apply(instance, WeightedSelectionPool::new));
    }

    public final Map<T, Integer> values;
    public final Optional<Integer> min;
    public final Optional<Integer> max;

    public WeightedSelectionPool(Map<T, Integer> values, Optional<Integer> min, Optional<Integer> max) {
        this.values = values;
        this.min = min;
        this.max = max;
    }

    public Map<T, Integer> getValues() {
        return values;
    }

    public Optional<Integer> getMin() {
        return min;
    }

    public Optional<Integer> getMax() {
        return max;
    }

    public List<T> getWeightedValues(RandomSource random) {
        List<T> selectedValues = new ArrayList<>();
        List<T> availableValues = new ArrayList<>(this.values.keySet());

        if (this.min.isPresent() && this.min.get() > this.values.size()) {
            RPGMobs.LOGGER.error("Value pool does not satisfy minimum requested size");
            return List.of();
        }

        int numValues = random.nextIntBetweenInclusive(
            min.orElse(1),
            max.orElse(this.values.size())
        );

        while (numValues != selectedValues.size()) {
            T randomValue = this.determineRandomValueByWeight(random, availableValues);

            if (randomValue == null) {
                break;
            }

            availableValues.remove(randomValue);
            selectedValues.add(randomValue);
        }

        return selectedValues;
    }

    public @Nullable T determineRandomValueByWeight(RandomSource random) {
        if (this.values.isEmpty()) {
            return null;
        }

        int weightPool = this.values
            .keySet()
            .stream()
            .map(traitId -> this.values.getOrDefault(traitId, 0))
            .reduce(0, Integer::sum);

        if (weightPool <= 0) {
            return null;
        }

        int randomRoll = random.nextInt(weightPool);
        int weightPoolSubtotal = 0;

        for (T key : this.values.keySet()) {
            weightPoolSubtotal += this.values.get(key);

            if (weightPoolSubtotal > randomRoll) {
                return key;
            }
        }

        return null;
    }

    public @Nullable T determineRandomValueByWeight(RandomSource random, List<T> values) {
        if (values.isEmpty()) {
            return null;
        }

        int weightPool = values
            .stream()
            .map(key -> this.values.getOrDefault(key, 0))
            .reduce(0, Integer::sum);

        if (weightPool <= 0) {
            return null;
        }

        int randomRoll = random.nextInt(weightPool);
        int weightPoolSubtotal = 0;

        for (T value : values) {
            weightPoolSubtotal += this.values.get(value);

            if (weightPoolSubtotal > randomRoll) {
                return value;
            }
        }

        return null;
    }
}
