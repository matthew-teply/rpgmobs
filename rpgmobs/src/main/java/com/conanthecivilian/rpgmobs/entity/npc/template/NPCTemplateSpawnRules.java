package com.conanthecivilian.rpgmobs.entity.npc.template;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;

import java.util.Map;
import java.util.Optional;

public record NPCTemplateSpawnRules(
    float baseChance,
    int weight,
    Optional<Conditions> conditions,
    Optional<Multipliers> multipliers
) {
    public record Conditions(
        Optional<YRange> y
    ) {
        public record YRange(VerticalAnchor min, VerticalAnchor max) {
            public static final Codec<YRange> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                VerticalAnchor.CODEC.fieldOf("min").forGetter(YRange::min),
                VerticalAnchor.CODEC.fieldOf("max").forGetter(YRange::max)
            ).apply(instance, YRange::new));
        }

        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            YRange.CODEC.optionalFieldOf("y").forGetter(Conditions::y)
        ).apply(instance, Conditions::new));
    }

    public record Multipliers(
        Map<ResourceLocation, Float> structures
    ) {
        public static Codec<Multipliers> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(ResourceLocation.CODEC, Codec.FLOAT).fieldOf("structures").forGetter(Multipliers::structures)
        ).apply(instance, Multipliers::new));
    }

    public static Codec<NPCTemplateSpawnRules> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.FLOAT.fieldOf("base_chance").forGetter(NPCTemplateSpawnRules::baseChance),
        Codec.INT.fieldOf("weight").forGetter(NPCTemplateSpawnRules::weight),
        Conditions.CODEC.optionalFieldOf("conditions").forGetter(NPCTemplateSpawnRules::conditions),
        Multipliers.CODEC.optionalFieldOf("multipliers").forGetter(NPCTemplateSpawnRules::multipliers)
    ).apply(instance, NPCTemplateSpawnRules::new));
}
