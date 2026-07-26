package com.conanthecivilian.rpgmobs.entity.npc;

import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

public class NPCSpawnPlacements {
    public static void onRegisterSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(
            NPCRegistry.NPC.get(),
            SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            AbstractNPC::checkNPCSpawnRules,
            RegisterSpawnPlacementsEvent.Operation.OR
        );
    }
}
