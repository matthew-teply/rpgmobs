package com.conanthecivilian.rpgmobs.repository;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.entity.conversation.custom.ConversationDialogueEntity;
import com.conanthecivilian.rpgmobs.entity.conversation.custom.ConversationTopicEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class ConversationRepository {
    public static final String TOPICS_TEMPLATES_LOCATION = "conversation/topic";
    public static final String DIALOGUES_TEMPLATES_LOCATION = "conversation/dialogue";

    public static final HashMap<ResourceLocation, ConversationTopicEntity> TOPICS = new HashMap<>();
    public static final HashMap<ResourceLocation, ConversationDialogueEntity> DIALOGUES = new HashMap<>();

    public static ConversationTopicEntity getTopic(ResourceLocation topicId) {
        ConversationTopicEntity topic = TOPICS.get(topicId);

        if (topic == null) {
            RPGMobs.LOGGER.info("Topic \"{}\" is null", topicId);
            RPGMobs.LOGGER.info(TOPICS.keySet().toString());
        }

        return topic;
    }

    public static ConversationDialogueEntity getDialogue(ResourceLocation dialogueId) {
        //RPGMobs.LOGGER.info("Loading dialogue {}", dialogueId);

        ConversationDialogueEntity dialogue = DIALOGUES.get(dialogueId);

        if (dialogue == null) {
            RPGMobs.LOGGER.info("Dialogue \"{}\" is null", dialogueId);
            RPGMobs.LOGGER.info(DIALOGUES.keySet().toString());
        }

        return dialogue;
    }

    public static List<ConversationDialogueEntity> getTopicDialogues(ResourceLocation topicId) {
        ConversationTopicEntity conversationTopic = getTopic(topicId);
        List<ConversationDialogueEntity> dialogues = new ArrayList<>();

        if (conversationTopic == null) {
            throw new Error("Conversation topic \"" + topicId + "\" not found");
        }

        conversationTopic.getDialogueIds().forEach(dialogueId -> {
            dialogues.add(getDialogue(dialogueId));
        });

        return dialogues;
    }

    public static ConversationDialogueEntity getRandomTopicDialogue(
        ResourceLocation topicId,
        Optional<List<ResourceLocation>> excludedIds
    ) {
        List<ConversationDialogueEntity> dialogues = getTopicDialogues(topicId);

        excludedIds.ifPresent(resourceLocations -> IntStream.range(0, dialogues.size())
            .forEach(dialogueIndex -> {
                if (resourceLocations.contains(dialogues.get(dialogueIndex).getId())) {
                    dialogues.remove(dialogueIndex);
                }
            }));

        RandomSource randomSource = RandomSource.create();

        return dialogues.get(randomSource.nextIntBetweenInclusive(0, dialogues.size() - 1));
    }
}
