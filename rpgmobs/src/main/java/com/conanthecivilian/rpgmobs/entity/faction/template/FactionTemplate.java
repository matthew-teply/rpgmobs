package com.conanthecivilian.rpgmobs.entity.faction.template;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import java.util.List;

public class FactionTemplate {
    private ResourceLocation id;
    private List<ResourceLocation> traitPool;
    private List<Integer> colorPool;
    private FactionTemplateNamePool namePool;
    private FactionTemplateLoreData factionTemplateLoreData;

    public FactionTemplate(
        ResourceLocation id,
        List<ResourceLocation> traitPool,
        List<Integer> colorPool,
        FactionTemplateNamePool namePool,
        FactionTemplateLoreData factionTemplateLoreData
    ) {
        this.id = id;
        this.traitPool = traitPool;
        this.colorPool = colorPool;
        this.namePool = namePool;
        this.factionTemplateLoreData = factionTemplateLoreData;
    }

    public static final Codec<FactionTemplate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ResourceLocation.CODEC.fieldOf("id").forGetter(FactionTemplate::getId),
        ResourceLocation.CODEC.listOf().fieldOf("trait_pool").forGetter(FactionTemplate::getTraitPool),
        Codec.INT.listOf().fieldOf("color_pool").forGetter(FactionTemplate::getColorPool),
        FactionTemplateNamePool.CODEC.fieldOf("name_pool").forGetter(FactionTemplate::getNamePool),
        FactionTemplateLoreData.CODEC.fieldOf("lore_data").forGetter(FactionTemplate::getFactionLoreData)
    ).apply(instance, FactionTemplate::new));

    public int getRandomColor() {
        RandomSource random = RandomSource.create();

        return colorPool.get(random.nextInt(colorPool.size()));
    }

    public String getRandomName() {
        RandomSource random = RandomSource.create();

        List<String> prefixes = this.namePool.prefixes();
        List<String> suffixes = this.namePool.suffixes();

        String prefix = prefixes.get(random.nextInt(prefixes.size()));
        String suffix = suffixes.get(random.nextInt(suffixes.size()));

        return prefix + " " + suffix;
    }

    public ResourceLocation getId() {
        return id;
    }

    public void setId(ResourceLocation id) {
        this.id = id;
    }

    public List<ResourceLocation> getTraitPool() {
        return traitPool;
    }

    public void setTraitPool(List<ResourceLocation> traitPool) {
        this.traitPool = traitPool;
    }

    public List<Integer> getColorPool() {
        return colorPool;
    }

    public void setColorPool(List<Integer> colorPool) {
        this.colorPool = colorPool;
    }

    public FactionTemplateNamePool getNamePool() {
        return namePool;
    }

    public void setNamePool(FactionTemplateNamePool namePool) {
        this.namePool = namePool;
    }

    public FactionTemplateLoreData getFactionLoreData() {
        return factionTemplateLoreData;
    }

    public void setFactionLoreData(FactionTemplateLoreData factionTemplateLoreData) {
        this.factionTemplateLoreData = factionTemplateLoreData;
    }
}
