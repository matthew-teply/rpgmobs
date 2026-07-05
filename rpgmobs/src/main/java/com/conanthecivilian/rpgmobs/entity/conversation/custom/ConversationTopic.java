package com.conanthecivilian.rpgmobs.entity.conversation.custom;

import com.conanthecivilian.rpgmobs.manager.TagManager.ITagHolder;
import com.conanthecivilian.rpgmobs.manager.TagManager.TagRepository;
import com.conanthecivilian.rpgmobs.manager.TagManager.TagType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class ConversationTopic implements ITagHolder {
    private ResourceLocation id;
    private String name;
    private String question;
    private List<ResourceLocation> dialogueIds;
    private List<ResourceLocation> tags;

    public static Codec<ConversationTopic> CODEC = RecordCodecBuilder
        .create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("id").forGetter(ConversationTopic::getId),
                Codec.STRING.fieldOf("name").forGetter(ConversationTopic::getName),
                Codec.STRING.fieldOf("question").forGetter(ConversationTopic::getQuestion),
                ResourceLocation.CODEC.listOf().fieldOf("dialogue_ids").forGetter(ConversationTopic::getDialogueIds),
                ResourceLocation.CODEC.listOf().fieldOf("tags").forGetter(ConversationTopic::getEntityTags)
            ).apply(instance, ConversationTopic::new)
        );

    public ConversationTopic(
        ResourceLocation id,
        String name,
        String question,
        List<ResourceLocation> dialogueIds,
        List<ResourceLocation> tags
    ) {
        this.id = id;
        this.name = name;
        this.question = question;
        this.dialogueIds = dialogueIds;
        this.tags = tags;
    }

    public ResourceLocation getId() {
        return id;
    }

    public void setId(ResourceLocation id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ResourceLocation> getDialogueIds() {
        return dialogueIds;
    }

    public void setDialogueIds(List<ResourceLocation> dialogueIds) {
        this.dialogueIds = dialogueIds;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public void registerTags() {
        TagRepository.setAll(this.tags, TagType.TOPIC, this.id);
    }

    @Override
    public List<ResourceLocation> getEntityTags() {
        return this.tags;
    }
}
