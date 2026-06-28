package com.conanthecivilian.rpgmobs.screen.custom.conversation;

import com.conanthecivilian.rpgmobs.entity.custom.AbstractHumanlikeEntity;
import com.conanthecivilian.rpgmobs.screen.ModMenuTypes;
import com.lowdragmc.lowdraglib2.gui.holder.ModularUIContainerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

public class ConversationMenu extends ModularUIContainerMenu {
    public ConversationMenu(int windowID, Inventory inventory, AbstractHumanlikeEntity<?> entity) {
        super(ModMenuTypes.CONVERSATION_MENU.get(), windowID, inventory, entity);
    }

    private static SimpleMenuProvider getMenuProvider(AbstractHumanlikeEntity<?> entity) {
        return new SimpleMenuProvider(
            (windowId, playerInventory, p) -> new ConversationMenu(windowId, playerInventory, entity),
            Component.literal("Conversation")
        );
    }

    /**
     * Opens conversation menu between the player on the server and an entity
     *
     * @param player Server player to initiate the conversation for
     * @param entity The entity that the player is holding a conversation with
     */
    public static void open(ServerPlayer player, AbstractHumanlikeEntity<?> entity) {
        player.openMenu(
            ConversationMenu.getMenuProvider(entity),
            buf -> buf.writeInt(entity.getId())
        );
    }

    /**
     * Builds a client-side menu type, determines how to get the entity on client-side
     */
    public static MenuType<ModularUIContainerMenu> getMenuType() {
        return IMenuTypeExtension.create((windowId, inventory, data) -> {
            int entityId = data.readInt();
            Entity entity = inventory.player.level().getEntity(entityId);

            if (entity instanceof AbstractHumanlikeEntity<?> humanlikeEntity) {
                return new ConversationMenu(windowId, inventory, humanlikeEntity);
            }

            throw new Error("Conversation entity not found");
        });
    }
}
