package com.conanthecivilian.rpgmobs.entity.conversation.custom;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.attachment.IAttachmentHolder;

import java.util.List;

public record AttachedConversationTopicsEntity(List<ResourceLocation> topics) {
    public AttachedConversationTopicsEntity(
        IAttachmentHolder iAttachmentHolder
    ) {
        this(((IConversationTopicsAccessor) iAttachmentHolder).getDefaultConversationTopics());
    }

    public static final Codec<AttachedConversationTopicsEntity> CODEC = ResourceLocation.CODEC.listOf()
        .xmap(AttachedConversationTopicsEntity::new, AttachedConversationTopicsEntity::topics);

    public static final StreamCodec<RegistryFriendlyByteBuf, AttachedConversationTopicsEntity> STREAM_CODEC =
        ByteBufCodecs.fromCodecWithRegistries(CODEC);
}
