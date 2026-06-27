package com.conanthecivilian.rpgmobs.mixin.PlayerMixin;

import com.conanthecivilian.rpgmobs.ModAttachments;
import com.conanthecivilian.rpgmobs.accessor.IConversationTopicsAccessor;
import com.conanthecivilian.rpgmobs.manager.ConversationManager.ConversationRepository;
import com.conanthecivilian.rpgmobs.manager.ConversationManager.record.ConversationTopic;
import com.conanthecivilian.rpgmobs.manager.ConversationManager.record.UnlockedConversationTopics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;

@Mixin(Player.class)
public abstract class PlayerMixin implements IConversationTopicsAccessor {
    public List<ResourceLocation> getDefaultConversationTopics() {
        return List.of(
            ResourceLocation.fromNamespaceAndPath("rpgmobs", "rumors")
            //ResourceLocation.parse("faction")
        );
    }

    public List<ConversationTopic> getConversationTopics() {
        Player player = (Player) (Object) this;

        return player
            .getData(ModAttachments.PLAYER_UNLOCKED_CONVERSATION_TOPICS.get())
            .unlockedTopics()
            .stream()
            .map(ConversationRepository::getTopic)
            .toList();
    }

    public void unlockConversationTopics(ResourceLocation topicId) {
        Player player = (Player) (Object) this;

        List<ResourceLocation> unlockedTopics =
            new ArrayList<>(player.getData(ModAttachments.PLAYER_UNLOCKED_CONVERSATION_TOPICS.get()).unlockedTopics());

        if (!unlockedTopics.contains(topicId)) {
            unlockedTopics.add(topicId);

            player.setData(ModAttachments.PLAYER_UNLOCKED_CONVERSATION_TOPICS.get(), new UnlockedConversationTopics(unlockedTopics));
        }
    }
}
