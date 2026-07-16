package com.conanthecivilian.rpgmobs.manager.ConversationManager;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.entity.conversation.ConversationDialogue;
import com.conanthecivilian.rpgmobs.entity.trait.ITraitHolder;
import com.conanthecivilian.rpgmobs.manager.ConversationManager.hydrator.DefaultConversationHydrator;
import com.conanthecivilian.rpgmobs.manager.ConversationManager.hydrator.NearbyEnemiesConversationHydrator;
import com.conanthecivilian.rpgmobs.repository.ConversationHydratorRepository;
import com.conanthecivilian.rpgmobs.repository.ConversationRepository;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConversationManager {
    public static void registerHydrators() {
        ConversationHydratorRepository.set(new DefaultConversationHydrator());
        ConversationHydratorRepository.set(new NearbyEnemiesConversationHydrator());
    }

    public static @Nullable ConversationDialogue determineDialogueByTraits(
        ResourceLocation topicId,
        ITraitHolder traitHolder,
        RandomSource random
    ) {
        if (traitHolder.getTraits().isEmpty()) {
            RPGMobs.LOGGER.info(traitHolder.toString());
            RPGMobs.LOGGER.info("Trait holder has no traits");
            return null;
        }

        List<ConversationDialogue> dialogues = ConversationRepository.getTopicDialogues(topicId);
        List<ConversationDialogue> traitlessDialogues = new ArrayList<>();

        ConversationDialogue mostViableDialogue = null;

        int mostTraitMatches = 0;

        for (ConversationDialogue dialogue : dialogues) {
            if (dialogue.getTraits().isEmpty()) {
                traitlessDialogues.add(dialogue);
                continue;
            }

            Set<ResourceLocation> lookupSet = new HashSet<>(traitHolder.getTraits());

            List<ResourceLocation> commonTraits = dialogue.getTraits()
                .stream()
                .filter(lookupSet::contains)
                .toList();

            if (commonTraits.size() > mostTraitMatches) {
                mostTraitMatches = commonTraits.size();
                mostViableDialogue = dialogue;
            }
        }

        if (mostViableDialogue == null && !traitlessDialogues.isEmpty()) {
            mostViableDialogue = traitlessDialogues.get(random.nextInt(traitlessDialogues.size()));
        }

        return mostViableDialogue;
    }
}
