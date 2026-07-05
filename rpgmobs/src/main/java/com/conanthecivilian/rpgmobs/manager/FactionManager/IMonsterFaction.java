package com.conanthecivilian.rpgmobs.manager.FactionManager;

import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;

public interface IMonsterFaction {
    default ArrayList<Class<?>> getEnemyList() {
        ArrayList<Class<?>> enemies = new ArrayList<>();

        enemies.add(Player.class);
        enemies.add(IHumanFaction.class);
        enemies.add(IDwarfFaction.class);
        enemies.add(AbstractGolem.class);
        enemies.add(Enemy.class);
        enemies.add(AbstractVillager.class);

        return enemies;
    }
}
