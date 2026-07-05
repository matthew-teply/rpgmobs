package com.conanthecivilian.rpgmobs.entity;

import com.conanthecivilian.rpgmobs.entity.npc.NPCRegistry;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

public class ModEntities {
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        NPCRegistry.registerRenderers(event);
    }

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        NPCRegistry.registerAttributes(event);
    }
}
