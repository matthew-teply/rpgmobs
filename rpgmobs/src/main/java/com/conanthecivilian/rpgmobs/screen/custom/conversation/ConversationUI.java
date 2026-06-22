package com.conanthecivilian.rpgmobs.screen.custom.conversation;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.entity.custom.AbstractHumanlikeEntity;
import com.lowdragmc.lowdraglib2.gui.sync.bindings.impl.SupplierDataSource;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.gui.ui.UI;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Button;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ProgressBar;
import com.lowdragmc.lowdraglib2.utils.XmlUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class ConversationUI {
    private final Player player;
    private final AbstractHumanlikeEntity<?> entity;

    private UI ui;

    private UIElement exitButtonElement;
    private UIElement titleElement;
    private UIElement contentElement;
    private UIElement relationShipBarElement;
    private List<UIElement> topicButtonElements;

    public ConversationUI(Player player, AbstractHumanlikeEntity<?> entity) {
        this.player = player;
        this.entity = entity;

        var xml = XmlUtils.loadXml(ResourceLocation.parse("rpgmobs:ui/conversation-ui/conversation-ui.xml"));

        if (xml != null) {
            ui = UI.of(xml);

            exitButtonElement = ui.select("#button-exit").findFirst().orElseThrow();
            titleElement = ui.select("#title").findFirst().orElseThrow();
            contentElement = ui.select("#content").findFirst().orElseThrow();
            relationShipBarElement = ui.select("#relationship-bar").findFirst().orElseThrow();
            topicButtonElements = ui.select(".topic").toList();

            registerTitleValues();
            registerContentValues(getDefaultDialogue());
            registerRelationshipBarValues();
            registerExitButtonEvent();
            registerTopicButtonEvent();

        }
    }

    public ModularUI createModularUI() {
        return ModularUI.of(ui, player);
    }

    private void registerExitButtonEvent() {
        if (exitButtonElement instanceof Button exitButton) {
            exitButton.setOnClick(event -> {
                player.closeContainer();
            });
        }
    }

    private void registerTitleValues() {
        if (titleElement instanceof Label title) {
            title.bindDataSource(SupplierDataSource.of(entity::getCustomName));
        }
    }

    private void registerContentValues(String text) {
        if (contentElement instanceof Label content) {
            content.setText(text);
        }
    }

    private void registerRelationshipBarValues() {
        if (relationShipBarElement instanceof ProgressBar relationshipBar) {
            relationshipBar.setMinValue(0);
            relationshipBar.setMaxValue(100);

            relationshipBar.label.bindDataSource(SupplierDataSource.of(
                () -> Component.literal(String.valueOf(entity.getRelationship())).append("/100"))
            );
            relationshipBar.bindDataSource(SupplierDataSource.of(() -> (float) entity.getRelationship()));
        }
    }

    private void registerTopicButtonEvent() {
        topicButtonElements.forEach(topicButtonElement -> {
            if (topicButtonElement instanceof Button topicButton) {
                RandomSource random = RandomSource.create();

                topicButton.setOnServerClick(event -> {
                    RPGMobs.LOGGER.info("SERVER CLICK");

                    if (random.nextBoolean()) {
                        entity.setRelationship(entity.getRelationship() + 1);
                    } else {
                        entity.setRelationship(entity.getRelationship() - 1);
                    }
                });

                topicButton.setOnClick(event -> {
                    registerContentValues("You want to talk about " + topicButton.text.getText().getString() + "?");
                });
            }
        });
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
