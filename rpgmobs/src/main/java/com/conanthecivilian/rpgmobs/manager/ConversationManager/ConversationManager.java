package com.conanthecivilian.rpgmobs.manager.ConversationManager;

import com.conanthecivilian.rpgmobs.entity.npc.custom.AbstractNPCEntity;
import com.conanthecivilian.rpgmobs.manager.ConversationManager.hydrator.DefaultConversationHydrator;
import com.conanthecivilian.rpgmobs.manager.ConversationManager.hydrator.NearbyEnemiesConversationHydrator;
import com.conanthecivilian.rpgmobs.repository.ConversationHydratorRepository;

public class ConversationManager {
    AbstractNPCEntity<?> entity;

    public ConversationManager(AbstractNPCEntity<?> entity) {
        this.entity = entity;
    }

    public static void registerHydrators() {
        ConversationHydratorRepository.set(new DefaultConversationHydrator());
        ConversationHydratorRepository.set(new NearbyEnemiesConversationHydrator());
    }
}
