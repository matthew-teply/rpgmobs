package com.conanthecivilian.rpgmobs.manager.ConversationManager.record;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class ConversationTopic {
    private ResourceLocation id;
    private String name;
    private String question;
    private List<ResourceLocation> dialogueIds;

    public static Codec<ConversationTopic> CODEC = RecordCodecBuilder
        .create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("id").forGetter(ConversationTopic::getId),
                Codec.STRING.fieldOf("name").forGetter(ConversationTopic::getName),
                Codec.STRING.fieldOf("question").forGetter(ConversationTopic::getQuestion),
                ResourceLocation.CODEC.listOf().fieldOf("dialogue_ids").forGetter(ConversationTopic::getDialogueIds)
            ).apply(instance, ConversationTopic::new)
        );

    public ConversationTopic(
        ResourceLocation id,
        String name,
        String question,
        List<ResourceLocation> dialogueIds
    ) {
        this.id = id;
        this.name = name;
        this.question = question;
        this.dialogueIds = dialogueIds;
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
}
