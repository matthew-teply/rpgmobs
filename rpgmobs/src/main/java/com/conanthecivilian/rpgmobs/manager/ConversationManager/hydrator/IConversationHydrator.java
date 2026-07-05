package com.conanthecivilian.rpgmobs.manager.ConversationManager.hydrator;

import com.conanthecivilian.rpgmobs.entity.npc.custom.AbstractNPCEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public interface IConversationHydrator {
    String hydrate(Player player, AbstractNPCEntity<?> entity, String rawText);

    ResourceLocation getId();
}
