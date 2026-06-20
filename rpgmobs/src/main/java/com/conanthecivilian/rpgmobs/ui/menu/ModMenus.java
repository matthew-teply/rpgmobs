package com.conanthecivilian.rpgmobs.ui.menu;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.lowdragmc.lowdraglib2.gui.factory.PlayerUIMenuType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
        DeferredRegister.create(Registries.MENU, "rpgmobs");


    public static void registerMenus() {
        PlayerUIMenuType.register(
            RPGMobs.CONVERSATION_UI_ID,
            player -> {
                return p -> ConversationMenu.createModularUI(p);
            }
        );
    }
}