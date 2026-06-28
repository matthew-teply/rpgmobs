package com.conanthecivilian.rpgmobs.manager.ConversationManager.repository;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.manager.ConversationManager.hydrator.DefaultConversationHydrator;
import com.conanthecivilian.rpgmobs.manager.ConversationManager.hydrator.IConversationHydrator;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class ConversationHydratorRepository {
    private static final HashMap<ResourceLocation, IConversationHydrator> HYDRATORS = new HashMap<>();

    public static void register(IConversationHydrator conversationHydrator) {
        HYDRATORS.put(conversationHydrator.getId(), conversationHydrator);
    }

    public static IConversationHydrator get(ResourceLocation conversationHydratorId) {
        RPGMobs.LOGGER.info(HYDRATORS.toString());

        return HYDRATORS.get(conversationHydratorId);
    }

    public static DefaultConversationHydrator getDefault() {
        return new DefaultConversationHydrator();
    }
}
