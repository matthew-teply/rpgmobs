package com.conanthecivilian.rpgmobs.entity.trait;

import com.conanthecivilian.rpgmobs.manager.TraitManager.TraitScope;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

public record Trait(
    ResourceLocation id,
    String label,
    String category,
    List<ResourceLocation> conflicts,
    List<TraitScope> scope,
    Optional<TraitDisposition> disposition
) {
    public final static Codec<Trait> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ResourceLocation.CODEC.fieldOf("id").forGetter(Trait::id),
        Codec.STRING.fieldOf("label").forGetter(Trait::label),
        Codec.STRING.fieldOf("category").forGetter(Trait::category),
        ResourceLocation.CODEC.listOf().fieldOf("conflicts").forGetter(Trait::conflicts),
        TraitScope.CODEC.listOf().fieldOf("scope").forGetter(Trait::scope),
        TraitDisposition.CODEC.optionalFieldOf("disposition").forGetter(Trait::disposition)
    ).apply(instance, Trait::new));
}
