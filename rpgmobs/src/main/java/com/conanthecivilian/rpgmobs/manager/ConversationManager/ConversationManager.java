package com.conanthecivilian.rpgmobs.manager.ConversationManager;

import com.conanthecivilian.rpgmobs.entity.custom.AbstractHumanlikeEntity;
import net.minecraft.world.entity.player.Player;

public class ConversationManager {
    private final Player player;
    private final AbstractHumanlikeEntity<?> entity;

    public ConversationManager(Player player, AbstractHumanlikeEntity<?> entity) {
        this.player = player;
        this.entity = entity;
    }
}
