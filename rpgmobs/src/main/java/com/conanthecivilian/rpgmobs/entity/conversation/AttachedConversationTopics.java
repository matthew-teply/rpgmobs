package com.conanthecivilian.rpgmobs.entity.conversation;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.attachment.IAttachmentHolder;

import java.util.List;

public record AttachedConversationTopics(List<ResourceLocation> topics) {
    public AttachedConversationTopics(
        IAttachmentHolder iAttachmentHolder
    ) {
        this(((IConversationTopicsAccessor) iAttachmentHolder).getDefaultConversationTopics());
    }

    public static final Codec<AttachedConversationTopics> CODEC = ResourceLocation.CODEC.listOf()
        .xmap(AttachedConversationTopics::new, AttachedConversationTopics::topics);

    public static final StreamCodec<RegistryFriendlyByteBuf, AttachedConversationTopics> STREAM_CODEC =
        ByteBufCodecs.fromCodecWithRegistries(CODEC);
}
