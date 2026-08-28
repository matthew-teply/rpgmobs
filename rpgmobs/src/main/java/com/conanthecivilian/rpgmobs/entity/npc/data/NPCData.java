package com.conanthecivilian.rpgmobs.entity.npc.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.neoforge.attachment.IAttachmentHolder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NPCData {
    public Optional<String> name;
    public Optional<String> gender;
    public ResourceLocation race;
    public Optional<ResourceLocation> texture;
    public Map<String, Float> attributes;
    public Optional<Map<EquipmentSlot, ResourceLocation>> equipment;
    public Optional<List<ResourceLocation>> traits;

    public NPCData(
        Optional<String> name,
        Optional<String> gender,
        ResourceLocation race,
        Optional<ResourceLocation> texture,
        Map<String, Float> attributes,
        Optional<Map<EquipmentSlot, ResourceLocation>> equipment,
        Optional<List<ResourceLocation>> traits
    ) {
        this.name = name;
        this.gender = gender;
        this.race = race;
        this.texture = texture;
        this.attributes = attributes;
        this.equipment = equipment;
        this.traits = traits;
    }

    public NPCData(NPCData data) {
        this(
            data.name,
            data.gender,
            data.race,
            data.texture,
            data.attributes,
            data.equipment,
            data.traits
        );
    }

    public NPCData(IAttachmentHolder attachmentHolder) {
    }

    public static final Codec<NPCData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf("name").forGetter(NPCData::getName),
            Codec.STRING.optionalFieldOf("gender").forGetter(NPCData::getGender),
            ResourceLocation.CODEC.fieldOf("race").forGetter(NPCData::getRace),
            ResourceLocation.CODEC.optionalFieldOf("texture").forGetter(NPCData::getTexture),
            Codec.unboundedMap(Codec.STRING, Codec.FLOAT).fieldOf("attributes").forGetter(NPCData::getAttributes),
            Codec.unboundedMap(EquipmentSlot.CODEC, ResourceLocation.CODEC).optionalFieldOf("equipment").forGetter(NPCData::getEquipment),
            ResourceLocation.CODEC.listOf().optionalFieldOf("traits").forGetter(NPCData::getTraits)
        ).apply(instance, NPCData::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, NPCData> STREAM_CODEC =
        ByteBufCodecs.fromCodecWithRegistries(CODEC);

    public Optional<String> getName() {
        return name;
    }

    public void setName(Optional<String> name) {
        this.name = name;
    }

    public Optional<String> getGender() {
        return gender;
    }

    public void setGender(Optional<String> gender) {
        this.gender = gender;
    }

    public ResourceLocation getRace() {
        return race;
    }

    public void setRace(ResourceLocation race) {
        this.race = race;
    }

    public Optional<ResourceLocation> getTexture() {
        return texture;
    }

    public void setTexture(Optional<ResourceLocation> texture) {
        this.texture = texture;
    }

    public Map<String, Float> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Float> attributes) {
        this.attributes = attributes;
    }

    public Optional<Map<EquipmentSlot, ResourceLocation>> getEquipment() {
        return equipment;
    }

    public void setEquipment(Optional<Map<EquipmentSlot, ResourceLocation>> equipment) {
        this.equipment = equipment;
    }

    public Optional<List<ResourceLocation>> getTraits() {
        return traits;
    }

    public void setTraits(Optional<List<ResourceLocation>> traits) {
        this.traits = traits;
    }
}
