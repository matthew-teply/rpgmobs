package com.conanthecivilian.rpgmobs.command;

import com.conanthecivilian.rpgmobs.ModAttachments;
import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.accessor.IConversationTopicsAccessor;
import com.conanthecivilian.rpgmobs.manager.ConversationManager.record.UnlockedConversationTopics;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.Collection;
import java.util.Collections;

@EventBusSubscriber(modid = RPGMobs.MODID)
public class ResetTopicsCommand {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("rpgmobs")
                .requires(source -> source.hasPermission(2))
                .then(
                    Commands.literal("resettopics")
                        .then(Commands.argument("targets", EntityArgument.players()))
                        .executes(context -> reset(
                            context.getSource(),
                            EntityArgument.getPlayers(context, "targets")
                        ))
                        .executes(context -> reset(
                            context.getSource(),
                            Collections.singleton(context.getSource().getPlayerOrException())
                        ))
                )
        );
    }

    private static int reset(CommandSourceStack source, Collection<ServerPlayer> targets) {
        int count = 0;

        for (ServerPlayer player : targets) {
            if (player instanceof IConversationTopicsAccessor accessor) {
                int playerUnlockedTopicsSize = accessor.getConversationTopics().size();
                int playerDefaultTopicsSize = accessor.getDefaultConversationTopics().size();

                player.setData(
                    ModAttachments.PLAYER_UNLOCKED_CONVERSATION_TOPICS.get(),
                    new UnlockedConversationTopics(accessor.getDefaultConversationTopics())
                );

                player.sendSystemMessage(
                    Component.literal(
                        "Your conversation topics have been wiped, " + (playerUnlockedTopicsSize - playerDefaultTopicsSize) + " in total."
                    )
                );
                count++;
            }
        }

        int finalCount = count;
        source.sendSuccess(() -> Component.literal("Successfully wiped topics for " + finalCount + " player(s)."), true);
        return count; // Returns the number of affected players
    }
}
