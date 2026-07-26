package com.conanthecivilian.rpgmobs.entity.faction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public record FactionLoreData(
    Integer yearFounded,
    Optional<Integer> yearDestroyed
) {
    public FactionLoreData(Integer yearFounded) {
        this(yearFounded, Optional.empty());
    }

    public static final Codec<FactionLoreData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("year_founded").forGetter(FactionLoreData::yearFounded),
        Codec.INT.optionalFieldOf("year_destroyed").forGetter(FactionLoreData::yearDestroyed)
    ).apply(instance, FactionLoreData::new));
}
