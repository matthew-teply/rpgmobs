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

        /*
        event.register(
            NPCRegistry.HUMAN_GUARD.get(),
            SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            IHumanFaction::checkHumanSpawnRules,
            RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
            NPCRegistry.HUMAN_ARCHER.get(),
            SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            IHumanFaction::checkHumanSpawnRules,
            RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
            NPCRegistry.DWARF_GUARD.get(),
            SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            IDwarfFaction::checkDwarfSpawnRules,
            RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
            NPCRegistry.DWARF_ARCHER.get(),
            SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            IDwarfFaction::checkDwarfSpawnRules,
            RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
            NPCRegistry.ORC_FIGHTER.get(),
            SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            AbstractNPCEntity::checkNPCSpawnRules,
            RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
            NPCRegistry.ORC_ARCHER.get(),
            SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            AbstractNPCEntity::checkNPCSpawnRules,
            RegisterSpawnPlacementsEvent.Operation.OR
        );
        */
    }
}
