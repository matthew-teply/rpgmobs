package com.conanthecivilian.rpgmobs.service;

import com.conanthecivilian.rpgmobs.RPGMobs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;


public class FactionService {
    private static final String NBT_KEY_ENEMY_FACTIONS = "FactionEnemies";

    protected final ArrayList<Class<?>> enemyFactions = new ArrayList<>();

    public void addEnemyFaction(Class<?> enemyClass) {
        this.enemyFactions.add(enemyClass);
        this.onEnemyFactionsChanged();
    }

    public void addEnemyFactions(ArrayList<Class<?>> enemyList) {
        this.enemyFactions.addAll(enemyList);
        this.onEnemyFactionsChanged();
    }

    public void onEnemyFactionsChanged() {}

    public boolean isEnemyFaction(LivingEntity entity) {
        if (entity == null) return false;

        return this.enemyFactions.stream().anyMatch(enemyClass -> enemyClass.isAssignableFrom(entity.getClass()));
    }

    public void saveEnemyFactions(CompoundTag nbt) {
        ListTag enemiesListTag = new ListTag();

        for (Class<?> enemyFaction : this.enemyFactions) {
            enemiesListTag.add(StringTag.valueOf(enemyFaction.getName()));
        }

        nbt.put(NBT_KEY_ENEMY_FACTIONS, enemiesListTag);
    }

    public void loadEnemyFactions(CompoundTag nbt) {
        this.enemyFactions.clear();

        if (nbt.contains(NBT_KEY_ENEMY_FACTIONS, Tag.TAG_LIST)) {
            ListTag enemiesListTag = nbt.getList(NBT_KEY_ENEMY_FACTIONS, Tag.TAG_STRING);

            for (int i = 0; i < enemiesListTag.size(); i++) {
                String enemyFactionClassName = enemiesListTag.getString(i);

                try {
                    this.addEnemyFaction(Class.forName(enemyFactionClassName));
                } catch (ClassNotFoundException e) {
                    RPGMobs.LOGGER.error("Could not load enemy faction {}", enemyFactionClassName);
                }
            }
        }
    }
}
