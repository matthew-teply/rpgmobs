package com.conanthecivilian.rpgmobs.entity.npc.custom.template;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Optional;

public record NPCTemplate(
    ResourceLocation id,
    ResourceLocation race,
    Map<String, Float> attributes,
    NPCTemplateSpawnRules spawnRules,
    Optional<Map<String, ResourceLocation>> equipment
) {
    public static final Codec<NPCTemplate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ResourceLocation.CODEC.fieldOf("id").forGetter(NPCTemplate::id),
        ResourceLocation.CODEC.fieldOf("race").forGetter(NPCTemplate::race),
        Codec.unboundedMap(Codec.STRING, Codec.FLOAT).fieldOf("attributes").forGetter(NPCTemplate::attributes),
        NPCTemplateSpawnRules.CODEC.fieldOf("spawn").forGetter(NPCTemplate::spawnRules),
        Codec.unboundedMap(Codec.STRING, ResourceLocation.CODEC).optionalFieldOf("equipment").forGetter(NPCTemplate::equipment)
    ).apply(instance, NPCTemplate::new));
}
