package com.conanthecivilian.rpgmobs.entity.faction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;

import java.util.List;
import java.util.UUID;

public record FactionDiplomacy(
    List<UUID> allies,
    List<UUID> neutral,
    List<UUID> enemies
) {
    public static final Codec<FactionDiplomacy> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        UUIDUtil.CODEC.listOf().fieldOf("allies").forGetter(FactionDiplomacy::allies),
        UUIDUtil.CODEC.listOf().fieldOf("neutral").forGetter(FactionDiplomacy::neutral),
        UUIDUtil.CODEC.listOf().fieldOf("enemies").forGetter(FactionDiplomacy::enemies)
    ).apply(instance, FactionDiplomacy::new));
}
