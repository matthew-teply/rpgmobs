package com.conanthecivilian.rpgmobs.screen.custom.conversation;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.accessor.IConversationTopicsAccessor;
import com.conanthecivilian.rpgmobs.entity.custom.AbstractHumanlikeEntity;
import com.conanthecivilian.rpgmobs.manager.ConversationManager.record.ConversationDialogue;
import com.conanthecivilian.rpgmobs.manager.ConversationManager.record.ConversationTopic;
import com.lowdragmc.lowdraglib2.gui.sync.bindings.impl.SupplierDataSource;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.gui.ui.UI;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Button;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ProgressBar;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ScrollerView;
import com.lowdragmc.lowdraglib2.utils.XmlUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class ConversationUI {
    private final Player player;
    private final AbstractHumanlikeEntity<?> entity;

    private UI ui;

    private Button exitButton;
    private Label title;
    private Label content;
    private ProgressBar relationshipBar;
    private ScrollerView topicsContainer;

    public ConversationUI(Player player, AbstractHumanlikeEntity<?> entity) {
        this.player = player;
        this.entity = entity;

        var xml = XmlUtils.loadXml(ResourceLocation.parse("rpgmobs:ui/conversation-ui/conversation-ui.xml"));

        if (xml != null) {
            ui = UI.of(xml);

            exitButton = (Button) ui.select("#button-exit").findFirst().orElseThrow();
            title = (Label) ui.select("#title").findFirst().orElseThrow();
            content = (Label) ui.select("#content").findFirst().orElseThrow();
            relationshipBar = (ProgressBar) ui.select("#relationship-bar").findFirst().orElseThrow();
            topicsContainer = (ScrollerView) ui.select("#topics-container").findFirst().orElseThrow();

            registerTitleValues();
            registerCurrentDialogueText();
            registerContentValues(this.getDefaultDialogue());
            registerRelationshipBarValues();
            registerExitButtonEvent();
            renderTopicsContainer();
        }
    }

    public ModularUI createModularUI() {
        return ModularUI.of(ui, player);
    }

    private void renderTopicsContainer() {
        if (!(this.player instanceof IConversationTopicsAccessor conversationPlayer)) {
            return;
        }

        this.topicsContainer.onMessage("refresh", (self, message) -> {
            RPGMobs.LOGGER.info("Server told topics container to refresh");
            refreshTopics(conversationPlayer);
        });

        renderTopics(conversationPlayer);
    }

    private void renderTopics(IConversationTopicsAccessor conversationPlayer) {
        List<ConversationTopic> entityTopics = this.entity.getConversationTopics();
        List<ConversationTopic> playerTopics = conversationPlayer.getConversationTopics();

        entityTopics.forEach(entityTopic -> {
            Button topicButton = new Button();

            topicButton.setClasses("topic");
            topicButton.setId("topic-" + entityTopic.getId().toString());

            topicButton.setOnServerClick(event -> {
                this.entity.setCurrentDialogueText("Let me think...");

                ConversationDialogue randomDialogue = this.entity.selectRandomDialogue(entityTopic);

                this.entity.setCurrentDialogueText(randomDialogue.getParsedContent(this.player, this.entity));

                randomDialogue.callback(conversationPlayer);

                this.topicsContainer.sendMessage("refresh");
            });

            if (!playerTopics.contains(entityTopic)) {
                topicButton.setText("Locked topic");
                topicButton.setActive(false);
            } else {
                topicButton.setText(entityTopic.getName());
            }

            topicsContainer.addScrollViewChild(topicButton);
        });
    }

    private void refreshTopics(IConversationTopicsAccessor conversationPlayer) {
        List<ConversationTopic> playerTopics = conversationPlayer.getConversationTopics();

        playerTopics.forEach(playerTopic -> {
            Button topicButton = (Button) this.topicsContainer
                .selectId("topic-" + playerTopic.getId().toString())
                .findFirst()
                .orElseThrow();

            topicButton.setText(playerTopic.getName());
            topicButton.setActive(true);
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

    private void registerContentValues(String text) {
        content.setText(text);
    }

    private void registerCurrentDialogueText() {
        content.bindDataSource(SupplierDataSource.of(() -> Component.literal(entity.getCurrentDialogueText())));
    }

    private void registerRelationshipBarValues() {
        relationshipBar.setMinValue(0);
        relationshipBar.setMaxValue(100);

        relationshipBar.label.bindDataSource(SupplierDataSource.of(
            () -> Component.literal(String.valueOf(entity.getRelationship())).append("/100"))
        );
        relationshipBar.bindDataSource(SupplierDataSource.of(() -> (float) entity.getRelationship()));
    }

    private String getDefaultDialogue() {
        return (Component
            .literal("Hello ")
            .append(player.getName())
            .append(", my name is ")
            .append((entity.getCustomName() != null ? entity.getCustomName().getString() : "not important"))
            .append(".").getString());
    }
}
