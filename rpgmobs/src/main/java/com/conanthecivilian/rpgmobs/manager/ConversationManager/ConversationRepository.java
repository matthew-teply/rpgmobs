package com.conanthecivilian.rpgmobs.manager.ConversationManager;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.manager.ConversationManager.record.ConversationDialogue;
import com.conanthecivilian.rpgmobs.manager.ConversationManager.record.ConversationTopic;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConversationRepository {
    public static final String TEMPLATE_LOCATION_TOPICS = "conversation/topic";
    public static final String TEMPLATE_LOCATION_DIALOGUES = "conversation/dialogue";

    public static final HashMap<ResourceLocation, ConversationTopic> TOPICS = new HashMap<>();
    public static final HashMap<ResourceLocation, ConversationDialogue> DIALOGUES = new HashMap<>();

    public static ConversationTopic getTopic(ResourceLocation topicId) {
        //RPGMobs.LOGGER.info("Loading topic {}", topicId);

        ConversationTopic topic = TOPICS.get(topicId);

        if (topic == null) {
            RPGMobs.LOGGER.info("Topic \"{}\" is null", topicId);
            RPGMobs.LOGGER.info(TOPICS.keySet().toString());
        }

        return topic;
    }

    public static ConversationDialogue getDialogue(ResourceLocation dialogueId) {
        //RPGMobs.LOGGER.info("Loading dialogue {}", dialogueId);

        ConversationDialogue dialogue = DIALOGUES.get(dialogueId);

        if (dialogue == null) {
            RPGMobs.LOGGER.info("Dialogue \"{}\" is null", dialogueId);
            RPGMobs.LOGGER.info(DIALOGUES.keySet().toString());
        }

        return dialogue;
    }

    public static List<ConversationDialogue> getTopicDialogues(ResourceLocation topicId) {
        ConversationTopic conversationTopic = getTopic(topicId);
        List<ConversationDialogue> dialogues = new ArrayList<>();

        if (conversationTopic == null) {
            throw new Error("Conversation topic \"" + topicId + "\" not found");
        }

        conversationTopic.getDialogueIds().forEach(dialogueId -> {
            dialogues.add(getDialogue(dialogueId));
        });

        return dialogues;
    }
}
