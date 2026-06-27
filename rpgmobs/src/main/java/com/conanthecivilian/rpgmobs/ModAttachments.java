package com.conanthecivilian.rpgmobs;

import com.conanthecivilian.rpgmobs.manager.ConversationManager.record.UnlockedConversationTopics;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
        DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, "rpgmobs");

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<UnlockedConversationTopics>> PLAYER_UNLOCKED_CONVERSATION_TOPICS =
        ATTACHMENT_TYPES.register("player_topics", () -> AttachmentType.builder(UnlockedConversationTopics::new)
            .serialize(UnlockedConversationTopics.CODEC)
            .sync(UnlockedConversationTopics.STREAM_CODEC)
            .copyOnDeath()
            .build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<UnlockedConversationTopics>> ENTITY_UNLOCKED_CONVERSATION_TOPICS =
        ATTACHMENT_TYPES.register("entity_topics", () -> AttachmentType.builder(UnlockedConversationTopics::new)
            .serialize(UnlockedConversationTopics.CODEC)
            .sync(UnlockedConversationTopics.STREAM_CODEC)
            .build());
}
