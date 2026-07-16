package com.conanthecivilian.rpgmobs.entity.trait;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.attachment.IAttachmentHolder;

import java.util.ArrayList;
import java.util.List;

public record AttachedTraits(List<ResourceLocation> traitIds) {
    public AttachedTraits(IAttachmentHolder iAttachmentHolder) {
        this(new ArrayList<>());
    }

    public static final Codec<AttachedTraits> CODEC = ResourceLocation.CODEC.listOf()
        .xmap(AttachedTraits::new, AttachedTraits::traitIds);
}
