package com.conanthecivilian.rpgmobs.manager.ConversationManager.hydrator;

import com.conanthecivilian.rpgmobs.entity.custom.AbstractHumanlikeEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultConversationHydrator implements IConversationHydrator {
    private static final Pattern TOKEN_PATTERN = Pattern.compile("\\{([^}]+)}");

    @Override
    public String hydrate(Player player, AbstractHumanlikeEntity<?> entity, String rawText) {
        if (rawText == null || rawText.isBlank()) {
            return null;
        }

        Matcher matcher = TOKEN_PATTERN.matcher(rawText);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String token = matcher.group(1);
            String replacement = this.resolveToken(token, player, entity);

            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    @Override
    public ResourceLocation getId() {
        return ResourceLocation.parse("rpgmobs:conversation_hydrator_default");
    }

    private String resolveToken(String token, Player player, LivingEntity entity) {
        return switch (token.toLowerCase()) {
            case "player_name" -> player.getName().getString();
            case "entity_name" ->
                entity.getCustomName() != null ? entity.getCustomName().getString() : entity.getName().getString();
            default -> "{" + token + "}";
        };
    }
}
