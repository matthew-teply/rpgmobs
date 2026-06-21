package com.conanthecivilian.rpgmobs.screen.custom.conversation;

import com.conanthecivilian.rpgmobs.entity.custom.AbstractHumanlikeEntity;
import com.lowdragmc.lowdraglib2.gui.sync.bindings.impl.SupplierDataSource;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.gui.ui.UI;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.data.Horizontal;
import com.lowdragmc.lowdraglib2.gui.ui.data.ScrollerMode;
import com.lowdragmc.lowdraglib2.gui.ui.data.Vertical;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Button;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ProgressBar;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ScrollerView;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class ConversationUI extends ConversationMenu {
    private final static int FONT_SIZE = 8;

    public ConversationUI(int windowID, Inventory inventory, AbstractHumanlikeEntity<?> entity) {
        super(windowID, inventory, entity);
    }

    public static ModularUI createModularUI(
        Player player,
        AbstractHumanlikeEntity<?> entity
    ) {
        var root = new UIElement()
            .lss("width", "100%")
            .lss("height", "100%")
            .lss("padding-all", 10)
            .lss("align-items", "center")
            .lss("justify-content", "end");

        var container = new UIElement()
            .lss("display", "flex")
            .lss("width", "85%")
            .lss("height", "50%")
            .lss("background", "built-in(ui-mc:RECT)")
            .lss("padding-all", 10)
            .lss("gap-all", 5)
            .lss("font-size", FONT_SIZE);

        var title = createTitle("Conversation with " + entity.getCustomName().getString());

        var contentContainer = createContentContainer(entity);
        var actionsContainer = createActionsContainer(entity);

        var conversationContainer = new UIElement()
            .lss("display", "flex")
            .lss("flex-direction", "row")
            .lss("flex", 1)
            .lss("gap-all", 5)
            .lss("background", "empty");

        conversationContainer.addChildren(
            contentContainer,
            actionsContainer
        );

        container.addChildren(
            title,
            conversationContainer
        );

        root.addChild(container);

        return ModularUI.of(UI.of(root), player);
    }

    private static UIElement createTitle(String title) {
        return new Label().setText(title)
            .textStyle(style -> style
                .textAlignHorizontal(Horizontal.CENTER)
                .textAlignVertical(Vertical.CENTER)
            )
            .lss("width", "100%")
            .lss("align-items", "center")
            .lss("justify-content", "center")
            .lss("padding-all", 5)
            .lss("font-size", FONT_SIZE);
    }

    private static UIElement createContentContainer(AbstractHumanlikeEntity<?> entity) {
        var contentContainer = new UIElement()
            .lss("background", "empty")
            .lss("flex", 1);

        var contentScrollerView = new ScrollerView();

        contentScrollerView
            .lss("flex", 3)
            .lss("height", "100%");

        contentScrollerView.viewPort.lss("background", "built-in(ui-mc:BORDER)");
        contentScrollerView.scrollerStyle(style -> style.mode(ScrollerMode.VERTICAL));

        var decreaseButton = new Button().setText("-");
        var increaseButton = new Button().setText("+");

        decreaseButton.text.lss("font-size", FONT_SIZE);
        increaseButton.text.lss("font-size", FONT_SIZE);

        decreaseButton.setOnServerClick(event -> entity.setRelationship(entity.getRelationship() - 1));
        increaseButton.setOnServerClick(event -> entity.setRelationship(entity.getRelationship() + 1));

        var content = new Label().bindDataSource(SupplierDataSource.of(
            () -> Component.literal("Relationship: ").append(String.valueOf(entity.getRelationship()))
        )).lss("font-size", FONT_SIZE);


        contentScrollerView.addScrollViewChildren(
            content,
            increaseButton,
            decreaseButton
        );

        contentContainer.addChildren(contentScrollerView);

        return contentScrollerView;
    }

    private static UIElement createActionsContainer(AbstractHumanlikeEntity<?> entity) {
        var actionsContainer = new UIElement()
            .lss("background", "empty")
            .lss("flex", 1);

        var topicsScrollerView = new ScrollerView();

        var relationshipBar = createRelationshipBar(entity);
        var topicsContainer = new UIElement().lss("flex", 1);
        var goodbyeButton = new Button().setText("Goodbye");

        goodbyeButton.text.lss("font-size", FONT_SIZE);

        goodbyeButton.setOnClick(event -> {
            assert Minecraft.getInstance().player != null;
            Minecraft.getInstance().player.closeContainer();
        });

        topicsScrollerView
            .lss("flex", 1)
            .lss("height", "100%")
            .lss("display", "flex")
            .lss("flex-direction", "column")
            .lss("gap", 5);

        topicsScrollerView.viewPort.lss("background", "built-in(ui-mc:BORDER)");

        topicsScrollerView.scrollerStyle(style -> style.mode(ScrollerMode.VERTICAL));

        topicsScrollerView.addScrollViewChild(topicsContainer);

        actionsContainer.addChildren(
            relationshipBar,
            topicsContainer,
            goodbyeButton
        );

        return actionsContainer;
    }

    private static UIElement createRelationshipBar(AbstractHumanlikeEntity<?> entity) {
        var relationshipContainer = new UIElement()
            .lss("background", "empty")
            .lss("display", "flex")
            .lss("flex-direction", "row")
            .lss("gap-all", 2)
            .lss("width", "100%");

        var relationshipBar = new ProgressBar();

        relationshipBar.lss("flex", 1);

        relationshipBar.setMaxValue(100);
        relationshipBar.setMinValue(0);

        relationshipBar.label.textStyle(textStyle -> textStyle.fontSize(6));

        relationshipBar.label.bindDataSource(SupplierDataSource.of(
            () -> Component.literal(String.valueOf(entity.getRelationship())).append("/100"))
        );
        relationshipBar.bindDataSource(SupplierDataSource.of(() -> (float) entity.getRelationship()));

        relationshipContainer.addChildren(
            relationshipBar
        );

        return relationshipContainer;
    }
}
