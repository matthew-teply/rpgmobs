package com.conanthecivilian.rpgmobs.manager.LoreManager;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;

import java.util.UUID;

public record LoreEvent(
    UUID uuid,
    int year,
    String description
) {
    public LoreEvent(int year, String description) {
        this(UUID.randomUUID(), year, description);
    }

    public static final Codec<LoreEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        UUIDUtil.CODEC.fieldOf("uuid").forGetter(LoreEvent::uuid),
        Codec.INT.fieldOf("year").forGetter(LoreEvent::year),
        Codec.STRING.fieldOf("description").forGetter(LoreEvent::description)
    ).apply(instance, LoreEvent::new));
}
