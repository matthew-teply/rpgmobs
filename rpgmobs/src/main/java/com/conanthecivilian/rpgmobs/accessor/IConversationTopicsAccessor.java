package com.conanthecivilian.rpgmobs.accessor;

import com.conanthecivilian.rpgmobs.manager.ConversationManager.record.ConversationTopic;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface IConversationTopicsAccessor {
    List<ConversationTopic> getConversationTopics();

    List<ResourceLocation> getDefaultConversationTopics();

    void unlockConversationTopics(ResourceLocation topicId);
}
