package com.conanthecivilian.rpgmobs.data;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.manager.ConversationManager.ConversationRepository;
import com.conanthecivilian.rpgmobs.manager.ConversationManager.record.ConversationDialogue;
import com.conanthecivilian.rpgmobs.manager.ConversationManager.record.ConversationTopic;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

@EventBusSubscriber(modid = RPGMobs.MODID, bus = EventBusSubscriber.Bus.GAME)
public class DataPackEvents {
    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new GenericReloadListener<>(
            ConversationTopic.CODEC,
            ConversationRepository.TOPICS,
            ConversationRepository.TEMPLATE_LOCATION_TOPICS
        ));

        event.addListener(new GenericReloadListener<>(
            ConversationDialogue.CODEC,
            ConversationRepository.DIALOGUES,
            ConversationRepository.TEMPLATE_LOCATION_DIALOGUES
        ));
    }
}
