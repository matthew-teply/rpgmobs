package com.conanthecivilian.rpgmobs.command;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.manager.LoreManager.LoreEvent;
import com.conanthecivilian.rpgmobs.repository.LoreRepository;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.List;

@EventBusSubscriber(modid = RPGMobs.MODID)
public class HistoryCommand {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("rpgmobs")
                .requires(source -> source.hasPermission(2))
                .then(
                    Commands.literal("history")
                        .then(Commands.literal("list")
                            .executes(context -> listHistory(context.getSource())))
                        .then(Commands.literal("inspect")
                            .then(Commands.argument("year", IntegerArgumentType.integer(0))
                                .executes(context ->
                                    inspectYear(context.getSource(), IntegerArgumentType.getInteger(context, "year"))
                                )
                            )
                        )
                )
        );
    }

    private static int listHistory(CommandSourceStack source) {
        LoreRepository loreRepository = LoreRepository.get(source.getServer());

        for (List<LoreEvent> loreEvents : loreRepository.getAllChronologically().values()) {
            source.sendSuccess(() -> Component.literal(""), false);
            source.sendSuccess(
                () -> Component.literal("=== Year " + loreEvents.getFirst().year() + " ==="),
                false
            );

            for (LoreEvent loreEvent : loreEvents) {
                source.sendSuccess(() -> Component.literal(
                        loreEvent.description()),
                    false);
            }
        }

        return 1;
    }

    private static int inspectYear(CommandSourceStack source, int year) {
        LoreRepository loreRepository = LoreRepository.get(source.getServer());

        List<LoreEvent> loreEvents = loreRepository.getEventsByYear(year);

        if (loreEvents == null) {
            source.sendFailure(Component.literal("No events in year " + year));
            return 0;
        }

        source.sendSuccess(() -> Component.literal(""), false);
        source.sendSuccess(
            () -> Component.literal("=== Year " + year + " ==="),
            false
        );

        for (LoreEvent loreEvent : loreEvents) {
            source.sendSuccess(() -> Component.literal(
                    loreEvent.description()),
                false);
        }

        return 1;
    }
}
