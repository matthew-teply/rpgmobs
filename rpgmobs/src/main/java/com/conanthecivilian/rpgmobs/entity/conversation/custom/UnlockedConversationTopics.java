package com.conanthecivilian.rpgmobs.entity.conversation.custom;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.attachment.IAttachmentHolder;

import java.util.List;

public record UnlockedConversationTopics(List<ResourceLocation> unlockedTopics) {
    public UnlockedConversationTopics(
        IAttachmentHolder iAttachmentHolder
    ) {
        this(((IConversationTopicsAccessor) iAttachmentHolder).getDefaultConversationTopics());
    }

    public static final Codec<UnlockedConversationTopics> CODEC = ResourceLocation.CODEC.listOf()
        .xmap(UnlockedConversationTopics::new, UnlockedConversationTopics::unlockedTopics);

    public static final StreamCodec<RegistryFriendlyByteBuf, UnlockedConversationTopics> STREAM_CODEC =
        ByteBufCodecs.fromCodecWithRegistries(CODEC);
}
