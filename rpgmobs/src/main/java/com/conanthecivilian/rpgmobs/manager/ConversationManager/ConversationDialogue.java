package com.conanthecivilian.rpgmobs.manager.ConversationManager;

import java.util.function.Function;

public class ConversationDialogue {
    private String id;
    private String topicId;
    private String content;
    private Function<?, ?> callback;
}
