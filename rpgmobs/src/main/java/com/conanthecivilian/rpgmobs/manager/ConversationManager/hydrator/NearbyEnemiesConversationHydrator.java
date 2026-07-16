package com.conanthecivilian.rpgmobs.manager.ConversationManager.hydrator;

import com.conanthecivilian.rpgmobs.entity.npc.AbstractNPC;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class NearbyEnemiesConversationHydrator implements IConversationHydrator {
    private static final int MAX_HOSTILES_LISTED = 5;
    private static final String HYDRATION_ELEMENT = "{enemies_list}";

    @Override
    public String hydrate(Player player, AbstractNPC<?> entity, String rawText) {
        List<LivingEntity> nearestHostiles = entity
            .getBrain()
            .getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES)
            .map(livingEntities -> livingEntities
                .stream()
                .filter(
                    livingEntity ->
                        entity.factionManager.isEnemyFaction(livingEntity) && !livingEntity.isDeadOrDying()
                )
                .toList())
            .orElse(null);

        String[] enemiesList = {""};

        if (nearestHostiles == null || nearestHostiles.isEmpty()) {
            return null;
        }

        List<Class<?>> listedHostiles = new ArrayList<>();
        AtomicInteger hostileCount = new AtomicInteger(0);

        nearestHostiles.forEach(hostile -> {
            if (hostileCount.get() == MAX_HOSTILES_LISTED) {
                return;
            }

            hostileCount.getAndIncrement();

            if (nearestHostiles.size() == 1 || hostileCount.get() == 1) {
                enemiesList[0] += "a " + hostile.getName().getString();
            } else if (hostileCount.get() == MAX_HOSTILES_LISTED || hostileCount.get() == nearestHostiles.size()) {
                if (listedHostiles.contains(hostile.getClass())) {
                    enemiesList[0] += ", and another " + hostile.getName().getString();
                } else {
                    enemiesList[0] += ", and a " + hostile.getName().getString();
                }
            } else {
                if (listedHostiles.contains(hostile.getClass())) {
                    enemiesList[0] += ", another " + hostile.getName().getString();
                } else {
                    enemiesList[0] += ", a " + hostile.getName().getString();
                }
            }

            listedHostiles.add(hostile.getClass());
        });

        if (!enemiesList[0].isBlank()) {
            return rawText.replace(HYDRATION_ELEMENT, enemiesList[0]);
        }

        return null;
    }

    @Override
    public ResourceLocation getId() {
        return ResourceLocation.parse("rpgmobs:conversation_hydrator_nearby_enemies");
    }
}
