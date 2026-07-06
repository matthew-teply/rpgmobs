package com.conanthecivilian.rpgmobs.entity.trait.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record TraitEntity(
    ResourceLocation id,
    String label,
    int weight,
    String category,
    List<String> type
) {
    public static Codec<TraitEntity> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ResourceLocation.CODEC.fieldOf("id").forGetter(TraitEntity::id),
        Codec.STRING.fieldOf("label").forGetter(TraitEntity::label),
        Codec.INT.fieldOf("weight").forGetter(TraitEntity::weight),
        Codec.STRING.fieldOf("category").forGetter(TraitEntity::category),
        Codec.STRING.listOf().fieldOf("type").forGetter(TraitEntity::type)
    ).apply(instance, TraitEntity::new));
}
