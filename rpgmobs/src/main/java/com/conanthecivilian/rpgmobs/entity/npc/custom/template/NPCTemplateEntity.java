package com.conanthecivilian.rpgmobs.entity.npc.custom.template;

import com.conanthecivilian.rpgmobs.entity.npc.custom.data.NPCDataEntity;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record NPCTemplateEntity(
    ResourceLocation id,
    NPCDataEntity data,
    NPCTemplateSpawnRulesEntity spawnRules
) {
    public static final Codec<NPCTemplateEntity> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ResourceLocation.CODEC.fieldOf("id").forGetter(NPCTemplateEntity::id),
        NPCDataEntity.CODEC.fieldOf("data").forGetter(NPCTemplateEntity::data),
        NPCTemplateSpawnRulesEntity.CODEC.fieldOf("spawn").forGetter(NPCTemplateEntity::spawnRules)
    ).apply(instance, NPCTemplateEntity::new));
}
