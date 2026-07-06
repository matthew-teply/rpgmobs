package com.conanthecivilian.rpgmobs.entity.conversation.custom;

import com.conanthecivilian.rpgmobs.entity.trait.custom.ITraitHolder;
import com.conanthecivilian.rpgmobs.manager.TraitManager.TraitType;
import com.conanthecivilian.rpgmobs.repository.TraitDatabaseRepository;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

public class ConversationTopicEntity implements ITraitHolder {
    private ResourceLocation id;
    private String name;
    private String question;
    private List<ResourceLocation> dialogueIds;
    private final List<ResourceLocation> traits;

    public static Codec<ConversationTopicEntity> CODEC = RecordCodecBuilder
        .create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("id").forGetter(ConversationTopicEntity::getId),
                Codec.STRING.fieldOf("name").forGetter(ConversationTopicEntity::getName),
                Codec.STRING.fieldOf("question").forGetter(ConversationTopicEntity::getQuestion),
                ResourceLocation.CODEC.listOf().fieldOf("dialogue_ids").forGetter(ConversationTopicEntity::getDialogueIds),
                ResourceLocation.CODEC.listOf().optionalFieldOf("traits").forGetter(ConversationTopicEntity::codecGetTraits)
            ).apply(instance, ConversationTopicEntity::new)
        );

    public ConversationTopicEntity(
        ResourceLocation id,
        String name,
        String question,
        List<ResourceLocation> dialogueIds,
        Optional<List<ResourceLocation>> traits
    ) {
        this.id = id;
        this.name = name;
        this.question = question;
        this.dialogueIds = dialogueIds;
        this.traits = traits.orElse(List.of());
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
    public void registerTraits() {
        if (!this.traits.isEmpty()) {
            TraitDatabaseRepository.setAll(this.traits, TraitType.TOPIC, this.id);
        }
    }

    @Override
    public List<ResourceLocation> getTraits() {
        return this.traits;
    }

    private Optional<List<ResourceLocation>> codecGetTraits() {
        return Optional.of(this.traits);
    }
}
