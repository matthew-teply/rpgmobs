package com.conanthecivilian.rpgmobs.manager.ConversationManager;

import java.util.List;

public class ConversationTopic {
    private static final List<String> PLAYER_TOPICS = List.of(
        "rumors",
        "faction"
    );

    private static final List<String> ENTITY_TIPICS = List.of(
        "rumors",
        "faction",
        "capital"
    );

    private String id;
    private String label;
}
