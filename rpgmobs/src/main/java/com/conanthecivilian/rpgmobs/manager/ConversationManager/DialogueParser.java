package com.conanthecivilian.rpgmobs.manager.ConversationManager;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DialogueParser {
    // Matches anything surrounded by curly braces, e.g., {player_name}
    private static final Pattern TOKEN_PATTERN = Pattern.compile("\\{([^}]+)}");

    public static String parse(String rawDialogue, Player player, LivingEntity npc) {
        if (rawDialogue == null || rawDialogue.isEmpty()) {
            return "";
        }

        Matcher matcher = TOKEN_PATTERN.matcher(rawDialogue);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String token = matcher.group(1);
            String replacement = resolveToken(token, player, npc);

            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    private static String resolveToken(String token, Player player, LivingEntity npc) {
        return switch (token.toLowerCase()) {
            case "player_name" -> player.getName().getString();
            case "entity_name" ->
                npc.getCustomName() != null ? npc.getCustomName().getString() : npc.getName().getString();
            default -> "{" + token + "}";
        };
    }
}