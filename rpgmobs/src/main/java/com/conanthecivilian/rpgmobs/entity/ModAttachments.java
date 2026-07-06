package com.conanthecivilian.rpgmobs.entity;

import com.conanthecivilian.rpgmobs.entity.conversation.custom.AttachedConversationTopicsEntity;
import com.conanthecivilian.rpgmobs.entity.npc.custom.data.NPCDataEntity;
import com.conanthecivilian.rpgmobs.entity.trait.custom.AttachedTraitsEntity;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
        DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, "rpgmobs");

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<AttachedConversationTopicsEntity>> PLAYER_UNLOCKED_CONVERSATION_TOPICS =
        ATTACHMENT_TYPES.register("player_topics", () -> AttachmentType.builder(AttachedConversationTopicsEntity::new)
            .serialize(AttachedConversationTopicsEntity.CODEC)
            .sync(AttachedConversationTopicsEntity.STREAM_CODEC)
            .copyOnDeath()
            .build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<AttachedConversationTopicsEntity>> NPC_TOPICS =
        ATTACHMENT_TYPES.register("npc_topics", () -> AttachmentType.builder(AttachedConversationTopicsEntity::new)
            .serialize(AttachedConversationTopicsEntity.CODEC)
            .sync(AttachedConversationTopicsEntity.STREAM_CODEC)
            .build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<NPCDataEntity>> NPC_DATA =
        ATTACHMENT_TYPES.register("npc_data", () -> AttachmentType.builder(NPCDataEntity::new)
            .serialize(NPCDataEntity.CODEC)
            .sync(NPCDataEntity.STREAM_CODEC)
            .build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<AttachedTraitsEntity>> NPC_TRAITS =
        ATTACHMENT_TYPES.register("npc_traits", () -> AttachmentType.builder(AttachedTraitsEntity::new)
            .serialize(AttachedTraitsEntity.CODEC)
            .build()
        );
}
