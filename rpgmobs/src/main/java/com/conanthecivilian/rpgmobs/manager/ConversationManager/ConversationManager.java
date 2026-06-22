package com.conanthecivilian.rpgmobs.manager.ConversationManager;

import com.conanthecivilian.rpgmobs.entity.custom.AbstractHumanlikeEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class ConversationManager {
    private final Player player;
    private final AbstractHumanlikeEntity<?> entity;

    public ConversationManager(Player player, AbstractHumanlikeEntity<?> entity) {
        this.player = player;
        this.entity = entity;
    }

    public ConversationTopic getTopic(String topicId) {
        // Getter
    }

    public ConversationDialogue getDialogue(String dialogueId) {
        // Getter
    }

    public ConversationDialogue getDialogueByTopicId(String topicId) {
        // 1. Get all dialogues with topic ID
        // 2. Check dialogue conditions
        // 3. Select the most appropriate dialogue
    }

    public List<ConversationTopic> getAvailableTopics() {
        // 1. Get player's topics
        // 2. Get entity's topics
        // 3. Cross-reference them
    }
}
