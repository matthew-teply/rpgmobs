package com.conanthecivilian.rpgmobs.manager.ConversationManager;

import com.conanthecivilian.rpgmobs.manager.ConversationManager.hydrator.DefaultConversationHydrator;
import com.conanthecivilian.rpgmobs.manager.ConversationManager.hydrator.NearbyEnemiesConversationHydrator;
import com.conanthecivilian.rpgmobs.manager.ConversationManager.repository.ConversationHydratorRepository;

public class ConversationManager {
    public static void registerHydrators() {
        ConversationHydratorRepository.register(new DefaultConversationHydrator());
        ConversationHydratorRepository.register(new NearbyEnemiesConversationHydrator());
    }
}
