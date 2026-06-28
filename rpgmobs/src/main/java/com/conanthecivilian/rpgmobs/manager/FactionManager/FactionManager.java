package com.conanthecivilian.rpgmobs.manager.FactionManager;

import com.conanthecivilian.rpgmobs.RPGMobs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;

import java.util.ArrayList;

public class FactionManager {
    private static final String NBT_KEY_ENEMY_FACTIONS = "FactionEnemies";
    private static final String NBT_KEY_ALLY_FACTIONS = "FactionAllies";

    protected ArrayList<Class<?>> enemyFactions = new ArrayList<>();
    protected ArrayList<Class<?>> allyFactions = new ArrayList<>();

    public void addEnemyFaction(Class<?> enemyClass) {
        enemyFactions.add(enemyClass);
        this.onEnemyFactionsChanged();
    }

    public void addEnemyFactions(ArrayList<Class<?>> enemyList) {
        enemyFactions.addAll(enemyList);
        this.onEnemyFactionsChanged();
    }

    public void addAllyFaction(Class<?> allyClass) {
        allyFactions.add(allyClass);
    }

    public void addAllyFactions(ArrayList<Class<?>> allyList) {
        allyFactions.addAll(allyList);
    }

    public void onEnemyFactionsChanged() {
    }

    public boolean isEnemyFaction(LivingEntity entity) {
        if (entity == null || entity instanceof Creeper) return false;

        return enemyFactions.stream().anyMatch(enemyClass -> enemyClass.isAssignableFrom(entity.getClass()));
    }

    public boolean isAllyFaction(LivingEntity entity) {
        if (entity == null) return false;

        return allyFactions.stream().anyMatch(allyClass -> allyClass.isAssignableFrom(entity.getClass()));
    }

    public ArrayList<Class<?>> getEnemyFactions() {
        return enemyFactions;
    }

    public ArrayList<Class<?>> getAllyFactions() {
        return allyFactions;
    }

    public void saveFactionList(
        CompoundTag nbt,
        String nbtKey,
        ArrayList<Class<?>> factionList
    ) {
        ListTag enemiesListTag = new ListTag();

        for (Class<?> faction : factionList) {
            enemiesListTag.add(StringTag.valueOf(faction.getName()));
        }

        nbt.put(nbtKey, enemiesListTag);
    }

    public void loadFactionList(
        CompoundTag nbt,
        String nbtKey,
        ArrayList<Class<?>> factionList
    ) {
        if (nbt.contains(nbtKey, Tag.TAG_LIST)) {
            ListTag factionsListTag = nbt.getList(nbtKey, Tag.TAG_STRING);

            for (int i = 0; i < factionsListTag.size(); i++) {
                String factionClassName = factionsListTag.getString(i);

                try {
                    factionList.add(Class.forName(factionClassName));
                } catch (ClassNotFoundException e) {
                    RPGMobs.LOGGER.error("Could not load faction {}", factionClassName);
                }
            }
        }
    }

    public void saveFactions(CompoundTag nbt) {
        this.saveFactionList(nbt, NBT_KEY_ENEMY_FACTIONS, enemyFactions);
        this.saveFactionList(nbt, NBT_KEY_ALLY_FACTIONS, allyFactions);
    }

    public void loadFactions(CompoundTag nbt) {
        enemyFactions.clear();
        this.loadFactionList(nbt, NBT_KEY_ENEMY_FACTIONS, enemyFactions);

        allyFactions.clear();
        this.loadFactionList(nbt, NBT_KEY_ALLY_FACTIONS, allyFactions);
    }
}
