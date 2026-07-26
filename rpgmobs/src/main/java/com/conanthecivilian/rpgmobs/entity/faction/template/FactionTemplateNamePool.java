package com.conanthecivilian.rpgmobs.entity.faction.template;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record FactionTemplateNamePool(
    List<String> prefixes,
    List<String> suffixes
) {
    public static final Codec<FactionTemplateNamePool> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.listOf().fieldOf("prefixes").forGetter(FactionTemplateNamePool::prefixes),
        Codec.STRING.listOf().fieldOf("suffixes").forGetter(FactionTemplateNamePool::suffixes)
    ).apply(instance, FactionTemplateNamePool::new));
}
