package com.conanthecivilian.rpgmobs.entity.faction.template;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

public record FactionTemplateRaceData(
    List<ResourceLocation> racePool,
    Optional<Integer> min,
    Optional<Integer> max
) {
    public static final Codec<FactionTemplateRaceData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ResourceLocation.CODEC.listOf().fieldOf("race_pool").forGetter(FactionTemplateRaceData::racePool),
        Codec.INT.optionalFieldOf("min").forGetter(FactionTemplateRaceData::min),
        Codec.INT.optionalFieldOf("max").forGetter(FactionTemplateRaceData::max)
    ).apply(instance, FactionTemplateRaceData::new));
}
