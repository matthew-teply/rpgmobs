package com.conanthecivilian.rpgmobs.mixin.PlayerMixin;

import com.conanthecivilian.rpgmobs.entity.ModAttachments;
import com.conanthecivilian.rpgmobs.entity.conversation.AttachedConversationTopics;
import com.conanthecivilian.rpgmobs.entity.conversation.ConversationTopic;
import com.conanthecivilian.rpgmobs.entity.conversation.IConversationTopicsAccessor;
import com.conanthecivilian.rpgmobs.repository.ConversationRepository;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;

@Mixin(Player.class)
public abstract class PlayerMixin implements IConversationTopicsAccessor {
    public List<ResourceLocation> getDefaultConversationTopics() {
        return List.of(
            ResourceLocation.parse("rpgmobs:topic_background"),
            ResourceLocation.parse("rpgmobs:topic_chat"),
            ResourceLocation.parse("rpgmobs:topic_nearby_enemies")
            //ResourceLocation.parse("faction")
        );
    }

    public List<ConversationTopic> getConversationTopics() {
        Player player = (Player) (Object) this;

        return player
            .getData(ModAttachments.PLAYER_UNLOCKED_CONVERSATION_TOPICS.get())
            .topics()
            .stream()
            .map(ConversationRepository::getTopic)
            .toList();
    }

    public void unlockConversationTopics(ResourceLocation topicId) {
        Player player = (Player) (Object) this;

        List<ResourceLocation> unlockedTopics =
            new ArrayList<>(player.getData(ModAttachments.PLAYER_UNLOCKED_CONVERSATION_TOPICS.get()).topics());

        if (!unlockedTopics.contains(topicId)) {
            unlockedTopics.add(topicId);

            player.setData(ModAttachments.PLAYER_UNLOCKED_CONVERSATION_TOPICS.get(), new AttachedConversationTopics(unlockedTopics));
        }
    }
}
