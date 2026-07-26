package com.conanthecivilian.rpgmobs.entity.npc.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Optional;

public record NPCNamePool(
    Optional<List<String>> firstNames,
    Optional<List<String>> middleNames,
    Optional<List<String>> lastNames
) {
    public static final Codec<NPCNamePool> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.listOf().optionalFieldOf("first_names").forGetter(NPCNamePool::firstNames),
        Codec.STRING.listOf().optionalFieldOf("middle_names").forGetter(NPCNamePool::middleNames),
        Codec.STRING.listOf().optionalFieldOf("last_names").forGetter(NPCNamePool::lastNames)
    ).apply(instance, NPCNamePool::new));

}
