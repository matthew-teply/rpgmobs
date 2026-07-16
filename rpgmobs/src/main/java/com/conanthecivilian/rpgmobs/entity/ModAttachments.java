package com.conanthecivilian.rpgmobs.entity;

import com.conanthecivilian.rpgmobs.entity.conversation.AttachedConversationTopics;
import com.conanthecivilian.rpgmobs.entity.npc.data.NPCData;
import com.conanthecivilian.rpgmobs.entity.npc.template.NPCTemplate;
import com.conanthecivilian.rpgmobs.entity.trait.AttachedTraits;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
        DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, "rpgmobs");

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<AttachedConversationTopics>> PLAYER_UNLOCKED_CONVERSATION_TOPICS =
        ATTACHMENT_TYPES.register("player_topics", () -> AttachmentType.builder(AttachedConversationTopics::new)
            .serialize(AttachedConversationTopics.CODEC)
            .sync(AttachedConversationTopics.STREAM_CODEC)
            .copyOnDeath()
            .build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<AttachedConversationTopics>> NPC_TOPICS =
        ATTACHMENT_TYPES.register("npc_topics", () -> AttachmentType.builder(AttachedConversationTopics::new)
            .serialize(AttachedConversationTopics.CODEC)
            .sync(AttachedConversationTopics.STREAM_CODEC)
            .build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<NPCTemplate>> NPC_TEMPLATE =
        ATTACHMENT_TYPES.register("npc_template", () -> AttachmentType.builder(NPCTemplate::new)
            .serialize(NPCTemplate.CODEC)
            .sync(NPCTemplate.STREAM_CODEC)
            .build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<NPCData>> NPC_DATA =
        ATTACHMENT_TYPES.register("npc_data", () -> AttachmentType.builder(NPCData::new)
            .serialize(NPCData.CODEC)
            .sync(NPCData.STREAM_CODEC)
            .build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<AttachedTraits>> NPC_TRAITS =
        ATTACHMENT_TYPES.register("npc_traits", () -> AttachmentType.builder(AttachedTraits::new)
            .serialize(AttachedTraits.CODEC)
            .build()
        );
}
