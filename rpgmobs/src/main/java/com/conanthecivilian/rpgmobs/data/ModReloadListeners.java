package com.conanthecivilian.rpgmobs.data;

import com.conanthecivilian.rpgmobs.entity.conversation.ConversationDialogue;
import com.conanthecivilian.rpgmobs.entity.conversation.ConversationTopic;
import com.conanthecivilian.rpgmobs.entity.faction.template.FactionTemplate;
import com.conanthecivilian.rpgmobs.entity.npc.data.NPCRace;
import com.conanthecivilian.rpgmobs.entity.npc.template.NPCTemplate;
import com.conanthecivilian.rpgmobs.entity.trait.Trait;
import com.conanthecivilian.rpgmobs.repository.*;
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

        event.addListener(new TraitReloadListener(
            Trait.CODEC,
            TraitRepository.TRAIT_TEMPLATES_LOCATION
        ));

        event.addListener(new GenericReloadListener<>(
            NPCRace.CODEC,
            NPCRaceRepository.RACES,
            NPCRaceRepository.RACE_TEMPLATES_LOCATION
        ));

        event.addListener(new GenericReloadListener<>(
            FactionTemplate.CODEC,
            FactionTemplateRepository.FACTION_TEMPLATES,
            FactionTemplateRepository.FACTION_TEMPLATES_LOCATION
        ));
    }
}
