package com.conanthecivilian.rpgmobs.entity.trait;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Optional;

public record TraitDisposition(
    int same,
    int conflicting,
    Optional<Map<ResourceLocation, Integer>> overrides
) {
    public static final Codec<TraitDisposition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("same").forGetter(TraitDisposition::same),
        Codec.INT.fieldOf("conflicting").forGetter(TraitDisposition::conflicting),
        Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT).optionalFieldOf("overrides").forGetter(TraitDisposition::overrides)
    ).apply(instance, TraitDisposition::new));
}
