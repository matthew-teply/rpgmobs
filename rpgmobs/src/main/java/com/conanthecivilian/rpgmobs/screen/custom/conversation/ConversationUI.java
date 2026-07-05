package com.conanthecivilian.rpgmobs.screen.custom.conversation;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.entity.conversation.custom.ConversationDialogue;
import com.conanthecivilian.rpgmobs.entity.conversation.custom.ConversationTopic;
import com.conanthecivilian.rpgmobs.entity.conversation.custom.IConversationTopicsAccessor;
import com.conanthecivilian.rpgmobs.entity.npc.custom.AbstractNPCEntity;
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
import com.lowdragmc.lowdraglib2.utils.XmlUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Optional;

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

    private RPCEmitter conversationRefreshEmitter;
    private RPCEmitter clientTopicBridgeEmitter;

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

            registerTitleValues();
            registerDialogueEvents();
            registerRelationshipBarValues();
            renderTopics();
            registerExitButtonEvent();

            ResourceLocation greetingsTopicId = ResourceLocation.parse("rpgmobs:greeting");

            addDialogueEntry(
                ConversationHydratorRepository.getDefault().hydrate(
                    this.player,
                    this.entity,
                    ConversationRepository.getTopic(greetingsTopicId).getQuestion()
                ),
                ConversationRepository.getRandomTopicDialogue(
                    greetingsTopicId,
                    Optional.empty()
                ).getHydratedContent(this.player, this.entity)
            );
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
        List<ConversationTopic> entityTopics = this.entity.getConversationTopics();
        List<ConversationTopic> playerTopics = conversationPlayer.getConversationTopics();

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
        List<ConversationTopic> playerTopics = conversationPlayer.getConversationTopics();

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
                ResourceLocation topicId = ResourceLocation.parse(ids[0]);
                ResourceLocation dialogueId = ResourceLocation.parse(ids[1]);

                ConversationTopic currentConversationTopic = ConversationRepository.getTopic(
                    ResourceLocation.parse(ids[0])
                );
                ConversationDialogue currentConversationDialogue = ConversationRepository.getDialogue(
                    ResourceLocation.parse(ids[1])
                );

                if (currentConversationDialogue == null) {
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
                // THIS RUNS ON THE SERVER
                ResourceLocation topicId = ResourceLocation.parse(topicIdString);
                RPGMobs.LOGGER.info("Player clicked inline topic: " + topicId);

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

    public void selectTopic(ConversationTopic conversationTopic) {
        if (!(this.player instanceof IConversationTopicsAccessor conversationPlayer)) {
            RPGMobs.LOGGER.warn("Player is not an instance of IConversationTopicsAccessor");
            return;
        }

        ConversationDialogue randomDialogue = ConversationRepository.getRandomTopicDialogue(
            conversationTopic.getId(),
            Optional.empty()
        );

        randomDialogue.callback(conversationPlayer);

        this.topics.sendMessage("refresh");

        String hydratedQuestion = randomDialogue.getQuestion().isPresent()
            ? randomDialogue.getHydratedQuestion(this.player, this.entity)
            : ConversationHydratorRepository.getDefault().hydrate(
            this.player,
            this.entity,
            conversationTopic.getQuestion()
        );

        String hydratedAnswer = randomDialogue.getHydratedContent(this.player, this.entity);

        String[] ids = {conversationTopic.getId().toString(), randomDialogue.getId().toString()};

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

    private void registerRelationshipBarValues() {
        relationshipBar.setMinValue(0);
        relationshipBar.setMaxValue(100);

        relationshipBar.label.bindDataSource(SupplierDataSource.of(
            () -> Component.literal(String.valueOf(entity.getRelationship())).append("/100"))
        );
        relationshipBar.bindDataSource(SupplierDataSource.of(() -> (float) entity.getRelationship()));
    }
}
