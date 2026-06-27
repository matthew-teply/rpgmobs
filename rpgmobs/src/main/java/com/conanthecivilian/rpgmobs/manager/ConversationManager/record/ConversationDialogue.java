package com.conanthecivilian.rpgmobs.manager.ConversationManager.record;

import com.conanthecivilian.rpgmobs.accessor.IConversationTopicsAccessor;
import com.conanthecivilian.rpgmobs.entity.custom.AbstractHumanlikeEntity;
import com.conanthecivilian.rpgmobs.manager.ConversationManager.DialogueParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class ConversationDialogue {
    private ResourceLocation id;
    private String content;
    private List<ResourceLocation> unlockedTopicIds;

    public static Codec<ConversationDialogue> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ResourceLocation.CODEC.fieldOf("id").forGetter(ConversationDialogue::getId),
        Codec.STRING.fieldOf("content").forGetter(ConversationDialogue::getContent),
        ResourceLocation.CODEC.listOf().fieldOf("unlocked_topic_ids").forGetter(ConversationDialogue::getUnlockedTopicIds)
    ).apply(instance, ConversationDialogue::new));

    public ConversationDialogue(
        ResourceLocation id,
        String content,
        List<ResourceLocation> unlockedTopicIds
    ) {
        this.id = id;
        this.content = content;
        this.unlockedTopicIds = unlockedTopicIds;
    }

    public void callback(IConversationTopicsAccessor conversationTopicsAccessor) {
        this.unlockConversationTopics(conversationTopicsAccessor);
    }

    private void unlockConversationTopics(IConversationTopicsAccessor conversationTopicsAccessor) {
        this.unlockedTopicIds.forEach(conversationTopicsAccessor::unlockConversationTopics);
    }

    public ResourceLocation getId() {
        return id;
    }

    public void setId(ResourceLocation id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public String getParsedContent(
        Player player,
        AbstractHumanlikeEntity<?> entity
    ) {
        return DialogueParser.parse(content, player, entity);
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ResourceLocation> getUnlockedTopicIds() {
        return unlockedTopicIds;
    }

    public void setUnlockedTopicIds(List<ResourceLocation> unlockedTopicIds) {
        this.unlockedTopicIds = unlockedTopicIds;
    }
}
