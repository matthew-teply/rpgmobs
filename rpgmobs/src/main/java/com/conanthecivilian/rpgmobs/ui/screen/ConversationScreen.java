package com.conanthecivilian.rpgmobs.ui.screen;

import com.conanthecivilian.rpgmobs.ui.menu.ConversationMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ConversationScreen extends AbstractContainerScreen<ConversationMenu> {

    public ConversationScreen(ConversationMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public void init() {
        // the modular widget has already added + init by events
        //this.imageWidth = (int) this.getMenu().getModularUI().getWidth();
        //this.imageHeight = (int) this.getMenu().getModularUI().getHeight();

        super.init();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {

    }
}
