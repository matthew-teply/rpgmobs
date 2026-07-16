package com.conanthecivilian.rpgmobs.manager.FactionManager;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.entity.npc.dwarf.AbstractDwarf;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.StructureTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.LevelAccessor;

import java.util.ArrayList;

public interface IDwarfFaction {
    default ArrayList<Class<?>> getEnemyList() {
        ArrayList<Class<?>> enemies = new ArrayList<>();

        enemies.add(IMonsterFaction.class);
        enemies.add(Enemy.class);

        return enemies;
    }

    static boolean checkDwarfSpawnRules(
        EntityType<? extends AbstractDwarf<?>> entity,
        LevelAccessor level,
        MobSpawnType spawnType,
        BlockPos pos,
        RandomSource random
    ) {
        float spawnChance = 0.5f;

        if (!level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), entity)) {
            return false;
        }

        if (pos.getY() <= level.getSeaLevel()) {
            if (level instanceof ServerLevel serverLevel) {
                var mineshaftCheck = serverLevel.structureManager().getStructureWithPieceAt(pos, StructureTags.MINESHAFT);

                if (mineshaftCheck.isValid()) {
                    spawnChance = 1f;
                }
            }
        } else {
            spawnChance = 0.1f;
        }

        boolean isSpawned = random.nextFloat() < spawnChance;

        if (isSpawned) {
            RPGMobs.LOGGER.debug("Spawned {} at [{} {} {}]",
                entity.getDescriptionId(), pos.getX(), pos.getY(), pos.getZ());
        }

        return isSpawned;
    }
}
