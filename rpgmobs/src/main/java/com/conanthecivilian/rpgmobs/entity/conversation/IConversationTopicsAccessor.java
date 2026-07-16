package com.conanthecivilian.rpgmobs.entity.conversation;

import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface IConversationTopicsAccessor {
    List<ConversationTopic> getConversationTopics();

    List<ResourceLocation> getDefaultConversationTopics();

    void unlockConversationTopics(ResourceLocation topicId);
}
