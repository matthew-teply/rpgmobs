package com.conanthecivilian.rpgmobs.entity;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.entity.custom.AbstractHumanlikeEntity;
import com.conanthecivilian.rpgmobs.entity.custom.dwarf.IDwarfFaction;
import com.conanthecivilian.rpgmobs.entity.custom.human.IHumanFaction;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

@EventBusSubscriber(modid = RPGMobs.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModSpawnPlacements {
    @SubscribeEvent
    public static void onRegisterSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        RPGMobs.LOGGER.info("⚔️ RPGMOBS: Registering spawn placements event has been reached! ⚔️");

        event.register(
            ModEntities.HUMAN_GUARD.get(),
            SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            IHumanFaction::checkHumanSpawnRules,
            RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
            ModEntities.HUMAN_ARCHER.get(),
            SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            IHumanFaction::checkHumanSpawnRules,
            RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
            ModEntities.DWARF_GUARD.get(),
            SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            IDwarfFaction::checkDwarfSpawnRules,
            RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
            ModEntities.DWARF_ARCHER.get(),
            SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            IDwarfFaction::checkDwarfSpawnRules,
            RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
            ModEntities.ORC_FIGHTER.get(),
            SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            AbstractHumanlikeEntity::checkHumanlikeSpawnRules,
            RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
            ModEntities.ORC_ARCHER.get(),
            SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            AbstractHumanlikeEntity::checkHumanlikeSpawnRules,
            RegisterSpawnPlacementsEvent.Operation.OR
        );
    }
}
