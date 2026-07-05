package com.conanthecivilian.rpgmobs.entity;

import com.conanthecivilian.rpgmobs.entity.conversation.custom.UnlockedConversationTopics;
import com.conanthecivilian.rpgmobs.entity.npc.custom.data.NPCData;
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

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<UnlockedConversationTopics>> NPC_TOPICS =
        ATTACHMENT_TYPES.register("npc_topics", () -> AttachmentType.builder(UnlockedConversationTopics::new)
            .serialize(UnlockedConversationTopics.CODEC)
            .sync(UnlockedConversationTopics.STREAM_CODEC)
            .build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<NPCData>> NPC_DATA =
        ATTACHMENT_TYPES.register("npc_data", () -> AttachmentType.builder(NPCData::new)
            .serialize(NPCData.CODEC)
            .sync(NPCData.STREAM_CODEC)
            .build());

}
