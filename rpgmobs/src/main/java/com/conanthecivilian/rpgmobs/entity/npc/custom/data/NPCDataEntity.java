package com.conanthecivilian.rpgmobs.entity.npc.custom.data;

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

public record NPCDataEntity(
    Optional<String> name,
    ResourceLocation race,
    Map<String, Float> attributes,
    Optional<Map<EquipmentSlot, ResourceLocation>> equipment
) {
    public NPCDataEntity(IAttachmentHolder attachmentHolder) {
        this(
            Optional.empty(),
            ResourceLocation.parse("rpgmobs:undefined"),
            Map.of(),
            Optional.empty()
        );
    }

    public NPCDataEntity(NPCDataEntity data) {
        this(
            data.name(),
            data.race(),
            data.attributes(),
            data.equipment()
        );
    }

    public static final Codec<NPCDataEntity> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf("name").forGetter(NPCDataEntity::name),
            ResourceLocation.CODEC.fieldOf("race").forGetter(NPCDataEntity::race),
            Codec.unboundedMap(Codec.STRING, Codec.FLOAT).fieldOf("attributes").forGetter(NPCDataEntity::attributes),
            Codec.unboundedMap(EquipmentSlot.CODEC, ResourceLocation.CODEC).optionalFieldOf("equipment").forGetter(NPCDataEntity::equipment)
        ).apply(instance, NPCDataEntity::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, NPCDataEntity> STREAM_CODEC =
        ByteBufCodecs.fromCodecWithRegistries(CODEC);
}
