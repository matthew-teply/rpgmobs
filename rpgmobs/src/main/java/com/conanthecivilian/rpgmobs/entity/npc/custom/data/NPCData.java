package com.conanthecivilian.rpgmobs.entity.npc.custom.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.attachment.IAttachmentHolder;

public record NPCData(
    ResourceLocation npcTemplateId,
    String name
) {
    public NPCData(IAttachmentHolder attachmentHolder) {
        this(ResourceLocation.parse("rpgmobs:undefined"), "Undefined");
    }

    public static final Codec<NPCData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("npc_template_id").forGetter(NPCData::npcTemplateId),
            Codec.STRING.fieldOf("name").forGetter(NPCData::name)
        ).apply(instance, NPCData::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, NPCData> STREAM_CODEC =
        ByteBufCodecs.fromCodecWithRegistries(CODEC);
}
