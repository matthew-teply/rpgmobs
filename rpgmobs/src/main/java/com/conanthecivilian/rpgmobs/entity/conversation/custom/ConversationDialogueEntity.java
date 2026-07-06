package com.conanthecivilian.rpgmobs.entity.conversation.custom;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.entity.npc.custom.AbstractNPCEntity;
import com.conanthecivilian.rpgmobs.entity.trait.custom.ITraitHolder;
import com.conanthecivilian.rpgmobs.manager.ConversationManager.hydrator.IConversationHydrator;
import com.conanthecivilian.rpgmobs.manager.TraitManager.TraitType;
import com.conanthecivilian.rpgmobs.repository.ConversationHydratorRepository;
import com.conanthecivilian.rpgmobs.repository.TraitDatabaseRepository;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class ConversationDialogueEntity implements ITraitHolder {
    private ResourceLocation id;
    private String content;
    private List<ResourceLocation> unlockedTopicIds;

    private final List<ResourceLocation> traits;

    private Optional<String> question;
    private Optional<List<ResourceLocation>> hydrators;
    private Optional<String> fallback;
    private Optional<ResourceLocation> template;

    public static Codec<ConversationDialogueEntity> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ResourceLocation.CODEC.fieldOf("id").forGetter(ConversationDialogueEntity::getId),
        Codec.STRING.fieldOf("content").forGetter(ConversationDialogueEntity::getContent),
        ResourceLocation.CODEC.listOf().fieldOf("unlocked_topic_ids").forGetter(ConversationDialogueEntity::getUnlockedTopicIds),

        ResourceLocation.CODEC.listOf().optionalFieldOf("traits").forGetter(ConversationDialogueEntity::codecGetTraits),
        Codec.STRING.optionalFieldOf("question").forGetter(ConversationDialogueEntity::getQuestion),
        ResourceLocation.CODEC.listOf().optionalFieldOf("hydrators").forGetter(ConversationDialogueEntity::getHydrators),
        Codec.STRING.optionalFieldOf("fallback").forGetter(ConversationDialogueEntity::getFallback),
        ResourceLocation.CODEC.optionalFieldOf("template").forGetter(ConversationDialogueEntity::getTemplate)
    ).apply(instance, ConversationDialogueEntity::new));

    public ConversationDialogueEntity(
        ResourceLocation id,
        String content,
        List<ResourceLocation> unlockedTopicIds,

        Optional<List<ResourceLocation>> traits,
        Optional<String> question,
        Optional<List<ResourceLocation>> hydrators,
        Optional<String> fallback,
        Optional<ResourceLocation> template
    ) {
        this.id = id;
        this.content = content;
        this.unlockedTopicIds = unlockedTopicIds;
        this.traits = traits.orElse(List.of());

        this.question = question;
        this.hydrators = hydrators;
        this.fallback = fallback;
        this.template = template;

        this.registerTraits();
    }

    public void callback(IConversationTopicsAccessor conversationTopicsAccessor) {
        this.unlockConversationTopics(conversationTopicsAccessor);
    }

    private void unlockConversationTopics(IConversationTopicsAccessor conversationTopicsAccessor) {
        this.unlockedTopicIds.forEach(conversationTopicsAccessor::unlockConversationTopics);
    }

    private String hydrate(
        Player player,
        AbstractNPCEntity<?> entity,
        String rawText
    ) {
        final AtomicReference<String> text = new AtomicReference<>(rawText);

        this.hydrators.ifPresent(hydratorIds -> hydratorIds.forEach(
            hydratorId -> {
                IConversationHydrator hydrator = ConversationHydratorRepository.get(hydratorId);

                if (hydrator != null) {
                    text.set(hydrator.hydrate(player, entity, text.get()));
                    return;
                }

                RPGMobs.LOGGER.warn("Conversation hydrator \"{}\" not found", hydratorId);
            }));

        text.set(ConversationHydratorRepository.getDefault().hydrate(player, entity, text.get()));

        return text.get();
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

    public String getHydratedContent(
        Player player,
        AbstractNPCEntity<?> entity
    ) {
        String hydratedContent = this.hydrate(player, entity, this.content);

        if (hydratedContent == null) {
            return this.fallback.orElse("I have nothing to say.");
        }

        return hydratedContent;
    }

    public String getHydratedQuestion(
        Player player,
        AbstractNPCEntity<?> entity
    ) {
        return this.question.map(s -> this.hydrate(player, entity, s)).orElse(null);
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

    public Optional<ResourceLocation> getTemplate() {
        return template;
    }

    public void setTemplate(Optional<ResourceLocation> template) {
        this.template = template;
    }

    public Optional<String> getQuestion() {
        return question;
    }

    public void setQuestion(Optional<String> question) {
        this.question = question;
    }

    public Optional<List<ResourceLocation>> getHydrators() {
        return hydrators;
    }

    public void setHydrators(Optional<List<ResourceLocation>> hydrators) {
        this.hydrators = hydrators;
    }

    public Optional<String> getFallback() {
        return fallback;
    }

    public void setFallback(Optional<String> fallback) {
        this.fallback = fallback;
    }

    @Override
    public void registerTraits() {
        if (!this.traits.isEmpty()) {
            TraitDatabaseRepository.setAll(this.traits, TraitType.DIALOGUE, this.id);
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
