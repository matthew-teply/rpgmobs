package com.conanthecivilian.rpgmobs.entity.faction.template;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record FactionTemplateLoreData(
    int minYearsActive,
    int maxYearsActive,
    List<ResourceLocation> foundingMyths,
    List<ResourceLocation> destructionMyths
) {
    public static final Codec<FactionTemplateLoreData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("min_years_active").forGetter(FactionTemplateLoreData::minYearsActive),
        Codec.INT.fieldOf("max_years_active").forGetter(FactionTemplateLoreData::maxYearsActive),
        ResourceLocation.CODEC.listOf().fieldOf("founding_myths").forGetter(FactionTemplateLoreData::foundingMyths),
        ResourceLocation.CODEC.listOf().fieldOf("destruction_myths").forGetter(FactionTemplateLoreData::destructionMyths)
    ).apply(instance, FactionTemplateLoreData::new));
}
