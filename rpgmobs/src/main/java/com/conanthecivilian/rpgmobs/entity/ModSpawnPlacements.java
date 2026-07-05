package com.conanthecivilian.rpgmobs.entity;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.entity.npc.NPCSpawnPlacements;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

@EventBusSubscriber(modid = RPGMobs.MODID)
public class ModSpawnPlacements {
    @SubscribeEvent
    public static void onRegisterSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        NPCSpawnPlacements.onRegisterSpawnPlacements(event);
    }
}
