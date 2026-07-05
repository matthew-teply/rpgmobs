package com.conanthecivilian.rpgmobs.data;

import com.conanthecivilian.rpgmobs.entity.conversation.custom.ConversationDialogue;
import com.conanthecivilian.rpgmobs.entity.conversation.custom.ConversationTopic;
import com.conanthecivilian.rpgmobs.entity.npc.custom.template.NPCTemplate;
import com.conanthecivilian.rpgmobs.repository.ConversationRepository;
import com.conanthecivilian.rpgmobs.repository.NPCTemplateRepository;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

public class ModReloadListeners {
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new GenericReloadListener<>(
            ConversationTopic.CODEC,
            ConversationRepository.TOPICS,
            ConversationRepository.TOPICS_TEMPLATES_LOCATION
        ));

        event.addListener(new GenericReloadListener<>(
            ConversationDialogue.CODEC,
            ConversationRepository.DIALOGUES,
            ConversationRepository.DIALOGUES_TEMPLATES_LOCATION
        ));

        event.addListener(new GenericReloadListener<>(
            NPCTemplate.CODEC,
            NPCTemplateRepository.TEMPLATES,
            NPCTemplateRepository.NPC_TEMPLATES_LOCATION
        ));
    }
}
