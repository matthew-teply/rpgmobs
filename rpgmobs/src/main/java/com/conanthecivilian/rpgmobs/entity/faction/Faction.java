package com.conanthecivilian.rpgmobs.entity.faction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.UUID;

public class Faction {
    private UUID uuid;
    private ResourceLocation templateId;
    private String name;
    private int color;
    private List<ResourceLocation> traits;
    private List<ResourceLocation> races;
    private FactionLoreData loreData;
    private FactionDiplomacy diplomacy;

    public Faction(
        UUID uuid,
        ResourceLocation templateId,
        String name,
        int color,
        List<ResourceLocation> traits,
        List<ResourceLocation> races,
        FactionLoreData loreData,
        FactionDiplomacy diplomacy
    ) {
        this.uuid = uuid;
        this.templateId = templateId;
        this.name = name;
        this.color = color;
        this.traits = traits;
        this.races = races;
        this.loreData = loreData;
        this.diplomacy = diplomacy;
    }

    public static final Codec<Faction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        UUIDUtil.CODEC.fieldOf("uuid").forGetter(Faction::getUUID),
        ResourceLocation.CODEC.fieldOf("template_id").forGetter(Faction::getTemplateId),
        Codec.STRING.fieldOf("name").forGetter(Faction::getName),
        Codec.INT.fieldOf("color").forGetter(Faction::getColor),
        ResourceLocation.CODEC.listOf().fieldOf("traits").forGetter(Faction::getTraits),
        ResourceLocation.CODEC.listOf().fieldOf("races").forGetter(Faction::getRaces),
        FactionLoreData.CODEC.fieldOf("lore_data").forGetter(Faction::getLoreData),
        FactionDiplomacy.CODEC.fieldOf("diplomacy").forGetter(Faction::getDiplomacy)
    ).apply(instance, Faction::new));

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID id) {
        this.uuid = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public List<ResourceLocation> getTraits() {
        return traits;
    }

    public void setTraits(List<ResourceLocation> traits) {
        this.traits = traits;
    }

    public ResourceLocation getTemplateId() {
        return templateId;
    }

    public void setTemplateId(ResourceLocation templateId) {
        this.templateId = templateId;
    }

    public FactionLoreData getLoreData() {
        return loreData;
    }

    public void setLoreData(FactionLoreData loreData) {
        this.loreData = loreData;
    }

    public FactionDiplomacy getDiplomacy() {
        return diplomacy;
    }

    public void setDiplomacy(FactionDiplomacy diplomacy) {
        this.diplomacy = diplomacy;
    }

    public boolean isActive() {
        return this.loreData.yearDestroyed().isEmpty();
    }

    public List<ResourceLocation> getRaces() {
        return races;
    }

    public void setRaces(List<ResourceLocation> races) {
        this.races = races;
    }
}
