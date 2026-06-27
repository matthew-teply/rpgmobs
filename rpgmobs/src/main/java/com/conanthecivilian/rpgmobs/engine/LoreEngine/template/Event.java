package com.conanthecivilian.rpgmobs.engine.LoreEngine.template;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public record Event(
    EventType type,
    int year,
    HashMap<String, ResourceLocation> parameters,
    String outcome
) {
    // public static final Codec<Event> CODEC = RecordCodecBuilder.create(
    //     instance -> instance.group(
    //
    //     )
    // )
}
