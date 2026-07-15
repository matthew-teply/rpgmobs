package com.conanthecivilian.rpgmobs.screen.custom.conversation;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.entity.conversation.custom.ConversationDialogueEntity;
import com.conanthecivilian.rpgmobs.entity.conversation.custom.ConversationTopicEntity;
import com.conanthecivilian.rpgmobs.entity.conversation.custom.IConversationTopicsAccessor;
import com.conanthecivilian.rpgmobs.entity.npc.custom.AbstractNPCEntity;
import com.conanthecivilian.rpgmobs.manager.ConversationManager.ConversationManager;
import com.conanthecivilian.rpgmobs.repository.ConversationHydratorRepository;
import com.conanthecivilian.rpgmobs.repository.ConversationRepository;
import com.conanthecivilian.rpgmobs.screen.custom.ConversationMDParser;
import com.lowdragmc.lowdraglib2.gui.sync.bindings.impl.SupplierDataSource;
import com.lowdragmc.lowdraglib2.gui.sync.rpc.RPCEmitter;
import com.lowdragmc.lowdraglib2.gui.sync.rpc.RPCEventBuilder;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.gui.ui.UI;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Button;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ProgressBar;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ScrollerView;
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEventListener;
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvents;
import com.lowdragmc.lowdraglib2.utils.XmlUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class ConversationUI {
    private final Player player;
    private final AbstractNPCEntity<?> entity;

    private UI ui;

    private Button exitButton;
    private Label title;
    private ScrollerView contentScroller;
    private UIElement content;
    private UIElement contentTest;
    private ProgressBar relationshipBar;
    private ScrollerView topics;

    public RPCEmitter conversationRefreshEmitter;
    public RPCEmitter clientTopicBridgeEmitter;

    private UIEventListener initializeEventListener;

    public ConversationUI(Player player, AbstractNPCEntity<?> entity) {
        this.player = player;
        this.entity = entity;

        var xml = XmlUtils.loadXml(ResourceLocation.parse("rpgmobs:ui/conversation-ui/conversation-ui.xml"));

        if (xml != null) {
            ui = UI.of(xml);

            exitButton = (Button) ui.select("#button-exit").findFirst().orElseThrow();
            title = (Label) ui.select("#title").findFirst().orElseThrow();
            contentScroller = (ScrollerView) ui.select("#content-scroller").findFirst().orElseThrow();
            content = ui.select("#content").findFirst().orElseThrow();
            contentTest = ui.select("#content-test").findFirst().orElseThrow();
            relationshipBar = (ProgressBar) ui.select("#relationship-bar").findFirst().orElseThrow();
            topics = (ScrollerView) ui.select("#topics").findFirst().orElseThrow();

            registerDialogueEvents();
            initializeDialogue();

            registerTitleValues();
            registerRelationshipBarValues();
            renderTopics();
            registerExitButtonEvent();
        }
    }

    public ModularUI createModularUI() {
        return ModularUI.of(ui, player);
    }

    private void renderTopics() {
        if (!(this.player instanceof IConversationTopicsAccessor conversationPlayer)) {
            return;
        }

        this.topics.onMessage("refresh", (self, message) -> {
            RPGMobs.LOGGER.info("Server told topics container to refresh");
            refreshTopics(conversationPlayer);
        });

        renderTopics(conversationPlayer);
    }

    private void renderTopics(IConversationTopicsAccessor conversationPlayer) {
        List<ConversationTopicEntity> entityTopics = this.entity.getConversationTopics();
        List<ConversationTopicEntity> playerTopics = conversationPlayer.getConversationTopics();

        entityTopics.forEach(entityTopic -> {
            if (entityTopic == null) {
                return;
            }

            Button topicButton = new Button();

            topicButton.setClasses("topic");
            topicButton.setId("topic-" + entityTopic.getId().toString());

            topicButton.setOnServerClick(event -> this.selectTopic(entityTopic));

            if (!playerTopics.contains(entityTopic)) {
                topicButton.setDisplay(false);
            } else {
                topicButton.setText(entityTopic.getName());
            }

            topics.addScrollViewChild(topicButton);
        });
    }

    private void refreshTopics(IConversationTopicsAccessor conversationPlayer) {
        List<ConversationTopicEntity> playerTopics = conversationPlayer.getConversationTopics();

        playerTopics.forEach(playerTopic -> {
            Button topicButton = (Button) this.topics
                .selectId("topic-" + playerTopic.getId().toString())
                .findFirst()
                .orElseThrow();

            topicButton.setText(playerTopic.getName());
            topicButton.setDisplay(true);
        });
    }

    private void registerExitButtonEvent() {
        exitButton.setOnClick(event -> {
            player.closeContainer();
        });
    }

    private void registerTitleValues() {
        title.bindDataSource(SupplierDataSource.of(entity::getCustomName));
    }

    private void registerDialogueEvents() {
        this.conversationRefreshEmitter = content.addRPCEvent(RPCEventBuilder.simple(
            String[].class, String.class, String.class,
            (ids, hydratedQuestion, hydratedAnswer) -> {
                ConversationDialogueEntity currentConversationDialogue = ConversationRepository.getDialogue(
                    ResourceLocation.parse(ids[1])
                );

                if (currentConversationDialogue == null) {
                    RPGMobs.LOGGER.info("Dialogue \"{}\" not found", ids[1]);
                    return;
                }

                if (currentConversationDialogue.getTemplate().isPresent()) {
                    var xml = XmlUtils.loadXml(currentConversationDialogue.getTemplate().get());

                    if (xml != null) {
                        contentTest.clearAllChildren();

                        content.setDisplay(false);
                        contentTest.setDisplay(true);

                        contentTest.addChild(UI.of(xml).rootElement);
                    }
                } else {
                    contentTest.setDisplay(false);
                    content.setDisplay(true);

                    if (hydratedQuestion.isBlank()) {
                        this.addDialogueEntry(hydratedAnswer);
                    } else {
                        this.addDialogueEntry(hydratedQuestion, hydratedAnswer);
                    }
                }

                contentScroller.verticalScroller.setValue(99999999f);
            }
        ));

        this.clientTopicBridgeEmitter = content.addRPCEvent(RPCEventBuilder.simple(
            String.class,
            (topicIdString) -> {
                // This runs on server
                ResourceLocation topicId = ResourceLocation.parse(topicIdString);
                this.selectTopic(ConversationRepository.getTopic(topicId));
            }
        ));
    }

    private void addDialogueEntry(
        String question,
        String answer
    ) {
        UIElement entryContainer = new UIElement();

        Label entryQuestion = new Label();
        UIElement entryAnswer = new UIElement();

        entryContainer.setClasses("dialogue-entry");
        entryQuestion.setClasses("dialogue-entry-question", "font-standard");
        entryAnswer.setClasses("dialogue-entry-answer", "font-standard");

        entryQuestion.setText(question);

        ConversationMDParser.parse(
            answer,
            entryAnswer,
            (String topicId) -> this.clientTopicBridgeEmitter.send(topicId)
        );

        entryContainer.addChildren(entryQuestion, entryAnswer);

        this.content.addChild(entryContainer);
    }

    public void selectTopic(ConversationTopicEntity conversationTopic) {
        if (!(this.player instanceof IConversationTopicsAccessor conversationPlayer)) {
            RPGMobs.LOGGER.warn("Player is not an instance of IConversationTopicsAccessor");
            return;
        }

        if (!(player instanceof ServerPlayer)) {
            RPGMobs.LOGGER.warn("Player is not an instance of ServerPlayer");
            return;
        }

        RandomSource random = RandomSource.create();

        ConversationDialogueEntity dialogue = ConversationManager.determineDialogueByTraits(
            conversationTopic.getId(),
            this.entity,
            random
        );

        if (dialogue == null) {
            String[] ids = {};

            this.conversationRefreshEmitter.send(
                ids,
                conversationTopic.getName(),
                "Dialogue for topic \"" + conversationTopic.getId() + "\" not found!"
            );
            return;
        }

        dialogue.callback(conversationPlayer);

        this.topics.sendMessage("refresh");

        String hydratedQuestion = dialogue.getQuestion().isPresent()
            ? dialogue.getHydratedQuestion(this.player, this.entity)
            : ConversationHydratorRepository.getDefault().hydrate(
            this.player,
            this.entity,
            conversationTopic.getQuestion()
        );

        String hydratedAnswer = dialogue.getHydratedContent(this.player, this.entity);

        String[] ids = {conversationTopic.getId().toString(), dialogue.getId().toString()};

        this.conversationRefreshEmitter.send(
            ids,
            hydratedQuestion,
            hydratedAnswer
        );
    }

    private void addDialogueEntry(String answer) {
        UIElement entryContainer = new UIElement();

        UIElement entryAnswer = new UIElement();

        entryContainer.setClasses("dialogue-entry");
        entryAnswer.setClasses("dialogue-entry-answer", "font-standard");

        ConversationMDParser.parse(
            answer,
            entryAnswer,
            (String topicId) -> this.clientTopicBridgeEmitter.send(topicId)
        );

        entryContainer.addChild(entryAnswer);

        this.content.addChild(entryContainer);
    }

    private void initializeDialogue() {
        this.initializeEventListener = e -> {
            this.clientTopicBridgeEmitter.send("rpgmobs:greeting");

            this.content.removeEventListener(UIEvents.TICK, this.initializeEventListener);
        };

        // The ADDED event is broken, it does not fire. This is an ugly workaround.
        this.content.addEventListener(UIEvents.TICK, initializeEventListener);
    }

    private void registerRelationshipBarValues() {
        relationshipBar.setMinValue(0);
        relationshipBar.setMaxValue(100);

        relationshipBar.label.bindDataSource(SupplierDataSource.of(
            () -> Component.literal(String.valueOf(entity.getRelationship())).append("/100"))
        );
        relationshipBar.bindDataSource(SupplierDataSource.of(() -> (float) entity.getRelationship()));
    }
}
