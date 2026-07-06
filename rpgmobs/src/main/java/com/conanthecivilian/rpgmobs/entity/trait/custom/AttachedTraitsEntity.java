package com.conanthecivilian.rpgmobs.entity.trait.custom;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.attachment.IAttachmentHolder;

import java.util.ArrayList;
import java.util.List;

public record AttachedTraitsEntity(List<ResourceLocation> traitIds) {
    public AttachedTraitsEntity(IAttachmentHolder iAttachmentHolder) {
        this(new ArrayList<>());
    }

    public static final Codec<AttachedTraitsEntity> CODEC = ResourceLocation.CODEC.listOf()
        .xmap(AttachedTraitsEntity::new, AttachedTraitsEntity::traitIds);
}
