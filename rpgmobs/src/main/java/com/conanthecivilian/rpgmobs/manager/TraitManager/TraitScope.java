package com.conanthecivilian.rpgmobs.manager.TraitManager;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum TraitScope implements StringRepresentable {
    NPC("npc"),
    FACTION("faction"),
    TOPIC("topic"),
    DIALOGUE("dialogue");

    public static final Codec<TraitScope> CODEC = StringRepresentable.fromEnum(TraitScope::values);

    public final String name;

    TraitScope(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
}
