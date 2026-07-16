package com.conanthecivilian.rpgmobs.entity.trait;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record Trait(
    ResourceLocation id,
    String label,
    int weight,
    String category,
    List<String> type
) {
    public static Codec<Trait> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ResourceLocation.CODEC.fieldOf("id").forGetter(Trait::id),
        Codec.STRING.fieldOf("label").forGetter(Trait::label),
        Codec.INT.fieldOf("weight").forGetter(Trait::weight),
        Codec.STRING.fieldOf("category").forGetter(Trait::category),
        Codec.STRING.listOf().fieldOf("type").forGetter(Trait::type)
    ).apply(instance, Trait::new));
}
