package com.conanthecivilian.rpgmobs.manager.ConversationManager.hydrator;

import com.conanthecivilian.rpgmobs.entity.custom.AbstractHumanlikeEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public interface IConversationHydrator {
    String hydrate(Player player, AbstractHumanlikeEntity<?> entity, String rawText);

    ResourceLocation getId();
}
