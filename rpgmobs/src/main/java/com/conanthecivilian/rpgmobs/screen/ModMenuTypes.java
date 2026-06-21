package com.conanthecivilian.rpgmobs.screen;

import com.conanthecivilian.rpgmobs.screen.custom.conversation.ConversationMenu;
import com.lowdragmc.lowdraglib2.gui.holder.ModularUIContainerMenu;
import com.lowdragmc.lowdraglib2.gui.holder.ModularUIContainerScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
        DeferredRegister.create(Registries.MENU, "rpgmobs");

    public static final Supplier<MenuType<ModularUIContainerMenu>> CONVERSATION_MENU =
        MENUS.register("conversation_menu", ConversationMenu::getMenuType);

    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.CONVERSATION_MENU.get(), ModularUIContainerScreen::new);
    }
}
