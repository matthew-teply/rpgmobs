package com.conanthecivilian.rpgmobs.entity.custom.human;

import com.conanthecivilian.rpgmobs.entity.custom.AbstractHumanlikeEntity;
import com.conanthecivilian.rpgmobs.entity.custom.monster.IMonsterFaction;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.StructureTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.LevelAccessor;

import java.util.ArrayList;

public interface IHumanFaction {
    default ArrayList<Class<?>> getEnemyList() {
        ArrayList<Class<?>> enemies = new ArrayList<>();

        enemies.add(IMonsterFaction.class);
        enemies.add(Enemy.class);

        return enemies;
    }

    static boolean getSpawnRules(
        EntityType<? extends AbstractHumanlikeEntity> humanlike,
        LevelAccessor level,
        MobSpawnType spawnType,
        BlockPos pos,
        RandomSource random
    ) {
        if (!level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), humanlike)) {
            return false;
        }

        if (level instanceof ServerLevel serverLevel) {
            var villageCheck = serverLevel.structureManager().getStructureWithPieceAt(pos, StructureTags.VILLAGE);

            if (villageCheck.isValid()) {
                return true;
            }
        }

        return random.nextFloat() < 0.20F;
    }
}
