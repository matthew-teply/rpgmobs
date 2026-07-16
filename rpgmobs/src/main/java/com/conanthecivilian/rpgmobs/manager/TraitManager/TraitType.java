package com.conanthecivilian.rpgmobs.manager.TraitManager;

public enum TraitType {
    NPC("npc"),
    TOPIC("topic"),
    DIALOGUE("dialogue");

    public final String id;

    TraitType(String id) {
        this.id = id;
    }
}
