package com.conanthecivilian.rpgmobs.data;

import com.conanthecivilian.rpgmobs.entity.conversation.custom.ConversationDialogueEntity;
import com.conanthecivilian.rpgmobs.entity.conversation.custom.ConversationTopicEntity;
import com.conanthecivilian.rpgmobs.entity.npc.custom.template.NPCTemplateEntity;
import com.conanthecivilian.rpgmobs.entity.trait.custom.TraitEntity;
import com.conanthecivilian.rpgmobs.repository.ConversationRepository;
import com.conanthecivilian.rpgmobs.repository.NPCTemplateRepository;
import com.conanthecivilian.rpgmobs.repository.TraitRepository;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

public class ModReloadListeners {
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new GenericReloadListener<>(
            ConversationTopicEntity.CODEC,
            ConversationRepository.TOPICS,
            ConversationRepository.TOPICS_TEMPLATES_LOCATION
        ));

        event.addListener(new GenericReloadListener<>(
            ConversationDialogueEntity.CODEC,
            ConversationRepository.DIALOGUES,
            ConversationRepository.DIALOGUES_TEMPLATES_LOCATION
        ));

        event.addListener(new GenericReloadListener<>(
            NPCTemplateEntity.CODEC,
            NPCTemplateRepository.TEMPLATES,
            NPCTemplateRepository.NPC_TEMPLATES_LOCATION
        ));

        event.addListener(new TraitReloadListener(
            TraitEntity.CODEC,
            TraitRepository.TRAIT_TEMPLATES_LOCATION
        ));
    }
}
