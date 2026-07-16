package com.conanthecivilian.rpgmobs.repository;

import com.conanthecivilian.rpgmobs.manager.ConversationManager.hydrator.DefaultConversationHydrator;
import com.conanthecivilian.rpgmobs.manager.ConversationManager.hydrator.IConversationHydrator;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class ConversationHydratorRepository {
    private static final HashMap<ResourceLocation, IConversationHydrator> HYDRATORS = new HashMap<>();

    public static void set(IConversationHydrator conversationHydrator) {
        HYDRATORS.put(conversationHydrator.getId(), conversationHydrator);
    }

    public static IConversationHydrator get(ResourceLocation conversationHydratorId) {
        return HYDRATORS.get(conversationHydratorId);
    }

    public static DefaultConversationHydrator getDefault() {
        return new DefaultConversationHydrator();
    }
}
