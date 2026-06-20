package com.conanthecivilian.rpgmobs.ui.menu;

import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.gui.ui.UI;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.data.ScrollerMode;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ScrollerView;
import com.lowdragmc.lowdraglib2.gui.ui.elements.SplitView;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ConversationMenu extends AbstractContainerMenu {
    public ConversationMenu(@Nullable MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    public static ModularUI createModularUI(Player player) {
        var root = new UIElement()
            .lss("width", "100%")
            .lss("height", "100%")
            .lss("padding-all", 10)
            .lss("align-items", "center")
            .lss("justify-content", "end");

        var container = new UIElement()
            .lss("width", "100%")
            .lss("height", "50%")
            .lss("background", "built-in(ui-gdp:BORDER)")
            .lss("gap-all", 5);

        var splitView = new SplitView.Horizontal().setPercentage(70);

        var title = ConversationMenu.createTitle();

        var conversationContent = ConversationMenu.createConversationContent();
        var conversationTopics = ConversationMenu.createConversationTopics();

        splitView.left(conversationContent);
        splitView.right(conversationTopics);

        container.addChild(title);
        container.addChild(splitView);

        root.addChild(container);

        return ModularUI.of(UI.of(root), player);
    }

    private static UIElement createTitle() {
        var titleContainer = new UIElement()
            .lss("width", "100%")
            .lss("align-items", "center")
            .lss("justify-content", "center")
            .lss("padding-all", 5);

        titleContainer.addChild(new Label().setText("Title"));

        return titleContainer;
    }

    private static UIElement createConversationContent() {
        var contentScrollerView = new ScrollerView();

        contentScrollerView.lss("height", "100%");
        contentScrollerView.scrollerStyle(style -> style.mode(ScrollerMode.VERTICAL));

        var content = new Label().setText("Content");

        contentScrollerView.addScrollViewChild(content);

        return contentScrollerView;
    }

    private static UIElement createConversationTopics() {
        var topicsScrollerView = new ScrollerView();

        topicsScrollerView.lss("height", "100%");
        topicsScrollerView.scrollerStyle(style -> style.mode(ScrollerMode.VERTICAL));

        var topics = new Label().setText("Topics");

        topicsScrollerView.addScrollViewChild(topics);

        return topicsScrollerView;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }
}
