package com.conanthecivilian.rpgmobs.entity.npc.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.neoforge.attachment.IAttachmentHolder;

import java.util.Map;
import java.util.Optional;

public class NPCData {
    private Optional<String> name;
    private ResourceLocation race;
    private Optional<ResourceLocation> texture;
    private Map<String, Float> attributes;
    private Optional<Map<EquipmentSlot, ResourceLocation>> equipment;

    public NPCData(
        Optional<String> name,
        ResourceLocation race,
        Optional<ResourceLocation> texture,
        Map<String, Float> attributes,
        Optional<Map<EquipmentSlot, ResourceLocation>> equipment
    ) {
        this.name = name;
        this.race = race;
        this.texture = texture;
        this.attributes = attributes;
        this.equipment = equipment;
    }

    public NPCData(NPCData data) {
        this(
            data.name,
            data.race,
            data.texture,
            data.attributes,
            data.equipment
        );
    }

    public NPCData(IAttachmentHolder attachmentHolder) {
    }

    public static final Codec<NPCData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf("name").forGetter(NPCData::getName),
            ResourceLocation.CODEC.fieldOf("race").forGetter(NPCData::getRace),
            ResourceLocation.CODEC.optionalFieldOf("texture").forGetter(NPCData::getTexture),
            Codec.unboundedMap(Codec.STRING, Codec.FLOAT).fieldOf("attributes").forGetter(NPCData::getAttributes),
            Codec.unboundedMap(EquipmentSlot.CODEC, ResourceLocation.CODEC).optionalFieldOf("equipment").forGetter(NPCData::getEquipment)
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
}
