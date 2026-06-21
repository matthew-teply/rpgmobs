package com.conanthecivilian.rpgmobs.screen.custom.conversation;

import com.lowdragmc.lowdraglib2.gui.holder.ModularUIContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ConversationScreen extends ModularUIContainerScreen {
    public ConversationScreen(ConversationMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }
}