package com.conanthecivilian.rpgmobs.entity.npc.template;

import com.conanthecivilian.rpgmobs.entity.npc.data.NPCData;
import com.conanthecivilian.rpgmobs.entity.trait.TraitPool;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.attachment.IAttachmentHolder;

import java.util.Map;
import java.util.Optional;

public record NPCTemplate(
    ResourceLocation id,
    String label,
    NPCData data,
    NPCTemplateSpawnRules spawnRules,
    Optional<Map<String, TraitPool>> traits
) {
    public static final Codec<NPCTemplate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ResourceLocation.CODEC.fieldOf("id").forGetter(NPCTemplate::id),
        Codec.STRING.fieldOf("label").forGetter(NPCTemplate::label),
        NPCData.CODEC.fieldOf("data").forGetter(NPCTemplate::data),
        NPCTemplateSpawnRules.CODEC.fieldOf("spawn").forGetter(NPCTemplate::spawnRules),
        Codec.unboundedMap(Codec.STRING, TraitPool.CODEC).optionalFieldOf("traits").forGetter(NPCTemplate::traits)
    ).apply(instance, NPCTemplate::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, NPCTemplate> STREAM_CODEC =
        ByteBufCodecs.fromCodecWithRegistries(CODEC);

    public NPCTemplate(IAttachmentHolder iAttachmentHolder) {
        this(null, null, null, null, Optional.empty());
    }
}
