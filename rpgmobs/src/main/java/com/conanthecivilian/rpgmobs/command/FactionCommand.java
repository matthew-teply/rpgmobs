package com.conanthecivilian.rpgmobs.command;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.entity.faction.Faction;
import com.conanthecivilian.rpgmobs.entity.faction.template.FactionTemplate;
import com.conanthecivilian.rpgmobs.entity.trait.Trait;
import com.conanthecivilian.rpgmobs.repository.FactionRepository;
import com.conanthecivilian.rpgmobs.repository.FactionTemplateRepository;
import com.conanthecivilian.rpgmobs.repository.TraitRepository;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@EventBusSubscriber(modid = RPGMobs.MODID)
public class FactionCommand {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("rpgmobs")
                .requires(source -> source.hasPermission(2))
                .then(
                    Commands.literal("factions")
                        .then(Commands.literal("all")
                            .executes(context -> listAllFactions(context.getSource())))
                        .then(Commands.literal("active")
                            .executes(context -> listActiveFactions(context.getSource())))
                        .then(Commands.literal("inactive")
                            .executes(context -> listInactiveFactions(context.getSource())))
                        .then(Commands.literal("get")
                            .then(Commands.argument("uuid", StringArgumentType.string())
                                .executes(context ->
                                    getFaction(context.getSource(), StringArgumentType.getString(context, "uuid"))
                                )
                            )
                        )
                )
        );
    }

    private static int listAllFactions(CommandSourceStack source) {
        FactionRepository factionRepository = FactionRepository.get(source.getServer());

        HashMap<UUID, Faction> factions = factionRepository.getAllFactions();

        source.sendSuccess(() -> Component.literal(factions.size() + " factions"), false);
        source.sendSuccess(() -> Component.literal("========="), false);

        int index = 0;
        for (Faction faction : factions.values()) {
            index++;

            int finalIndex = index;
            source.sendSuccess(() -> Component.literal(finalIndex + ". " + faction.getName() + " (" + faction.getUUID() + ")"), false);
        }

        return 1;
    }

    private static int listActiveFactions(CommandSourceStack source) {
        FactionRepository factionRepository = FactionRepository.get(source.getServer());

        List<UUID> activeFactions = factionRepository.getActiveFactions();

        source.sendSuccess(() -> Component.literal(activeFactions.size() + " active factions"), false);
        source.sendSuccess(() -> Component.literal("========="), false);

        int index = 0;

        for (UUID factionId : activeFactions) {
            index++;

            Faction faction = factionRepository.getFaction(factionId);
            FactionTemplate factionTemplate = FactionTemplateRepository.get(faction.getTemplateId());

            int finalIndex = index;
            source.sendSuccess(
                () ->
                    Component
                        .literal(finalIndex + ". " + faction.getName() + " - " + factionTemplate.getLabel() + " (" + faction.getUUID() + ")")
                        .withColor(faction.getColor())
                , false
            );
        }

        return 1;
    }

    private static int listInactiveFactions(CommandSourceStack source) {
        FactionRepository factionRepository = FactionRepository.get(source.getServer());

        List<UUID> inactiveFactions = factionRepository.getInactiveFactions();

        source.sendSuccess(() -> Component.literal(inactiveFactions.size() + " inactive factions"), false);
        source.sendSuccess(() -> Component.literal("========="), false);

        int index = 0;

        for (UUID factionId : inactiveFactions) {
            index++;

            Faction faction = factionRepository.getFaction(factionId);

            int finalIndex = index;
            source.sendSuccess(() -> Component.literal(finalIndex + ". " + faction.getName() + " (" + faction.getUUID() + ")"), false);
        }

        return 1;
    }

    private static int getFaction(CommandSourceStack source, String factionId) {
        FactionRepository factionRepository = FactionRepository.get(source.getServer());

        Faction faction = factionRepository.getFaction(UUID.fromString(factionId));

        if (faction == null) {
            source.sendFailure(Component.literal("Faction with id " + factionId + " does not exist"));
            return 0;
        }

        StringBuilder traitsString = new StringBuilder();

        for (ResourceLocation traitId : faction.getTraits()) {
            Trait trait = TraitRepository.get(traitId);

            if (trait == null) {
                continue;
            }

            if (traitsString.isEmpty()) {
                traitsString.append(trait.label());
            } else {
                traitsString.append(", ");
                traitsString.append(trait.label());
            }
        }

        StringBuilder racesString = new StringBuilder();

        for (ResourceLocation raceId : faction.getRaces()) {
            if (racesString.isEmpty()) {
                racesString.append(raceId);
            } else {
                racesString.append(", ");
                racesString.append(raceId);
            }
        }

        FactionTemplate factionTemplate = FactionTemplateRepository.get(faction.getTemplateId());

        source.sendSuccess(() -> Component.literal(""), false);
        source.sendSuccess(() -> Component.literal("Name: " + faction.getName()), false);
        source.sendSuccess(() -> Component.literal("Label: " + factionTemplate.getLabel()), false);
        source.sendSuccess(() -> Component.literal("Color: " + faction.getColor()), false);

        source.sendSuccess(() -> Component.literal("Traits: " + traitsString), false);
        source.sendSuccess(() -> Component.literal("Races: " + racesString), false);

        source.sendSuccess(() -> Component.literal("Lore data: " + faction.getLoreData()), false);
        source.sendSuccess(() -> Component.literal("Template ID: " + faction.getTemplateId()), false);
        source.sendSuccess(() -> Component.literal(""), false);

        source.sendSuccess(() -> Component.literal("====== Diplomacy ======"), false);
        source.sendSuccess(() -> Component.literal(""), false);

        source.sendSuccess(() -> Component.literal("=== Allies ==="), false);
        for (UUID diplomacyFactionId : faction.getDiplomacy().allies()) {
            logDiplomacy(source, diplomacyFactionId);
        }
        source.sendSuccess(() -> Component.literal(""), false);

        source.sendSuccess(() -> Component.literal("=== Neutral ==="), false);
        for (UUID diplomacyFactionId : faction.getDiplomacy().neutral()) {
            logDiplomacy(source, diplomacyFactionId);
        }
        source.sendSuccess(() -> Component.literal(""), false);

        source.sendSuccess(() -> Component.literal("=== Enemies ==="), false);
        for (UUID diplomacyFactionId : faction.getDiplomacy().enemies()) {
            logDiplomacy(source, diplomacyFactionId);
        }

        return 1;
    }

    private static void logDiplomacy(CommandSourceStack source, UUID factionId) {
        FactionRepository factionRepository = FactionRepository.get(source.getServer());

        Faction faction = factionRepository.getFaction(factionId);
        FactionTemplate factionTemplate = FactionTemplateRepository.get(faction.getTemplateId());

        MutableComponent factionLog = Component
            .literal(faction.getName() + " - " + factionTemplate.getLabel())
            .withColor(faction.getColor());

        if (faction.getLoreData().yearDestroyed().isPresent()) {
            factionLog
                .append(" (Destroyed in " + faction.getLoreData().yearDestroyed().get() + ")")
                .withStyle(ChatFormatting.GRAY);
        }

        source.sendSuccess(() -> factionLog, false);
    }
}
