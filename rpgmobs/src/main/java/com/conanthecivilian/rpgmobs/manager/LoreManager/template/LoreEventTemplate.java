package com.conanthecivilian.rpgmobs.manager.LoreManager.template;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public record LoreEventTemplate(
    LoreEventType type,
    int year,
    HashMap<String, ResourceLocation> parameters,
    String outcome
) {
}
